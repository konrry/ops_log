package com.lvmama.log.job;

import com.lvmama.log.utils.ExceptionFormatUtil;
import com.lvmama.log.service.BizLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

/**
 * 任务
 */
@Service
public class ScheduledTaskSerice {

    private Logger logger = LoggerFactory.getLogger(ScheduledTaskSerice.class);

    @Autowired
    private BizLogService bizLogService;

    private int fromSize = 5;

    // Seconds Minutes Hours DayofMonth Month DayofWeek Year
    @Scheduled(cron = "0 0 5 * * ?")
    public void autoCreateTable(){
        logger.info("ScheduledTaskSerice.autoCreateTable start ...");
        try {
            this.doWork();
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
        logger.info("ScheduledTaskSerice.autoCreateTable end ...");
    }

    /**
     * 处理任务
     */
    private void doWork(){
        boolean jobEnable = false;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String enable = properties.getProperty("job.enable");
            jobEnable = Boolean.valueOf(enable);
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }

        if(!jobEnable){
            logger.info("ScheduledTaskSerice.autoCreateTable jobEnable is false ... ");
            return;
        }

        //当前月
        Calendar calendar = Calendar.getInstance();
        bizLogService.autoCreateTable(calendar.getTime());

        //未来5个月
        for(int i = 0; i < fromSize; i++){
            calendar.add(Calendar.MONTH,1);
            bizLogService.autoCreateTable(calendar.getTime());
        }
    }

}
