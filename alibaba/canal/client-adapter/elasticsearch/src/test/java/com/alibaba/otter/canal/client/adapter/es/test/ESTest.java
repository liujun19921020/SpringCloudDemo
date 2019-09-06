package com.alibaba.otter.canal.client.adapter.es.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

@Ignore
public class ESTest {

    private TransportClient transportClient;

    @Before
    public void init() throws UnknownHostException, FileNotFoundException, URISyntaxException {
        Settings.Builder settingBuilder = Settings.builder();
        // System.out.println(ClassLoader.getSystemResource("ssl/spock.key").getPath().toString());
        // System.out.println(ResourceUtils.getFile("").getPath());
        settingBuilder.put("cluster.name", "ebuy-cloud-cluster")
                .put("searchguard.ssl.transport.pemkey_filepath", Paths.get("D:\\ssl\\spock.key"))
                .put("searchguard.ssl.transport.pemcert_filepath", Paths.get("D:\\ssl\\spock.pem"))
                .put("searchguard.ssl.transport.pemtrustedcas_filepath", Paths.get("D:\\ssl\\root-ca.pem"))
                .put("searchguard.ssl.transport.pemkey_password", "3QgfFoYd8Ken");
        Settings settings = settingBuilder.build();
        transportClient = new PreBuiltTransportClient(settings, SearchGuardSSLPlugin.class);
        String[] hostArray = TestConstant.esHosts.split(",");
        for (String host : hostArray) {
            int i = host.indexOf(":");
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.71.246"),
                    9300));
        }
    }

    @Test
    public void test01() {
        SearchResponse response = transportClient.prepareSearch("test")
            .setTypes("osm")
            .setQuery(QueryBuilders.termQuery("_id", "1"))
            .setSize(10000)
            .get();
        for (SearchHit hit : response.getHits()) {
            System.out.println(hit.getSourceAsMap().get("data").getClass());
        }
    }

    @Test
    public void queryIndices(){
        ActionFuture<IndicesStatsResponse> isr = transportClient.admin().indices().stats(new IndicesStatsRequest().all());
        IndicesAdminClient indicesAdminClient = transportClient.admin().indices();
        Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
        isr.actionGet().getIndices().keySet().forEach(i ->{
            System.out.println(i.toString());
        });
    }

    @Test
    public void createIndex(){
        assert transportClient.admin().indices().prepareCreate("zyloops").execute().actionGet().isAcknowledged();
    }

    @Test
    public void addIndexAndType() throws IOException {
        String index = "aliyun2";
        String type = "user";
        // 创建索引映射,相当于创建数据库中的表操作
        CreateIndexRequestBuilder cib = transportClient.admin().indices().prepareCreate(index);
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
                .startObject("_name").field("type", "text").endObject()
                .startObject("_age").field("type", "text").endObject()
                .startObject("_clz_name").field("type", "text").endObject()
                .startObject("_score").field("type", "text").endObject()
                .endObject().endObject();
        cib.addMapping(type, mapping);
        System.out.println(cib.execute().actionGet().isAcknowledged());
    }

    @Test
    public void getDoc(){
        System.out.println(transportClient.prepareGet("aliyun2", "user", "65").get());
    }

    @Test
    public void test02() {
        Map<String, Object> esFieldData = new LinkedHashMap<>();
        esFieldData.put("userId", 2L);
        esFieldData.put("eventId", 4L);
        esFieldData.put("eventName", "网络异常");
        esFieldData.put("description", "第四个事件信息");

        Map<String, Object> relations = new LinkedHashMap<>();
        esFieldData.put("user_event", relations);
        relations.put("name", "event");
        relations.put("parent", "2");

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        bulkRequestBuilder
            .add(transportClient.prepareIndex("test", "osm", "2_4").setRouting("2").setSource(esFieldData));
        commit(bulkRequestBuilder);
    }

    @Test
    public void test03() {
        Map<String, Object> esFieldData = new LinkedHashMap<>();
        esFieldData.put("userId", 2L);
        esFieldData.put("eventName", "网络异常1");

        Map<String, Object> relations = new LinkedHashMap<>();
        esFieldData.put("user_event", relations);
        relations.put("name", "event");
        relations.put("parent", "2");

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        bulkRequestBuilder.add(transportClient.prepareUpdate("test", "osm", "2_4").setRouting("2").setDoc(esFieldData));
        commit(bulkRequestBuilder);
    }

    @Test
    public void test04() {
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        bulkRequestBuilder.add(transportClient.prepareDelete("test", "osm", "2_4"));
        commit(bulkRequestBuilder);
    }

    private void commit(BulkRequestBuilder bulkRequestBuilder) {
        if (bulkRequestBuilder.numberOfActions() > 0) {
            BulkResponse response = bulkRequestBuilder.execute().actionGet();
            if (response.hasFailures()) {
                for (BulkItemResponse itemResponse : response.getItems()) {
                    if (!itemResponse.isFailed()) {
                        continue;
                    }

                    if (itemResponse.getFailure().getStatus() == RestStatus.NOT_FOUND) {
                        System.out.println(itemResponse.getFailureMessage());
                    } else {
                        System.out.println("ES bulk commit error" + itemResponse.getFailureMessage());
                    }
                }
            }
        }
    }

    @After
    public void after() {
        transportClient.close();
    }
}
