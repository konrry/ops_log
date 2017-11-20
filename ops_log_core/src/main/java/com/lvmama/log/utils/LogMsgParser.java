package com.lvmama.log.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.log.bo.LogMsgVo;
import com.lvmama.log.comm.po.ComLog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 接收到的日志消息的解析过程
 */
public class LogMsgParser {

    private final Logger logger = LoggerFactory.getLogger(LogMsgParser.class);

    private static LogMsgParser logMsgParser = null;

    private LogMsgParser(){}

    public static LogMsgParser get(){
        if(logMsgParser == null){
            synchronized (LogMsgParser.class){
                logMsgParser = new LogMsgParser();
            }
        }
        return logMsgParser;
    }

    /**
     * 解析字符串为Josn，然后转换为VO
     * @param msg
     * @return
     */
    public LogMsgVo parse(String msg){
        LogMsgVo logMsgVo = null;

        if(StringUtils.isEmpty(msg)){
            logger.error(" The msg is empty ... ");
            return null;
        }
        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(msg);
            //消息Id
            String msgId = (String) jsonObject.get("msgId");

            String logType = (String) jsonObject.get("logType");
            String logName = (String) jsonObject.get("logName");
            String content = (String) jsonObject.get("content");
            String memo = (String) jsonObject.get("memo");
            String objectType = (String) jsonObject.get("objectType");
            String objectId = (String) jsonObject.get("objectId");
            String parentId = (String) jsonObject.get("parentId");
            String parentType = (String) jsonObject.get("parentType");
            String operatorName = (String) jsonObject.get("operatorName");
            String createTime = (String) jsonObject.get("createTime");
            String sysName = (String) jsonObject.get("sysName");

            ComLog comLog = new ComLog();
            comLog.setMsgId(msgId);
            comLog.setLogType(logType);
            comLog.setLogName(logName);
            comLog.setContent(content);
            comLog.setMemo(memo);
            comLog.setObjectType(objectType);
            if(StringUtils.isNotEmpty(objectId) && !"null".equalsIgnoreCase(objectId)){
                comLog.setObjectId(Long.valueOf(objectId));
            }
            if(StringUtils.isNotEmpty(parentId) && !"null".equalsIgnoreCase(parentId)){
                comLog.setParentId(Long.valueOf(parentId));
            }
            comLog.setParentType(parentType);
            comLog.setContent(content);
            comLog.setOperatorName(operatorName);
            comLog.setCreateTime(DateUtils.parse(createTime));
            comLog.setReceiveTime(new Date());
            comLog.setSysName(sysName);

            logMsgVo = new LogMsgVo();
            logMsgVo.setMsgId(msgId);
            logMsgVo.setComLog(comLog);

        } catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        return logMsgVo;
    }

    /**
     * 将 ComLog 对象转换成 Json 字符串
     */
    public static String toJsonString(ComLog comLog){
        if(comLog == null){
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId", comLog.getMsgId());
        jsonObject.put("logId", comLog.getLogId());
        jsonObject.put("logType", comLog.getLogType());
        jsonObject.put("logName", comLog.getLogName());
        jsonObject.put("content", comLog.getContent());
        jsonObject.put("memo", comLog.getMemo());
        jsonObject.put("objectType", comLog.getObjectType());
        jsonObject.put("objectId", comLog.getObjectId());
        jsonObject.put("parentId", comLog.getParentId());
        jsonObject.put("parentType", comLog.getParentType());
        jsonObject.put("contentType", comLog.getContentType());
        jsonObject.put("operatorName", comLog.getOperatorName());
        jsonObject.put("createTime", comLog.getCreateTime() == null ? null : DateUtils.formate(comLog.getCreateTime()));
        jsonObject.put("receiveTime", comLog.getReceiveTime() == null ? null : DateUtils.formate(comLog.getReceiveTime()));
        jsonObject.put("logTime", comLog.getLogTime() == null ? null : DateUtils.formate(comLog.getLogTime()));
        return jsonObject.toJSONString();
    }


    public static String getDemoComLogString(long logId, long objectId, long parentId){
        ComLog comLog = new ComLog();
        comLog.setLogId(logId);
        comLog.setLogType("LOG_TYPE");
        comLog.setLogName("LOG_NAME");
        comLog.setContent("驴妈妈旅游网的操作日志");
        comLog.setMemo("MEMO");
        comLog.setObjectType("OBJECT_TYPE");
        comLog.setObjectId(objectId);
        comLog.setParentId(parentId);
        comLog.setParentType("PARENT_TYPE");
        comLog.setContentType("VARCHAR");
        comLog.setOperatorName("GALVIN");
        comLog.setCreateTime(new Date());
        comLog.setReceiveTime(new Date());
        comLog.setLogTime(new Date());
        return toJsonString(comLog);
    }

}
