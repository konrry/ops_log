package net.galvin.ops.log.service.impl;

import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.utils.ExceptionFormatUtil;
import net.galvin.ops.log.comm.utils.Pagination;
import net.galvin.ops.log.service.EsComLogAdapterService;
import net.galvin.ops.log.service.EsComLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志保存到es的service
 */
@Service
public class EsComLogServiceImpl implements EsComLogService {

    private final Logger logger = LoggerFactory.getLogger(EsComLogServiceImpl.class);

    @Autowired
    private EsComLogAdapterService esComLogAdapterService;

    @Override
    public Pagination<ComLog> selectByParams(ComLogPams comLogPams, Integer curPage, Integer pageSize) {
        Pagination<ComLog> comLogPagenation = this.esComLogAdapterService.selectByParams(comLogPams,curPage,pageSize);
        return comLogPagenation;
    }

    @Override
    public void insert(ComLog comLog) {
        try {
            this.esComLogAdapterService.insert(comLog);
        }catch (Exception e){
            logger.error(ExceptionFormatUtil.getTrace(e));
        }
    }

}
