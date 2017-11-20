package net.galvin.ops.log.utils;

import net.galvin.ops.log.utils.redis.JedisClusterClient;

/**
 * 幂等性检查
 */
public class IdempotentUtil {

    private static IdempotentUtil idempotentUtil = null;

    /**
     * 私有的构造函数
     */
    private IdempotentUtil(){}

    /**
     * 双重校验锁的单利模式
     * @return
     */
    public static IdempotentUtil get(){
        if(idempotentUtil == null){
            synchronized (IdempotentUtil.class){
                idempotentUtil = new IdempotentUtil();
            }
        }
        return idempotentUtil;
    }

    /**
     * 幂等性检查
     * @param msgId
     * @return true: 有重复的日志消息  false: 没有重复的日志消息
     */
    public boolean check(String msgId){
        String key = SysEnum.KEY.LOG_MSG_IDEMPOTENT_.name()+msgId;
        //检查当前的msg是否存在与redis缓存
        boolean exists = JedisClusterClient.get().exists(key);
        if(!exists){
            //缓存不存在，就保存一份到缓存中
            JedisClusterClient.get().set(key,msgId,SysEnum.KEY.LOG_MSG_IDEMPOTENT_.getSeconds());
        }
        return exists;
    }

}
