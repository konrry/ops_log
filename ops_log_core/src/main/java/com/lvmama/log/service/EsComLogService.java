package com.lvmama.log.service;

import com.lvmama.log.comm.bo.ComLogPams;
import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.comm.utils.Pagination;

import java.util.Map;

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
