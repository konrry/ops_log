package net.galvin.ops.log.dao;

import net.galvin.ops.log.comm.po.ComLog;

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
