package net.galvin.ops.log.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志服务
 */
public class DateUtils {

    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 日志格式
     */
    public static String DEFAULT_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将字符串转换为日志
     * @param dateStr
     * @return
     */
    public static Date parse(String dateStr){
        return parse(dateStr, null);
    }

    /**
     * 将字符串转换为日志
     * @param dateStr
     * @param dateFormate
     * @return
     */
    public static Date parse(String dateStr, String dateFormate){
        Date date = null;
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        if(StringUtils.isEmpty(dateFormate)){
            dateFormate = DEFAULT_DATE_FORMATE;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormate);
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        return date;
    }

    /**
     * 格式化日志
     * @param date
     * @return
     */
    public static String formate(Date date){
        return formate(date, null);
    }

    /**
     * 格式化日志
     * @param date
     * @param formateStr
     * @return
     */
    public static String formate(Date date, String formateStr){
        if(date == null){
            return null;
        }
        if(StringUtils.isEmpty(formateStr)){
            formateStr = DEFAULT_DATE_FORMATE;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formateStr);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }



}
