package com.lvmama.log.client;

import com.lvmama.log.comm.bo.ComLogPams;
import com.lvmama.log.comm.bo.ResultHandle;
import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.comm.utils.Pagination;


/**
 * 日志服务对外提供的接口
 */
public interface QueryLogClientService {

    /**
     * 分页查询日志
     * @param comLogPams
     * @param curPage
     * @param pageSize
     * @return
     */
    ResultHandle<Pagination<ComLog>> findLog(ComLogPams comLogPams, Integer curPage, Integer pageSize);

}
