package net.galvin.ops.log.service;

import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.comm.utils.Pagination;

/**
 * 日志保存到es的service
 */
public interface EsComLogService {

    /**
     * 从es中分页查询
     * @param queryParams
     * @param curPage
     * @param pageSize
     * @return
     */
    Pagination<ComLog> selectByParams(ComLogPams comLogPams, Integer curPage, Integer pageSize);

    /**
     * index日志到es
     * @param comLog
     */
    void insert(ComLog comLog);

}
