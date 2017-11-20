package net.galvin.ops.log.service.impl;

import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.utils.ExceptionFormatUtil;
import net.galvin.ops.log.pojo.ComLogContent;
import net.galvin.ops.log.dao.ComLogContentDao;
import net.galvin.ops.log.dao.ComLogDao;
import net.galvin.ops.log.service.BizLogService;
import net.galvin.ops.log.utils.SysEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BizLogServiceImpl implements BizLogService {

    private final Logger logger = LoggerFactory.getLogger(BizLogServiceImpl.class);

    @Autowired
    private ComLogDao comLogDao;

    @Autowired
    private ComLogContentDao comLogContentDao;

    @Override
    public void save(ComLog comLog) {
        try {
            this.insert(comLog);
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
    }

    /**
     * 保存数据导Mysql
     * @param comLog
     */
    private void insert(ComLog comLog){
        //保存DB时，记录一个时间
        comLog.setLogTime(new Date());
        comLog.setTableName(SysEnum.getPrimaryTable(comLog.getCreateTime()));

        boolean isClob = false;
        String content = comLog.getContent();
        if (content != null) {
            int len = content.getBytes().length;
            if (len <= 4000) {
                comLog.setContentType(ComLog.COM_LOG_CONTENT_TYPE.VARCHAR.name());
            } else {
                isClob = true;
                comLog.setContentType(ComLog.COM_LOG_CONTENT_TYPE.CLOB.name());
                comLog.setContent("");
            }
        }

        this.comLogDao.insert(comLog);
        if (isClob && comLog.getLogId() != null) {
            ComLogContent comLogContent = new ComLogContent();
            comLogContent.setTableName(SysEnum.getSlaveTable(comLog.getCreateTime()));
            comLogContent.setLogId(comLog.getLogId());
            comLogContent.setContent(content);
            comLogContent.setCreateTime(comLog.getCreateTime());
            this.comLogContentDao.insert(comLogContent);
            comLog.setContent(content);
        }
    }

    @Override
    public void autoCreateTable(Date date) {

        //为空的判断
        if(date == null){
            logger.error(" The date is null ... ");
            return;
        }

        //获取表名称
        String primaryTable = SysEnum.getPrimaryTable(date);
        String slaveTable = SysEnum.getSlaveTable(date);

        //为空的判断
        if (StringUtils.isEmpty(primaryTable) || StringUtils.isEmpty(slaveTable)){
            logger.error(" The primaryTable || slaveTable is null ... ");
            return;
        }

        logger.info("primaryTable=" + primaryTable + ", slaveTable=" + slaveTable);

        //如果存在就不创建表， 不存在就创建表
        //主表
        Map<String,Object> primaryMap = new HashMap<String,Object>();
        primaryMap.put("tableName", primaryTable);
        this.comLogDao.createTable(primaryMap);
        //字表
        Map<String,Object> slaveMap = new HashMap<String,Object>();
        slaveMap.put("tableName", slaveTable);
        this.comLogContentDao.createTable(slaveMap);
    }

}
