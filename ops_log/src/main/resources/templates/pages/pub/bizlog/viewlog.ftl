<!DOCTYPE html>
<html>
<head>
    <title>日志查询</title>
    <meta Access-Control-Allow-Origin="*" />
<#include "/pages/base/head.ftl"/>
</head>
<body>
<div class="iframe-content">
<#if bizLogPage.itemList?? && bizLogPage.itemList?size &gt; 0>
    <div>
        <table style="width:100%" class="log_table table_center">
            <tr>
                <th style="width:50px;text-align: center;"></th>
                <th style="width:10%;" nowrap="nowrap">产品编号</th>
                <th style="width:10%;" nowrap="nowrap">操作人</th>
                <th style="width:15%;" nowrap="nowrap">操作时间</th>
                <th style="width:20%;" nowrap="nowrap">操作类型</th>
                <th style="width:41%;" nowrap="nowrap">操作内容</th>
            </tr>
            <#list bizLogPage.itemList as log>
                <tr>
                    <td>
                        ${log_index + 1}
                        <#if (log.logId)?? >
                            <a style="display: none;">${(log.logId)?c}</a>
                        </#if>
                    </td>
                    <td><#if (log.parentId)??>${(log.parentId)?c}</#if></td>
                    <td>${log.operatorName!''}</td>
                    <td>${log.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${(log.memo)!''}</td>
                    <td>
                        <#if (log.content)??>
                            ${log.content!}
                        </#if>
                    </td>
                </tr>
            </#list>
        </table>
        <#if bizLogPage.itemList?exists>
            <div class="paging" >
                <div id="pages_" class="pages">
                    <div id="lv_page">
                        <div class="Pages" name="pagesItem"></div>
                    </div>
                </div>
            </div>
        </#if>
    </div>
<#else>
    <div class="no_data mt20"><i class="icon-warn32"></i>暂无操作日志  ！</div>
</#if>
    <div style="display: none;">
        <input type="hidden" name="sysName" value="${sysName!''}" >
        <input type="hidden" name="parentId" <#if parentId?? >value="${parentId?c}"</#if> >
        <input type="hidden" name="parentType" value="${parentType!''}" >
        <input type="hidden" name="objectId" <#if objectId?? >value="${objectId?c}"</#if> >
        <input type="hidden" name="objectType" value="${objectType!''}" >
        <input type="hidden" name="operatorName" value="${operatorName!''}" >
        <input type="hidden" name="startTime" value="${startTime!''}" >
        <input type="hidden" name="endTime" value="${endTime!''}" >
    </div>
</div>
</body>
<#include "/pages/base/foot.ftl"/>
</html>

<script type="text/javascript">
    $(function () {

        $.lvmm_log.init({
            target: $('div[name="pagesItem"]'),
            curPage: ${curPage?c},
            pageSize: ${pageSize?c},
            totalNums: ${(bizLogPage.totalRows)?c},
            url: '/lvmm_log/bizLog/findLogList',
            queryPams: [
                { name: "sysName", value: $("input[name='sysName']").val() },
                { name: "parentId", value: $("input[name='parentId']").val() },
                { name: "parentType", value: $("input[name='parentType']").val() },
                { name: "objectId", value: $("input[name='objectId']").val() },
                { name: "objectType", value: $("input[name='objectType']").val() },
                { name: "operatorName", value: $("input[name='operatorName']").val() },
                { name: "startTime", value: $("input[name='startTime']").val() },
                { name: "endTime", value: $("input[name='endTime']").val() }
            ]
        });

    });
</script>

