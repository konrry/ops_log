package net.galvin.ops.log.bo;

import net.galvin.ops.log.comm.po.ComLog;

/**
 * 日志消息的逻辑对象
 */
public class LogMsgVo {

    private String topic;

    private String groupId;

    private String msgId;

    private ComLog comLog;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public ComLog getComLog() {
        return comLog;
    }

    public void setComLog(ComLog comLog) {
        this.comLog = comLog;
    }
}
