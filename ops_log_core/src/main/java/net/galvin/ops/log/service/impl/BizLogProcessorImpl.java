package net.galvin.ops.log.service.impl;

import net.galvin.ops.log.service.BizLogService;
import net.galvin.ops.log.service.EsComLogService;
import net.galvin.ops.log.utils.IdempotentUtil;
import net.galvin.ops.log.utils.LogMsgParser;
import net.galvin.ops.log.utils.kafka.consumer.MsgProcessor;
import net.galvin.ops.log.bo.LogMsgVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bizLogProcessor")
public class BizLogProcessorImpl implements MsgProcessor {

    private final Logger logger = LoggerFactory.getLogger(BizLogProcessorImpl.class);

    @Autowired
    private BizLogService bizLogService;

    @Autowired
    private EsComLogService esComLogService;

    @Override
    public void execu(String msg, String topic, String groupId) {
        logger.info("OPS_LOG ===>> msg: " + msg);

        //消息解析
        LogMsgVo logMsgVo = this.parse(msg,topic,groupId);
        if(logMsgVo == null || StringUtils.isEmpty(logMsgVo.getMsgId())
                || logMsgVo.getComLog() == null){
            logger.info("OPS_LOG ===>> logMsg is null ... ");
            return;
        }

        //幂等性检测
        boolean idempotent = IdempotentUtil.get().check(logMsgVo.getMsgId());
        if(idempotent){
            logger.info("OPS_LOG ===>> idempotent true ... ");
            return;
        }

        //保存入库
        this.saveComlog(logMsgVo);
    }

    /**
     * 解析日志消息
     * @param msg
     * @param topic
     * @param groupId
     * @return
     */
    private final LogMsgVo parse(String msg, String topic, String groupId){
        LogMsgVo logMsgVo = LogMsgParser.get().parse(msg);
        if(logMsgVo != null){
            logMsgVo.setTopic(topic);
            logMsgVo.setGroupId(groupId);
        }
        return logMsgVo;
    }

    /**
     * 保存日志
     * @param logMsgVo
     */
    private final void saveComlog(LogMsgVo logMsgVo){
        //保存到数据库(oracle/mysql)
        this.bizLogService.save(logMsgVo.getComLog());
        //保存到ElasticSearch
        this.esComLogService.insert(logMsgVo.getComLog());
    }


}
