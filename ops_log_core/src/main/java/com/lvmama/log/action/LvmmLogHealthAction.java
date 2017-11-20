package com.lvmama.log.action;

import com.lvmama.log.comm.bo.ComLogPams;
import com.lvmama.log.comm.po.ComLog;
import com.lvmama.log.service.EsComLogService;
import com.lvmama.log.comm.utils.Pagination;
import com.lvmama.log.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Es数据查询Action
 */
@Controller
@RequestMapping("/lvmmLogHealth")
public class LvmmLogHealthAction {

    private final Integer curPage = 1;
    private final Integer pageSize = 1000;

    @Autowired
    private EsComLogService esComLogService;

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
            @RequestParam(value="operatorNameIn", defaultValue = "") String operatorNameIn,
            @RequestParam(value="operatorNameNotIn", defaultValue = "") String operatorNameNotIn,
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
        if(StringUtils.isNotEmpty(operatorNameIn)){
            String[] operatorNameInArr = operatorNameIn.split(",");
            List<String> operatorNameInList = Arrays.asList(operatorNameInArr);
            comLogPams.setOperatorNameIn(operatorNameInList);
        }
        if(StringUtils.isNotEmpty(operatorNameNotIn)){
            String[] operatorNameNotInArr = operatorNameNotIn.split(",");
            List<String> operatorNameNotInList = Arrays.asList(operatorNameNotInArr);
            comLogPams.setOperatorNameNotIn(operatorNameNotInList);
        }
        Pagination<ComLog> bizLogPage = this.esComLogService.selectByParams(comLogPams,curPage, pageSize);

        params.put("bizLogPage", bizLogPage);
        params.put("sysName", sysName);
        params.put("parentId", parentId);
        params.put("parentType", parentType);
        params.put("objectId", objectId);
        params.put("objectType", objectType);
        params.put("operatorName", operatorName);
        params.put("operatorNameIn", operatorNameIn);
        params.put("operatorNameNotIn", operatorNameNotIn);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("curPage", curPage);
        params.put("pageSize", pageSize);
        return new ModelAndView("pages/pub/bizlog/es_biz_log_page", params);
    }





}
