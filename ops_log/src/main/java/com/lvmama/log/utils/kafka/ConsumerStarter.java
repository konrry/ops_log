package com.lvmama.log.utils.kafka;

import com.lvmama.log.utils.SysEnum;
import com.lvmama.log.utils.kafka.consumer.MsgProcessor;
import com.lvmama.log.utils.kafka.consumer.SimpleConsumerProxy;
import com.lvmama.log.utils.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产者启动类
 */
@Component
public class ConsumerStarter {

    private Logger logger = LoggerFactory.getLogger(ConsumerStarter.class);

    @Autowired
    private SysConfig sysConfig;

    @Resource(name="bizLogProcessor")
    private MsgProcessor msgProcessor;

    private List<SimpleConsumerProxy> simpleConsumerProxyList = new ArrayList<SimpleConsumerProxy>();

    @PostConstruct
    public void init(){
        logger.info(" starting Comsumer groups ... ");
        for(SysEnum.KAFKA_TOPIC tempTopic : SysEnum.KAFKA_TOPIC.values()){
            logger.info("Consumer Group start With Topic: " + tempTopic.name());
            SimpleConsumerProxy simpleConsumerProxy = new SimpleConsumerProxy();
            simpleConsumerProxy.setTopic(tempTopic.name());
            simpleConsumerProxy.setGroupId(tempTopic.name());
            simpleConsumerProxy.setReceiveEnable(sysConfig.isReceiveEnable());
            simpleConsumerProxy.setZookeeperServers(this.sysConfig.getZookeeperServers());
            simpleConsumerProxy.setMsgHandlers(this.msgProcessor);
            simpleConsumerProxy.consume();
            simpleConsumerProxyList.add(simpleConsumerProxy);
            logger.info("Consumer Group started With Topic: " + tempTopic.name());
        }
        logger.info(" Comsumer groups started ... ");
    }

    @PreDestroy
    public void dead(){
        logger.info(" Comsumer groups are overing ... ");
        for(SimpleConsumerProxy simpleConsumerProxy : simpleConsumerProxyList){
            simpleConsumerProxy.shutdown();
        }
        logger.info(" Comsumer groups are overed... ");
    }


}
