package com.lvmama.log.action;

import com.lvmama.log.comm.bo.ComLogPams;
import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.service.EsComLogService;
import com.lvmama.log.utils.DateUtils;
import com.lvmama.log.comm.utils.Pagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

/**
 * 日志查询Action
 */
@Controller
@RequestMapping("/bizLog")
public class BizLogAction {

    private final Integer curPage = 1;
    private final Integer pageSize = 10;

    @Autowired
    private EsComLogService esComLogService;

    /**
     * 提供分页json接口
     */
    @RequestMapping(value = "/findJson")
    @ResponseBody
    public Pagination<ComLog> findJson(@RequestParam(value="sysName", defaultValue = "") String sysName,
                                       @RequestParam(value="parentId", defaultValue = "") Long parentId,
                                       @RequestParam(value="parentType", defaultValue = "") String parentType,
                                       @RequestParam(value="objectId", defaultValue = "") Long objectId,
                                       @RequestParam(value="objectType", defaultValue = "") String objectType,
                                       @RequestParam(value="logType", defaultValue = "") String logType,
                                       @RequestParam(value="operatorName", defaultValue = "") String operatorName,
                                       @RequestParam(value="startTime", defaultValue = "") String startTime,
                                       @RequestParam(value="endTime", defaultValue = "") String endTime,
                                       @RequestParam(value="curPage", defaultValue="1") Integer curPage,
                                       @RequestParam(value="pageSize", defaultValue="10") Integer pageSize){
        curPage = (curPage == null || curPage <= 0) ? this.curPage : curPage;
        pageSize = (pageSize == null || pageSize <= 0) ? this.pageSize : pageSize;
        ComLogPams comLogPams = new ComLogPams();
        comLogPams.setParentId(parentId);
        comLogPams.setParentType(parentType);
        comLogPams.setObjectId(objectId);
        comLogPams.setObjectType(objectType);
        comLogPams.setOperatorName(operatorName);
        comLogPams.setStartTime(StringUtils.isEmpty(startTime) ? null : DateUtils.parse(startTime));
        comLogPams.setEndTime(StringUtils.isEmpty(endTime) ? null : DateUtils.parse(endTime));
        Pagination<ComLog> bizLogPage = this.esComLogService.selectByParams(comLogPams,curPage, pageSize);
        return bizLogPage;
    }

    /**
     * 提供分页页面接口
     */
    @RequestMapping(value = "/find")
    public ModelAndView find(
            @RequestParam(value="sysName", defaultValue = "") String sysName,
            @RequestParam(value="parentId", defaultValue = "") Long parentId,
            @RequestParam(value="parentType", defaultValue = "") String parentType,
            @RequestParam(value="objectId", defaultValue = "") Long objectId,
            @RequestParam(value="objectType", defaultValue = "") String objectType,
            @RequestParam(value="operatorName", defaultValue = "") String operatorName,
            @RequestParam(value="startTime", defaultValue = "") String startTime,
            @RequestParam(value="endTime", defaultValue = "") String endTime,
            @RequestParam(value="curPage", defaultValue="1") Integer curPage,
            @RequestParam(value="pageSize", defaultValue="10") Integer pageSize){
        final Map<String,Object> params = new HashMap<String,Object>();
        curPage = (curPage == null || curPage <= 0) ? this.curPage : curPage;
        pageSize = (pageSize == null || pageSize <= 0) ? this.pageSize : pageSize;
        ComLogPams comLogPams = new ComLogPams();
        comLogPams.setParentId(parentId);
        comLogPams.setParentType(parentType);
        comLogPams.setObjectId(objectId);
        comLogPams.setObjectType(objectType);
        comLogPams.setOperatorName(operatorName);
        comLogPams.setStartTime(StringUtils.isEmpty(startTime) ? null : DateUtils.parse(startTime));
        comLogPams.setEndTime(StringUtils.isEmpty(endTime) ? null : DateUtils.parse(endTime));
        Pagination<ComLog> bizLogPage = this.esComLogService.selectByParams(comLogPams,curPage, pageSize);

        params.put("bizLogPage", bizLogPage);
        params.put("sysName", sysName);
        params.put("parentId", parentId);
        params.put("parentType", parentType);
        params.put("objectId", objectId);
        params.put("objectType", objectType);
        params.put("operatorName", operatorName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("curPage", curPage);
        params.put("pageSize", pageSize);
        return new ModelAndView("pages/pub/bizlog/biz_log_page", params);
    }

    /**
     * 提供分页页面接口
     */
    @RequestMapping(value = "/showVersatileLogList")
    public ModelAndView showVersatileLogList(
            @RequestParam(value="sysName", defaultValue = "") String sysName,
            @RequestParam(value="parentId", defaultValue = "") Long parentId,
            @RequestParam(value="parentType", defaultValue = "") String parentType,
            @RequestParam(value="objectId", defaultValue = "") Long objectId,
            @RequestParam(value="objectType", defaultValue = "") String objectType,
            @RequestParam(value="logType", defaultValue = "") String logType,
            @RequestParam(value="operatorName", defaultValue = "") String operatorName,
            @RequestParam(value="startTime", defaultValue = "") String startTime,
            @RequestParam(value="endTime", defaultValue = "") String endTime,
            @RequestParam(value="curPage", defaultValue="1") Integer curPage,
            @RequestParam(value="pageSize", defaultValue="10") Integer pageSize){
        final Map<String,Object> params = new HashMap<String,Object>();
        curPage = (curPage == null || curPage <= 0) ? this.curPage : curPage;
        pageSize = (pageSize == null || pageSize <= 0) ? this.pageSize : pageSize;
        ComLogPams comLogPams = new ComLogPams();
        comLogPams.setParentId(parentId);
        comLogPams.setParentType(parentType);
        comLogPams.setObjectId(objectId);
        comLogPams.setObjectType(objectType);
        comLogPams.setOperatorName(operatorName);
        comLogPams.setStartTime(StringUtils.isEmpty(startTime) ? null : DateUtils.parse(startTime));
        comLogPams.setEndTime(StringUtils.isEmpty(endTime) ? null : DateUtils.parse(endTime));
        Pagination<ComLog> bizLogPage = this.esComLogService.selectByParams(comLogPams,curPage, pageSize);

        params.put("bizLogPage", bizLogPage);
        params.put("sysName", sysName);
        params.put("parentId", parentId);
        params.put("parentType", parentType);
        params.put("objectId", objectId);
        params.put("objectType", objectType);
        params.put("logType", logType);
        params.put("operatorName", operatorName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("curPage", curPage);
        params.put("pageSize", pageSize);
        return new ModelAndView("pages/pub/bizlog/showVersatileLogList",params);
    }

    /**
     * 查询ebk订单的日志
     */
    @RequestMapping(value = "/findLogList")
    public ModelAndView findLogList(@RequestParam(value="sysName", defaultValue = "") String sysName,
                                    @RequestParam(value="parentId", defaultValue = "") Long parentId,
                                    @RequestParam(value="parentType", defaultValue = "") String parentType,
                                    @RequestParam(value="objectId", defaultValue = "") Long objectId,
                                    @RequestParam(value="objectType", defaultValue = "") String objectType,
                                    @RequestParam(value="logType", defaultValue = "") String logType,
                                    @RequestParam(value="operatorName", defaultValue = "") String operatorName,
                                    @RequestParam(value="startTime", defaultValue = "") String startTime,
                                    @RequestParam(value="endTime", defaultValue = "") String endTime,
                                    @RequestParam(value="curPage", defaultValue="1") Integer curPage,
                                    @RequestParam(value="pageSize", defaultValue="10") Integer pageSize,
                                    @RequestParam(value="parentIdHidden", defaultValue="") String parentIdHidden) {
        final Map<String, Object> params = new HashMap<String, Object>();
        curPage = (curPage == null || curPage <= 0) ? this.curPage : curPage;
        pageSize = (pageSize == null || pageSize <= 0) ? this.pageSize : pageSize;
        ComLogPams comLogPams = new ComLogPams();
        comLogPams.setParentId(parentId);
        comLogPams.setParentType(parentType);
        comLogPams.setObjectId(objectId);
        comLogPams.setObjectType(objectType);
        comLogPams.setOperatorName(operatorName);
        comLogPams.setStartTime(StringUtils.isEmpty(startTime) ? null : DateUtils.parse(startTime));
        comLogPams.setEndTime(StringUtils.isEmpty(endTime) ? null : DateUtils.parse(endTime));
        Pagination<ComLog> bizLogPage = this.esComLogService.selectByParams(comLogPams,curPage, pageSize);

        params.put("bizLogPage", bizLogPage);
        params.put("sysName", sysName);
        params.put("parentId", parentId);
        params.put("parentType", parentType);
        params.put("objectId", objectId);
        params.put("objectType", objectType);
        params.put("logType", logType);
        params.put("operatorName", operatorName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("curPage", curPage);
        params.put("pageSize", pageSize);
        if ("Y".equals(parentIdHidden)) {
            return  new ModelAndView("pages/pub/bizlog/viewlogNoParentId",params);
        }
        return new ModelAndView("pages/pub/bizlog/viewlog",params);
    }

}
