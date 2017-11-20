package net.galvin.ops.log.client.impl;

import net.galvin.ops.log.client.QueryLogClientService;
import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.bo.ResultHandle;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.comm.utils.Pagination;
import net.galvin.ops.log.service.EsComLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志服务对外提供客户端实现
 */
@Service(value = "queryLogClientService")
public class QueryLogClientServiceImpl implements QueryLogClientService {

    private final Logger logger = LoggerFactory.getLogger(QueryLogClientServiceImpl.class);

    @Autowired
    private EsComLogService esComLogService;

    @Override
    public ResultHandle<Pagination<ComLog>> findLog(ComLogPams comLogPams, Integer curPage, Integer pageSize) {

        //返回对象
        ResultHandle<Pagination<ComLog>> resultHandle = new ResultHandle<Pagination<ComLog>>();

        //参数判断
        if(comLogPams == null){
            logger.info(" The queryParams can not empty ... ");
            resultHandle.setMsg("The queryParams can not empty ... ");
            return resultHandle;
        }
        logger.info("comLogPams ===>> " + comLogPams);

        Pagination<ComLog>  comLogPagination = esComLogService.selectByParams(comLogPams,curPage, pageSize);
        resultHandle.setT(comLogPagination);
        return resultHandle;
    }

}
