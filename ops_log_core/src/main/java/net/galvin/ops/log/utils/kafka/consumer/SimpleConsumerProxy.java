package net.galvin.ops.log.utils.kafka.consumer;

import net.galvin.ops.log.utils.ExceptionFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 简单消费者代理类
 */
public class SimpleConsumerProxy {

    private final Logger logger = LoggerFactory.getLogger(SimpleConsumerProxy.class);

    protected MsgConsumerGroup msgConsumerGroup;
    protected MsgProcessor msgProcessor;

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
    // 设置为true，消费者才会工作
    private Boolean receiveEnable = false;

    public void setMsgHandlers(MsgProcessor msgProcessor) {
        this.msgProcessor = msgProcessor;
    }
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
    public void setReceiveEnable(Boolean receiveEnable) {
        this.receiveEnable = receiveEnable;
    }

    public void consume() {
        if(this.receiveEnable){
            try {
                msgConsumerGroup = new DefaultMsgConsumerGroup(zookeeperServers,groupId, properties,topic,threadSum);
                this.msgConsumerGroup.execute(this.msgProcessor);
            }catch (Exception e){
                this.receiveEnable = false;
                logger.error(ExceptionFormatUtil.getTrace(e));
            }
        }else{
            logger.warn(" The receiveEnable is advised being true. If not true, any consumer will not be created!!! ");
        }
    }

    /**
     * 是否可以发消息, 只有sendFlag为true的情况下才会发消息
     * @return true： 可以发消息  false： 不能发消息
     */
    private final boolean checkSend(){
        return receiveEnable != null && receiveEnable;
    }

    public void shutdown(){
        if(this.receiveEnable){
            if(msgConsumerGroup != null){
                this.msgConsumerGroup.shutdown();
            }
        }
    }


}
