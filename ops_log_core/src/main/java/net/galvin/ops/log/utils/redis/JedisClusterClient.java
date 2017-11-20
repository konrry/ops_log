package net.galvin.ops.log.utils.redis;

import net.galvin.ops.log.utils.ExceptionFormatUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class JedisClusterClient {

	private final static Logger logger = LoggerFactory.getLogger(JedisClusterClient.class);

	/**
	 * 单利模式， 静态内部类实现
	 */
	public static class build {
		public static final JedisClusterClient jedisClusterClient = new JedisClusterClient();
	}
	public static JedisClusterClient get(){
		return build.jedisClusterClient;
	}

	private final int DEFULT_PORT = 6379;
	
	private Set<HostAndPort> jedisClusterNodes;
	private JedisPoolConfig jedisPoolConfig;
	private JedisCluster jedisCluster;
	private Boolean enable = false;
	private int timeout = 2000;
	private int maxRedirections = 10;

	/**
	 * 默认的构造方法
     */
	private JedisClusterClient(){
		try {
			this.init();
			this.check();
		} catch (Exception e) {
			this.enable = false;
		}

	}

	/**
	 * 初始化Jedis客户端
	 * @throws Exception
     */
	private void init() throws Exception {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("redis.properties");
		Properties properties = new Properties();
		properties.load(inputStream);

		// 初始化redis集群客户端
		String redisServers = properties.getProperty("cluster.redis.server");
		String tempEnable = (String) properties.get("cluster.redis.enable");
		if(!"true".equalsIgnoreCase(tempEnable)
				&& !"ok".equalsIgnoreCase(tempEnable)
				&& !"y".equalsIgnoreCase(tempEnable)){
			logger.error(" The cluster.redis.enable is not enable ... ");
			return;
		}
		if(StringUtils.isEmpty(redisServers)){
			logger.error(" The cluster.redis.server is empty ... ");
			return;
		}

		// 连接池的配置
		jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(30);
		jedisPoolConfig.setMaxIdle(20);
		jedisPoolConfig.setMinIdle(10);
		jedisPoolConfig.setBlockWhenExhausted(true);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(false);
		jedisPoolConfig.setTestOnReturn(false);
		jedisPoolConfig.setTestWhileIdle(false);
		this.jedisClusterNodes = this.getNodes(redisServers);
		this.jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxRedirections, jedisPoolConfig);
	}

	/**
	 * 检测是否初始化成功
     */
	private void check(){
		if(this.jedisCluster == null
				|| this.jedisCluster.getClusterNodes() == null
				|| this.jedisCluster.getClusterNodes().isEmpty()){
			logger.error("Redis Cluster not nodes");
		} else {
			this.enable = true;
			logger.info("Cluster nodes:"+jedisCluster.getClusterNodes().keySet());
		}
	}

	private Set<HostAndPort> getNodes(String redisServer) {
		String[] servers = redisServer.replaceAll(" ", "").split(",");
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		for (String ipPort : servers) {
			String host;
			int port = DEFULT_PORT;
			if(ipPort.contains(":")) {
				host = ipPort.split(":")[0];
				port = Integer.parseInt(ipPort.split(":")[1]);
			}else{
				host = ipPort;
			}
			jedisClusterNodes.add(new HostAndPort(host, port));
		}
		return jedisClusterNodes;
	}

	/**
	 * 判断键是否存在
	 * @param key
	 * @return
     */
	public boolean exists(String key){
		boolean exists = false;
		if(!isEnable()) return exists;
		if(key == null) return exists;
		try {
			exists = this.jedisCluster.exists(key);
		}catch (Exception e){
			logger.error(ExceptionFormatUtil.getTrace(e));
		}
		return exists;
	}

	/**
	 * 删除对象
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		if(!isEnable()) return false;
		if(key == null) return false;
		this.getJedisCluster().del(key);
		return true;
	}

	/**
	 * 缓存字符串对象
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, String value, int seconds) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		assert(seconds <= 0) : "seconds less than 0";
		String isOk = this.getJedisCluster().setex(key, seconds, value);
		if(!"OK".equals(isOk)) {
			if(logger.isInfoEnabled()) logger.info("set error : " + isOk);
			return false;
		}
		return true;
	}

	/**
	 * 获取字符串对象
	 * @param key
	 * @return
	 */
	public String get(String key) {
		if(!isEnable()) 
			return null;
		if(key == null) return null;
		return this.getJedisCluster().get(key);
	}

	/**
	 * 存放Map对象
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setMap(String key, Map<String, String> value) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		String isOk = this.getJedisCluster().hmset(key, value);
		if(!"OK".equals(isOk)) {
			if(logger.isInfoEnabled()) logger.info("set error : " + isOk);
			return false;
		}
		return true;
	}

	/**
	 * 获取Map对象
	 * @param key
	 * @return
     */
	public Map<String, String> getMap(String key) {
		if(!isEnable()) return null;
		if(key == null) return null;
		return this.getJedisCluster().hgetAll(key);
	}

	/**
	 * 存放字符串到Map类型的值中去
	 * @param key
	 * @param field
	 * @param mValue
	 * @return
	 */
	public boolean setMapField(String key, String field, String mValue) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(field == null) : "field is null";
		assert(mValue == null) : "mValue is null";
		this.getJedisCluster().hset(key, field, mValue);
		return true;
	}

	/**
	 * 获取Map对象中field对应的字符串
	 * @param key
	 * @param field
     * @return
     */
	public String getMapField(String key, String field) {
		if(!isEnable()) 
			return null;
		if(key == null) return null;
		if(field == null) return null;
		return this.getJedisCluster().hget(key, field);
	}
	
	public JedisCluster getJedisCluster() {
		if(!isEnable()) 
			return null;
		return jedisCluster;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
