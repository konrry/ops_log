package net.galvin.ops.log.dao;

import net.galvin.ops.log.pojo.ComLogContent;

import java.util.Map;

/**
 * 日志BLOB表
 */
public interface ComLogContentDao {

    /**
     * 保存日志内容
     * @param comLogContent
     */
    void insert(ComLogContent comLogContent);

    /**
     * 创建表
     * @param param
     */
    void createTable(Map<String,Object> param);

}
