package net.galvin.ops.log.utils.kafka.consumer;

import net.galvin.ops.log.utils.ExceptionFormatUtil;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组模式消费者
 */
public class DefaultMsgConsumer implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(DefaultMsgConsumer.class);

    private KafkaStream<byte[],byte[]> kafkaStream;
    private MsgProcessor msgProcessor;

    private String topic;
    private String groupId;

    public DefaultMsgConsumer(KafkaStream<byte[],byte[]> kafkaStream,MsgProcessor msgProcessor,
                              String topic, String groupId){
        if(kafkaStream == null && msgProcessor == null){
            logger.error(" The kafkaStream or msghandlerList in Constructor's parameters can not be empty !!!");
            return;
        }
        this.kafkaStream = kafkaStream;
        this.msgProcessor = msgProcessor;
        this.topic = topic;
        this.groupId = groupId;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
        while (iterator.hasNext()){
            byte[] msgBytes = iterator.next().message();
            String message = null;
            try {
                message = new String(msgBytes,"utf-8");
                msgProcessor.execu(message,topic,groupId);
            } catch (Exception e) {
                logger.info("Msg Process Failed, ====>> " + message);
                logger.error(ExceptionFormatUtil.getTrace(e));
            }
        }
    }

}
