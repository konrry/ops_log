package com.lvmama.log.dao;

import com.lvmama.log.comm.po.ComLog;

import java.util.List;
import java.util.Map;

/**
 * 日志主表Dao
 */
public interface ComLogDao{

    /**
     * 保存日志
     * @param comLog
     * @return
     */
    Long insert(ComLog comLog);

    /**
     * 创建表
     * @param param
     */
    void createTable(Map<String,Object> param);

}
