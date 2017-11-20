package net.galvin.ops.log.service.impl;

import net.galvin.ops.log.comm.bo.ComLogPams;
import net.galvin.ops.log.comm.po.ComLog;
import net.galvin.ops.log.service.EsComLogAdapterService;
import net.galvin.ops.log.utils.DateUtils;
import net.galvin.ops.log.comm.utils.Pagination;
import net.galvin.ops.log.utils.SysEnum;
import net.galvin.ops.log.utils.LogMsgParser;
import net.galvin.ops.log.utils.es.EsClusterClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EsComLogAdapterServiceImpl implements EsComLogAdapterService {

    private final Logger logger = LoggerFactory.getLogger(EsComLogAdapterServiceImpl.class);

    private EsClusterClient esClusterClient = EsClusterClient.get();

    @Override
    public Pagination<ComLog> selectByParams(ComLogPams comLogPams, Integer curPage, Integer pageSize){
        Pagination<ComLog> comLogPagenation = new Pagination<ComLog>(curPage,pageSize);

        //时间范围参数
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(comLogPams != null){

            //时间参数
            if(comLogPams.getStartTime() != null && comLogPams.getEndTime() != null){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("createTime")
                        .from(DateUtils.formate(comLogPams.getStartTime()))
                        .to(DateUtils.formate(comLogPams.getEndTime())));
            }

            //参数转换
            BoolQueryBuilder boolQueryBuilderParams = QueryBuilders.boolQuery();

            if(comLogPams.getParentId() != null && comLogPams.getParentId() > 0){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("parentId", comLogPams.getParentId()));
            }
            if(StringUtils.isNotEmpty(comLogPams.getParentType())){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("parentType", comLogPams.getParentType()));
            }
            if(comLogPams.getObjectId() != null && comLogPams.getObjectId() > 0){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("objectId", comLogPams.getObjectId()));
            }
            if(StringUtils.isNotEmpty(comLogPams.getObjectType())){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("objectType", comLogPams.getObjectType()));
            }
            if(StringUtils.isNotEmpty(comLogPams.getLogType())){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("logType", comLogPams.getLogType()));
            }
            if(StringUtils.isNotEmpty(comLogPams.getOperatorName())){
                boolQueryBuilderParams.must(QueryBuilders.termQuery("operatorName", comLogPams.getOperatorName()));
            }
            //operatorNameIn
            if(CollectionUtils.isNotEmpty(comLogPams.getOperatorNameIn())){
                for(String tempStr : comLogPams.getOperatorNameIn()){
                    if(StringUtils.isEmpty(tempStr))continue;
                    boolQueryBuilderParams.should(QueryBuilders.termQuery("operatorName", tempStr));
                }
            }
            //operatorNameNotIn
            if(CollectionUtils.isNotEmpty(comLogPams.getOperatorNameNotIn())){
                for(String tempStr : comLogPams.getOperatorNameNotIn()){
                    if(StringUtils.isEmpty(tempStr))continue;
                    boolQueryBuilderParams.mustNot(QueryBuilders.termQuery("operatorName", tempStr));
                }
            }
            if(boolQueryBuilderParams.hasClauses()){
                boolQueryBuilder.filter(boolQueryBuilderParams);
            }
        }

        //调用接口查询Es
        SearchResponse searchResponse = esClusterClient.
                search(SysEnum.ES_TYPE.com_log.name(),boolQueryBuilder,"createTime",SortOrder.DESC,curPage, pageSize,"lvmm_log_*");

        if(searchResponse == null){
            logger.info(" the searchResponse is null ");
            return comLogPagenation;
        }

        //设置总的数据条数
        Long totalRows = searchResponse.getHits().getTotalHits();

        //设置分页的数据
        if(totalRows != null && totalRows > 0){
            comLogPagenation.setTotalRows(totalRows.intValue());
            List<ComLog> itemList = new ArrayList<ComLog>();
            for(SearchHit searchHit : searchResponse.getHits().getHits()){
                Map<String,Object> comLogMap = searchHit.getSource();
                ComLog comLog = new ComLog();
                comLog.setMsgId((String) comLogMap.get("msgId"));
                Object logId = comLogMap.get("logId");
                comLog.setLogId(SysEnum.obj2Long(logId));
                comLog.setLogType((String) comLogMap.get("logType"));
                comLog.setLogName((String) comLogMap.get("logName"));
                comLog.setContent((String) comLogMap.get("content"));
                comLog.setMemo((String) comLogMap.get("memo"));
                comLog.setObjectType((String) comLogMap.get("objectType"));
                Object objectId = comLogMap.get("objectId");
                comLog.setObjectId(SysEnum.obj2Long(objectId));
                Object parentId = comLogMap.get("parentId");
                comLog.setParentId(SysEnum.obj2Long(parentId));
                comLog.setParentType((String) comLogMap.get("parentType"));
                comLog.setContentType((String) comLogMap.get("contentType"));
                comLog.setOperatorName((String) comLogMap.get("operatorName"));
                String createTime = (String) comLogMap.get("createTime");
                comLog.setCreateTime(StringUtils.isEmpty(createTime) ? null : DateUtils.parse(createTime));
                String receiveTime = (String) comLogMap.get("receiveTime");
                comLog.setReceiveTime(StringUtils.isEmpty(receiveTime) ? null : DateUtils.parse(receiveTime));
                String logTime = (String) comLogMap.get("logTime");
                comLog.setLogTime(StringUtils.isEmpty(logTime) ? null : DateUtils.parse(logTime));
                itemList.add(comLog);
            }
            comLogPagenation.setItemList(itemList);
        }
        return comLogPagenation;
    }

    @Override
    public void insert(ComLog comLog) {
        String indexName = SysEnum.curIndex();
        String typeName = SysEnum.ES_TYPE.com_log.name();
        String comLogJson = LogMsgParser.toJsonString(comLog);
        IndexResponse indexResponse = this.esClusterClient.index(indexName,typeName, comLogJson);
        if(indexResponse != null){
            logger.info("indexResponse:" + indexResponse.toString());
        }
    }

}
