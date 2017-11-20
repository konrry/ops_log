package net.galvin.ops.log.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class SysEnum {

    private final static Logger logger = LoggerFactory.getLogger(SysEnum.class);

    final private static String PRIMARY_TABLE_PREFIX = "COM_LOG_";
    final private static String SLAVE_TABLE_PREFIX = "COM_LOG_CONTENT_";

    /**
     * ES 中 index的前缀
     */
    final private static String INDEX_PREFIX = "lvmm_log_";

    /**
     * 消息 topic 的配置
     */
    public enum KAFKA_TOPIC {

        VST_LOG("VST日志"), VST_LOG_ORDER("VST订单日志");

        private String remark;

        KAFKA_TOPIC(String remark) {
            this.remark = remark;
        }
    }

    /**
     * 缓存的Key
     */
    public enum KEY{

        LOG_MSG_IDEMPOTENT_(60 * 60 * 24 * 3);

        KEY(int s){
            seconds = s;
        }
        /**
         * 缓存的秒数
         */
        private int seconds;

        public int getSeconds(){
            return this.seconds;
        }
    }

    /**
     * ES 中的TYPE
     */
    public enum ES_TYPE{

        com_log("日志表"),msg_transport("消息传输日志表");

        private String cName;

        ES_TYPE(String cName) {
            this.cName = cName;
        }
        public String getcName() {
            return cName;
        }
        public void setcName(String cName) {
            this.cName = cName;
        }
    }

    /**
     * ES 中的INDEX
     */
    public enum ES_INDEX{
        lvmm_log_2014(),
        lvmm_log_2015(),
        lvmm_log_2016(),
        lvmm_log_2017();
    }

    /**
     * 获取当前的Index
     * @return
     */
    public  static String curIndex(){
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        return INDEX_PREFIX + nowYear;
    }

    /**
     * 获取表
     * @return
     */
    public  static String getPrimaryTable(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        return PRIMARY_TABLE_PREFIX + nowYear + (nowMonth>9? nowMonth : "0"+nowMonth);
    }

    /**
     * 获取表
     * @return
     */
    public  static String getSlaveTable(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        return SLAVE_TABLE_PREFIX + nowYear + (nowMonth>9? nowMonth : "0"+nowMonth);
    }

    /**
     * 字符串转换成Integer
     * @param strVal
     * @return
     */
    public static Integer str2Integer(String strVal){
        Integer intVal = null;
        if(StringUtils.isEmpty(strVal)){
            return intVal;
        }
        try {
            intVal = Integer.valueOf(strVal);
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        return intVal;
    }

    /**
     * Object转换成Long
     */
    public static Long obj2Long(Object objVal){
        Long longVal = null;
        if(objVal == null){
            return longVal;
        }
        try {
            longVal = Long.valueOf(String.valueOf(objVal));
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        return longVal;
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        System.out.println(PRIMARY_TABLE_PREFIX + nowYear + (nowMonth>9? nowMonth : "0"+nowMonth));

    }

}
