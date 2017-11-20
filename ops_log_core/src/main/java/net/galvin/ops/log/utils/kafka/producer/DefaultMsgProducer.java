package net.galvin.ops.log.utils.kafka.producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Set;

/**
 * 底层的消息生产者
 */
@Service
public class DefaultMsgProducer implements MsgProducer {

    private final Logger logger = LoggerFactory.getLogger(DefaultMsgProducer.class);

    //kafka生产者实例
    private Producer<Object,Object> kafkaProducer;
    //生产者配置实例
    private Properties properties;
    //kafka服务器   10.112.4.78:9092,10.112.4.78:9093,10.112.4.78:9094
    private String bootstrapServers;
    //设置为True才能发消息
    private Boolean sendEnable = false;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
    public void setSendEnable(Boolean sendEnable) {
        this.sendEnable = sendEnable;
    }

    public DefaultMsgProducer(){}

    /**
     * 初始化kafka生产者
     */
    public void init(){
        if(this.checkSend()){
            Properties props = this.getProps();
            ProducerConfig config = new ProducerConfig(props);
            this.kafkaProducer = new Producer<Object, Object>(config);
        } else {
            logger.warn(" The sendEnable is advised being true. If not true, any producer Will not be created!!!");
        }
    }

    /**
     * 获取默认的初始化属性
     * @return
     */
    private final Properties getProps(){
        Properties props = new Properties();
        props.put("metadata.broker.list", this.bootstrapServers);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
        props.put("request.required.acks", "1");
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
                if(key != null && this.properties.get(key) != null){
                    props.put(key,this.properties.get(key));
                }
            }
        }
    }

    @Override
    public boolean send(String topic, Object key, Object partKey, Object msg) {
        boolean status = false;
        if(this.checkSend()){
            try {
                logger.info("PRODUCER TOPIC: " + topic + ",MSG: " + msg.toString());
                KeyedMessage<Object,Object> keyedMessage = new KeyedMessage<Object, Object>(topic,key,partKey,msg);
                this.kafkaProducer.send(keyedMessage);
                status = true;
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return status;
    }

    @Override
    public boolean send(String topic,Object key, Object msg) {
        boolean status = false;
        if(this.checkSend()){
            try {
                logger.info("PRODUCER TOPIC: " + topic + ",MSG: " + msg.toString());
                KeyedMessage<Object,Object> keyedMessage = new KeyedMessage<Object, Object>(topic,key,msg);
                this.kafkaProducer.send(keyedMessage);
                status = true;
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return status;
    }

    @Override
    public boolean send(String topic, Object msg) {
        boolean status = false;
        if(this.checkSend()){
            try {
                logger.info("PRODUCER TOPIC: " + topic + ",MSG: " + msg.toString());
                KeyedMessage<Object,Object> keyedMessage = new KeyedMessage<Object, Object>(topic,msg);
                this.kafkaProducer.send(keyedMessage);
                status = true;
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return status;
    }

    @Override
    public void close() {
        if(this.kafkaProducer != null){
            this.kafkaProducer.close();
        }
    }

    /**
     * 是否可以发消息, 只有sendFlag为true的情况下才会发消息
     * @return true： 可以发消息  false： 不能发消息
     */
    private final boolean checkSend(){
        return sendEnable != null && sendEnable;
    }

}
