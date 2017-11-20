package net.galvin.ops.log.service;

import net.galvin.ops.log.comm.po.ComLog;

import java.util.Date;

public interface BizLogService {

    /**
     * 保存日志
     * @param comLog
     * @return
     */
    void save(ComLog comLog);

    /**
     * 根据日期创建表
     * @param date
     */
    void autoCreateTable(Date date);

}
