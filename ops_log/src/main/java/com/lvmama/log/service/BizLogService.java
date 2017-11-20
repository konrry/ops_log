package com.lvmama.log.service;

import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.comm.utils.Pagination;

import java.util.Date;
import java.util.Map;

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
