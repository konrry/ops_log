package com.lvmama.log.utils.kafka.consumer;

/**
 * 消息处理器
 */
public interface MsgProcessor {

    /**
     * 执行方法
     * @param msg
     */
    void execu(String msg, String topic, String groupId);

}
