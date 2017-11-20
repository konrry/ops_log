package com.lvmama.log.utils.kafka.consumer;

import com.lvmama.log.utils.ExceptionFormatUtil;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  租模式消费者组
 */
public class DefaultMsgConsumerGroup implements MsgConsumerGroup {

    private final Logger logger = LoggerFactory.getLogger(DefaultMsgConsumerGroup.class);

    private ConsumerConnector consumerConnector = null;
    private static ExecutorService executorService = null;

    //zookeeper服务器
    private String zookeeperServers = null;
    //消费者组Id
    private String groupId = null;
    //属性读取
    private Properties properties = null;
    //主题
    private String topic;
    /*
        该参数决定了本实例在接收消息时，开启了多少个消费者来接受消息。
        一般只启用一个消费者，修改时请慎重。
     */
    private int threadSum = 1;

    public void setZookeeperServers(String zookeeperServers) {
        this.zookeeperServers = zookeeperServers;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public void setThreadSum(int threadSum) {
        this.threadSum = threadSum;
    }

    public DefaultMsgConsumerGroup(){}

    public DefaultMsgConsumerGroup(String zookeeperServers,String groupId,Properties properties, String topic,int threadSum){
        this.zookeeperServers = zookeeperServers;
        this.groupId = groupId;
        this.properties = properties;
        this.topic = topic;
        if (threadSum > 0)
            this.threadSum = threadSum;
        init();
    }


    public void init(){
        try {
            Properties props = getProps();
            ConsumerConfig consumerConfig = new ConsumerConfig(props);
            this.consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
    }

    /**
     * 获取默认的初始化属性
     * @return
     */
    private final Properties getProps() throws Exception {
        Properties props = new Properties();
        props.put("zookeeper.connect", this.zookeeperServers);
        props.put("group.id", this.groupId);
        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.connection.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "10000");
        props.put("auto.commit.interval.ms", "500");
        this.loadProp(props);
        return props;
    }

    /**
     * 加载用户配置属性
     * @param props
     */
    private final void loadProp(Properties props){
        if(this.properties != null && this.properties.size() > 0){
            Set keySet = this.properties.keySet();
            for(Object key : keySet){
                if(key != null){
                    props.put(key,this.properties.get(key));
                }
            }
        }
    }

    @Override
    public void shutdown(){
        /*
            Kafka在每次处理后不会立即更新zookeeper上的偏移值，她会休息上一段时间后提交。
                在这段时间内，你的消费者可能已经消费了一些消息，但并没有提交到zookeeper上。这样你可能会重复消费数据。
             同时一些时候，broker失败从新选取leader是也可能会导致重复消费消息。
             为了避免这种情况应该清理完成后再关闭，而不是直接使用kill -9命令。
             这里的例子是休息10秒后再关闭。
         */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        if(this.consumerConnector != null){
            this.consumerConnector.shutdown();
        }
        if(executorService != null){
            executorService.shutdown();
        }
    }

    @Override
    public void execute(MsgProcessor msgProcessor){
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(threadSum));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = this.consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = consumerMap.get(topic);
        if(executorService == null) {
            executorService = Executors.newFixedThreadPool(10);
        }
        for (final KafkaStream kafkaStream : kafkaStreams){
            DefaultMsgConsumer defaultMsgConsumer = new DefaultMsgConsumer(kafkaStream, msgProcessor, topic, groupId);
            executorService.submit(defaultMsgConsumer);
        }
    }

}
