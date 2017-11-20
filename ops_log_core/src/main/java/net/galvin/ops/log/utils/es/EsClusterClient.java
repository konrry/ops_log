package net.galvin.ops.log.utils.es;

import com.alibaba.fastjson.JSONObject;
import net.galvin.ops.log.utils.ExceptionFormatUtil;
import net.galvin.ops.log.utils.SysEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.get.GetIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * es简单封装
 */
public class EsClusterClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(EsClusterClient.class);

    private static EsClusterClient esClusterClient = null;
    private boolean esEnable = false;
    private TransportClient transportClient;
    private final static int BULK_SIZE = 4000;

    private String hostAndPorts;
    private Integer shardNum = 3;
    private Integer replicaNum = 2;

    /**
     * 单例模式
     * @return
     */
    public static EsClusterClient get(){
        if(esClusterClient == null){
            synchronized (EsClusterClient.class){
                if (esClusterClient == null){
                    esClusterClient = new EsClusterClient();
                }
            }
        }
        return esClusterClient;
    }

    /**
     * 私有的构造函数
     */
    private EsClusterClient(){
        this.init();
        this.checkEs();
        this.initEsIndex();
    }

    /**
     * 初始化
     */
    private void init(){
        System.out.println(" init start .... ");
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("elasticsearch.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            this.hostAndPorts = properties.getProperty("cluster.es.server");
            String tempEnable = (String) properties.get("cluster.es.enable");
            String clusterName = (String) properties.get("cluster.es.name");
            String tempNumShard = (String) properties.get("cluster.es.number.shards");
            String tempNumReplica = (String) properties.get("cluster.es.number.replicas");
            this.shardNum = SysEnum.str2Integer(tempNumShard) == null ?
                    this.shardNum : SysEnum.str2Integer(tempNumShard);
            this.replicaNum = SysEnum.str2Integer(tempNumReplica) == null ?
                    this.replicaNum : SysEnum.str2Integer(tempNumReplica);

            if(!"true".equalsIgnoreCase(tempEnable)
                    && !"ok".equalsIgnoreCase(tempEnable)
                    && !"y".equalsIgnoreCase(tempEnable)){
                LOGGER.error(" The cluster.es.enable is not enable ... ");
                return;
            }
            if(StringUtils.isEmpty(clusterName)){
                LOGGER.error(" The cluster.es.name is not enable ... ");
                return;
            }

            Settings settings = Settings.settingsBuilder()
                    .put("client.transport.sniff", true)
                    .put("cluster.name", clusterName).build();
            transportClient = TransportClient.builder().settings(settings).build();
            List<InetSocketTransportAddress> transportAddressList = this.getTransportAddresses();
            if(CollectionUtils.isEmpty(transportAddressList)){
                LOGGER.error(" transportAddressList is null ... ");
                return;
            }
            for(InetSocketTransportAddress transportAddress : transportAddressList){
                transportClient.addTransportAddress(transportAddress);
            }
        } catch (Exception e) {
            LOGGER.error(" init failed ... ");
            LOGGER.error(ExceptionFormatUtil.getTrace(e));
        }
        System.out.println(" init end .... ");
    }

    /**
     * 拼接端口
     * @return
     */
    private List<InetSocketTransportAddress> getTransportAddresses(){
        List<InetSocketTransportAddress> transportAddressList = new ArrayList<InetSocketTransportAddress>();
        if(StringUtils.isEmpty(this.hostAndPorts)){
            LOGGER.error(" The hostAndPorts must not be empty ... ");
            return transportAddressList;
        }
        String[] hostAndPortArr = this.hostAndPorts.split(",");
        if(hostAndPortArr != null && hostAndPortArr.length > 0){
            for(String hostAndPort : hostAndPortArr){
                if(StringUtils.isEmpty(hostAndPort)){
                    continue;
                }
                String[] hostAndPortPair = hostAndPort.split(":");
                if(hostAndPortPair == null || hostAndPortPair.length != 2){
                    continue;
                }
                String host = hostAndPortPair[0];
                Integer port = Integer.valueOf(hostAndPortPair[1]);
                if(StringUtils.isEmpty(host) || port == null || port <= 0){
                    continue;
                }
                try {
                    transportAddressList.add(new InetSocketTransportAddress(InetAddress.getByName(host), port));
                } catch (UnknownHostException e) {
                    LOGGER.error(ExceptionFormatUtil.getTrace(e));
                }
            }
        }
        return transportAddressList;
    }

    /**
     * 检查es客户端是否初始化成功,初始化一些参数.
     *
     */
    private void checkEs(){
        try {
            List<DiscoveryNode> discoveryNodeList = this.transportClient.listedNodes();
            if(CollectionUtils.isEmpty(discoveryNodeList)){
                LOGGER.error(" no Node discovered ... ");
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for(DiscoveryNode discoveryNode : discoveryNodeList){
                stringBuilder.append("{ nodeId: " + discoveryNode.getId());
                stringBuilder.append(", nodeName: " + discoveryNode.getName());
                stringBuilder.append(", hostName: " + discoveryNode.getHostName());
                stringBuilder.append(", hostAddress: " + discoveryNode.getHostAddress());
                stringBuilder.append(", version: " + discoveryNode.getVersion());
                stringBuilder.append(" }      ");
            }
            LOGGER.info("============  checkEs info start   ============");
            LOGGER.info(" discoveryNodeList ===> : " + stringBuilder.toString());
            LOGGER.info("============  checkEs info end   ============");
            this.esEnable = true;
        }catch (Exception e){
            LOGGER.error(ExceptionFormatUtil.getTrace(e));
        }
    }

    /**
     * 销毁对象
     */
    public void distroy(){
        if(this.transportClient != null){
            this.transportClient.close();
            this.transportClient = null;
        }
        System.out.println(" distroy .... ");
    }

    /**
     * 初始化Es中的  index  setting mapping
     */
    private void initEsIndex(){
        IndicesAdminClient indicesAdminClient = transportClient.admin().indices();

        /** 检查index是否存在 */
        GetIndexRequestBuilder getIndexRequestBuilder = indicesAdminClient.prepareGetIndex();
        GetIndexResponse getIndexResponse = getIndexRequestBuilder.get();
        String[] indices = getIndexResponse.getIndices();
        Set<String> indexSet = new HashSet<String>();
        if(indices != null && indices.length > 0){
            for(String index : indices){
                indexSet.add(index);
            }
        }

        /** 如果不存在index，就创建一个 */
        for(SysEnum.ES_INDEX esIndex : SysEnum.ES_INDEX.values()){
            if(indexSet.contains(esIndex.name())){
                LOGGER.info(" In ES, exists index : " + esIndex.name());
                continue;
            }
            JSONObject mappingJson = new JSONObject();
            JSONObject propertiesJson = new JSONObject();
            mappingJson.put(SysEnum.ES_TYPE.com_log.name(),propertiesJson);
            JSONObject detailJson = new JSONObject();
            propertiesJson.put("properties",detailJson);

            //"msgId":{"type": "long","index": "not_analyzed"},0
            JSONObject msgIdJson = new JSONObject();
            msgIdJson.put("type","string");
            msgIdJson.put("index","not_analyzed");
            detailJson.put("msgId", msgIdJson);

            //"logId":{"type": "long"},1
            JSONObject logIdJson = new JSONObject();
            logIdJson.put("type","long");
            detailJson.put("logId", logIdJson);

            //"parentId":{"type": "long","index": "not_analyzed"},2
            JSONObject parentIdJson = new JSONObject();
            parentIdJson.put("type","long");
            parentIdJson.put("index","not_analyzed");
            detailJson.put("parentId", parentIdJson);

            //"parentType":{"type": "string","index": "not_analyzed"},3
            JSONObject parentTypeJson = new JSONObject();
            parentTypeJson.put("type","string");
            parentTypeJson.put("index","not_analyzed");
            detailJson.put("parentType", parentTypeJson);

            //"objectId":{"type": "long"},4
            JSONObject objectIdJson = new JSONObject();
            objectIdJson.put("type","long");
            detailJson.put("objectId", objectIdJson);

            //"objectType":{"type": "string","index": "not_analyzed"},5
            JSONObject objectTypeJson = new JSONObject();
            objectTypeJson.put("type","string");
            objectTypeJson.put("index","not_analyzed");
            detailJson.put("objectType", objectTypeJson);

            //"logType":{"type": "string","index": "not_analyzed"},6
            JSONObject logTypeJson = new JSONObject();
            logTypeJson.put("type","string");
            logTypeJson.put("index","not_analyzed");
            detailJson.put("logType", logTypeJson);

            //"logName":{"type": "string","index": "not_analyzed"},7
            JSONObject logNameJson = new JSONObject();
            logNameJson.put("type","string");
            logNameJson.put("index","not_analyzed");
            detailJson.put("logName", logNameJson);

            //"content":{"type": "string","index": "not_analyzed"},8
            JSONObject contentJson = new JSONObject();
            contentJson.put("type","string");
            contentJson.put("index","no");
            detailJson.put("content", contentJson);

            //"contentType":{"type": "string","index": "not_analyzed"},9
            JSONObject contentTypeJson = new JSONObject();
            contentTypeJson.put("type","string");
            contentTypeJson.put("index","not_analyzed");
            detailJson.put("contentType", contentTypeJson);

            //"memo":{"type": "string","index": "not_analyzed"},10
            JSONObject memoJson = new JSONObject();
            memoJson.put("type","string");
            memoJson.put("index","not_analyzed");
            detailJson.put("memo", memoJson);

            //"operatorName":{"type": "string","index": "not_analyzed"},11
            JSONObject operatorNameJson = new JSONObject();
            operatorNameJson.put("type","string");
            operatorNameJson.put("index","not_analyzed");
            detailJson.put("operatorName", operatorNameJson);

            //"createTime":{"type": "date","format": "yyyy-MM-dd HH:mm:ss"},12
            JSONObject createTimeJson = new JSONObject();
            createTimeJson.put("type","date");
            createTimeJson.put("format","yyyy-MM-dd HH:mm:ss");
            detailJson.put("createTime", createTimeJson);

            //"receiveTime":{"type": "date","format": "yyyy-MM-dd HH:mm:ss"},13
            JSONObject receiveTimeJson = new JSONObject();
            receiveTimeJson.put("type","date");
            receiveTimeJson.put("format","yyyy-MM-dd HH:mm:ss");
            detailJson.put("receiveTime", receiveTimeJson);

            //"logTime":{"type": "date","format": "yyyy-MM-dd HH:mm:ss"}}}}}'14
            JSONObject logTimeJson = new JSONObject();
            logTimeJson.put("type","date");
            logTimeJson.put("format","yyyy-MM-dd HH:mm:ss");
            detailJson.put("logTime", logTimeJson);

            String mapping = mappingJson.toJSONString();
            indicesAdminClient.prepareCreate(esIndex.name())
                    /**  3个主shard  和 1个复制  */
                    .setSettings(Settings.builder()
                                            .put("index.number_of_replicas", this.replicaNum)
                                            .put("number_of_shards", this.shardNum))
                    .addMapping(SysEnum.ES_TYPE.com_log.name(),mapping).get();
        }

    }

    /**
     * index一个文档到 ES 中
     */
    public IndexResponse index(String indexName, String typeName, String document) {
        if(!this.esEnable){
            LOGGER.error(" The esEnable is false ... ");
            return null;
        }
        if(StringUtils.isEmpty(indexName) || StringUtils.isEmpty(typeName) || StringUtils.isEmpty(document)){
            LOGGER.error(" The indexName | typeName | document is empty ... ");
            return null;
        }
        IndexResponse indexResponse = transportClient.prepareIndex(indexName, typeName).setSource(document).get();
        return indexResponse;
    }

    /**
     * 批量插入数据
     * @param indexName
     * @param typeName
     * @param documentList
     * @return
     */
    public void bulkOne(String indexName, String typeName, List<String> documentList){
        if(!this.esEnable){
            LOGGER.error(" The esEnable is false ... ");
            return;
        }
        if(StringUtils.isEmpty(indexName) || StringUtils.isEmpty(typeName) || CollectionUtils.isEmpty(documentList)){
            LOGGER.error(" The indexName | typeName | documentList is empty ... ");
            return;
        }

        //计算
        int factDocSize = documentList.size();
        int modules = factDocSize / BULK_SIZE;
        int reminder = factDocSize % BULK_SIZE;
        if(reminder > 0){
            modules += 1;
        }

        //拆分成多个小的 list
        List<List<String>> docList = new ArrayList<List<String>>();
        for(int i=0; i<modules; i++){
            int start = i * BULK_SIZE;
            int end =  (i+1) * BULK_SIZE > factDocSize ? factDocSize : (i+1) * BULK_SIZE;
            docList.add(documentList.subList(start,end));
        }

        //通过循环， 批量插入
        LOGGER.info(" bulk start ....");
        for(List<String> tempDoc : docList){
            BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
            for(String document : tempDoc){
                if(StringUtils.isNotEmpty(document)){
                    bulkRequestBuilder.add(transportClient.prepareIndex(indexName, typeName).setSource(document));
                }
            }
            bulkRequestBuilder.get();
        }
        LOGGER.info(" bulk end ....");
    }

    /**
     * 搜索接口
     */
    public SearchResponse search(String typeName,QueryBuilder queryBuilder,
                                 String sortField, SortOrder sortOrder,
                                 int curPage, int pageSize, String... indexNames){
        if(!this.esEnable){
            LOGGER.error(" The esEnable is false ... ");
            return null;
        }
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(indexNames).setTypes(typeName);
        //排序
        if (StringUtils.isNotEmpty(sortField)){
            sortOrder = sortOrder == null ? SortOrder.ASC : sortOrder;
            searchRequestBuilder.addSort(sortField, sortOrder);
        }
        if(queryBuilder != null){
            searchRequestBuilder.setQuery(queryBuilder);
        }
        if(curPage > 0 && pageSize > 0){
            searchRequestBuilder.setFrom((curPage-1) * pageSize);
            searchRequestBuilder.setSize(pageSize);
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return searchResponse;
    }

}
