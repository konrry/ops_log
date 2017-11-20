package com.lvmama.log.utils.kafka.producer;

/**
 * 消息生产者统一接口
 */
public interface MsgProducer {

    /**
     * 发消息的方法
     * @param topic 消息主题
     * @param key key
     * @param partKey 消息内容
     * @param msg 消息内容
     * @return true: 发送成功  false： 发送失败
     */
    boolean send(String topic, Object key, Object partKey, Object msg);

    /**
     * 发消息的方法
     * @param topic 消息主题
     * @param key key
     * @param msg 消息内容
     * @return true: 发送成功  false： 发送失败
     */
    boolean send(String topic, Object key, Object msg);

    /**
     * 发消息的方法
     * @param topic 消息主题
     * @param msg 消息内容
     * @return true: 发送成功  false： 发送失败
     */
    boolean send(String topic, Object msg);

    /**
     * 关闭生产者
     */
    void close();
}
