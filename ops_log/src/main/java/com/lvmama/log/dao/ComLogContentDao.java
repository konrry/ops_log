package com.lvmama.log.dao;

import com.lvmama.log.pojo.ComLogContent;

import java.util.List;
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
