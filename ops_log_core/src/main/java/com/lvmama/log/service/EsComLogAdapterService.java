package com.lvmama.log.service;

import com.lvmama.log.comm.bo.ComLogPams;
import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.comm.utils.Pagination;

/**
 * 将日志保存到 ElasticSearch
 */
public interface EsComLogAdapterService {

    /**
     * 可选参数的查询日志信息
     * @return
     */
    Pagination<ComLog> selectByParams(ComLogPams comLogPams, Integer curPage, Integer pageSize);

    /**
     * 保存日志
     * @param comLog
     * @return
     */
    void insert(ComLog comLog);

}
