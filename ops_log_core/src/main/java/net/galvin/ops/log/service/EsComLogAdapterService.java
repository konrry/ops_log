package net.galvin.ops.log.service;

import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.comm.utils.Pagination;

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
