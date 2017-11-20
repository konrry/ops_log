package net.galvin.ops.log.client;

import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.bo.ResultHandle;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.comm.utils.Pagination;


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
