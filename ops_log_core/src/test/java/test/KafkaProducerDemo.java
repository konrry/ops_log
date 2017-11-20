package test;

import com.alibaba.fastjson.JSON;
import net.galvin.ops.log.utils.DateUtils;
import net.galvin.ops.log.utils.kafka.producer.DefaultMsgProducer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/9.
 */
@Component
public class KafkaProducerDemo {

    private List<Long> failureCount = new ArrayList<>();
    private static int count = 10;

    public void start() {

        for(int i=0; i<1; i++){
            System.out.println("第个 "+ String.valueOf(i) +" 线程");
            Thread thread = new Thread(new Producer());
            thread.start();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    class Producer implements Runnable {

        @Override
        public void run() {
            DefaultMsgProducer defaultMsgProducer = new DefaultMsgProducer();
            defaultMsgProducer.setBootstrapServers("10.112.4.177:7090,10.112.4.177:7091,10.112.4.177:7092");
//            defaultMsgProducer.setBootstrapServers("10.200.5.124:9092");
            defaultMsgProducer.setSendEnable(true);
            defaultMsgProducer.init();
            int i = 0;
            while(i < count){

                Map<String,String> logMsg = new HashMap<>();
                logMsg.put("msgId", UUID.randomUUID().toString());
                logMsg.put("logType", "logTypeTest");
                logMsg.put("logName", "logName");
                StringBuilder content = new StringBuilder();
                for(int l=0;l<1;l++){
                    content.append("驴妈妈旅游网测试日志");
                    System.out.println(l);
                }

                System.out.println(content.toString().getBytes().length);
                logMsg.put("content", content.toString());
                logMsg.put("memo", "memo");
                logMsg.put("objectType", "ORD_ORDER_ORDER");
                logMsg.put("objectId", "20059681");
                logMsg.put("parentId", "20059681");
                logMsg.put("parentType", "ORD_ORDER");
                logMsg.put("operatorName", "fy001");
                logMsg.put("createTime", DateUtils.formate(new Date()));
                logMsg.put("sysName", "O2O");
                String msg = JSON.toJSONString(logMsg);
                boolean status = defaultMsgProducer.send("VST_LOG_ORDER", msg);
                if(!status){
                    failureCount.add(1l);
                }
                i++;
            }
            System.out.println( "发送失败的条数：" + failureCount.size());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        KafkaProducerDemo kafkaProducerDemo = new KafkaProducerDemo();
        kafkaProducerDemo.start();
    }

}
