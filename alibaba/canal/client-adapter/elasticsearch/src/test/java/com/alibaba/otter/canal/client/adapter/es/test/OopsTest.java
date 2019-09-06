package com.alibaba.otter.canal.client.adapter.es.test;

import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.stats.*;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author lzy
 * @version 1.0
 * @description TODO
 * @date 2019/7/24 11:25
 **/
@Ignore
public class OopsTest {

    private TransportClient transportClient;

    @Before
    public void init() throws UnknownHostException {
        /*transportClient = new PreBuiltTransportClient(Settings.builder().put("cluster.name", "ebuy-cloud-cluster")
                .put("searchguard.ssl.transport.pemkey_filepath", Paths.get("D:\\ssl\\spock.key"))
                .put("searchguard.ssl.transport.pemcert_filepath", Paths.get("D:\\ssl\\spock.pem"))
                .put("searchguard.ssl.transport.pemtrustedcas_filepath", Paths.get("D:\\ssl\\root-ca.pem"))
                .put("searchguard.ssl.transport.pemkey_password", "3QgfFoYd8Ken").build(), SearchGuardSSLPlugin.class);*/
        transportClient = new PreBuiltTransportClient(Settings.builder().put("cluster.name", "ebuy-cloud-cluster")
                .put("searchguard.ssl.transport.pemkey_filepath", Paths.get("D:\\ssl\\spock.key"))
                .put("searchguard.ssl.transport.pemcert_filepath", Paths.get("D:\\ssl\\spock.pem"))
                .put("searchguard.ssl.transport.pemtrustedcas_filepath", Paths.get("D:\\ssl\\root-ca.pem"))
                .put("searchguard.ssl.transport.pemkey_password", "3QgfFoYd8Ken").build(), SearchGuardSSLPlugin.class);
        transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.71.246"), 9300));
    }

    @After
    public void after() {
        transportClient.close();
    }

    @Test
    public void createIndex() {
        String index = "zyloops";
        assert !transportClient.admin().indices().prepareExists(index).execute().actionGet().isExists();
        assert transportClient.admin().indices().prepareCreate(index).execute().actionGet().isAcknowledged();
    }

    @Test
    public void addType() throws IOException {
        String index = "zyloops_user_single_table";
        String type = "_doc";
        PutMappingRequestBuilder builder = transportClient.admin().indices().preparePutMapping(index);
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
                .startObject("_name").field("type", "text").endObject()
                .startObject("_age").field("type", "text").endObject()
                .startObject("_clzName").field("type", "text").endObject()
                .startObject("_score").field("type", "short").endObject()
                .endObject().endObject();
        builder.setType(type).setSource(mapping);
        assert builder.execute().actionGet().isAcknowledged();
    }

    @Test
    public void addIndexAndType() throws IOException {
//        String index = "zyloops_user_single_table";
//        String type = "_doc";
//        CreateIndexRequestBuilder cib = transportClient.admin().indices().prepareCreate(index);
//        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
//                .startObject("_name").field("type", "text").endObject()
//                .startObject("_age").field("type", "text").endObject()
//                .startObject("_clzName").field("type", "text").endObject()
//                .startObject("_score").field("type", "short").endObject()
//                .endObject().endObject();
//        cib.addMapping(type, mapping);

        String index = "zyloops_user";
        String type = "_doc";
        CreateIndexRequestBuilder cib = transportClient.admin().indices().prepareCreate(index);
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
                .startObject("_name").field("type", "text").endObject()
                .startObject("_age").field("type", "text").endObject()
                .endObject().endObject();
        cib.addMapping(type, mapping);
        assert cib.execute().actionGet().isAcknowledged();
    }

    @Test
    public void addDoc() throws IOException {
        String index = "zyloops";
        String type = "user";
        IndexResponse response = transportClient.prepareIndex(index, type, "1")
                .setSource(XContentFactory.jsonBuilder().startObject()
                        .field("_id", "大学英语")
                        .field("_name", 22.33)
                        .field("_age", "大拿")
                        .endObject())
                .get();
    }

    /*@Test
    public void deleteIndex() {
        String index = "zyloops";
        assert transportClient.admin().indices().prepareExists(index).execute().actionGet().isExists();
        assert transportClient.admin().indices().prepareDelete(index).execute().actionGet().isAcknowledged();
    }*/

    @Test
    public void isExistsIndex() {
        assert transportClient.admin().indices().prepareExists("zyloops").execute().actionGet().isExists();
    }

    @Test
    public void isExistsType() {
        assert transportClient.admin().indices().prepareTypesExists("zyloops").setTypes("user").execute().actionGet().isExists();
    }

    @Test
    public void queryAllIndices() {
        transportClient.admin().indices().stats(new IndicesStatsRequest().all()).actionGet().getIndices().keySet().forEach(System.out::println);
    }

    @Test
    public void queryIndex() {
        System.out.println(transportClient.admin().indices().prepareStats("zyloops_user", "zyloops_user_single_table")
                .setIndexing(false).setGet(false)
                .setSearch(false)
                .setWarmer(false).setMerge(false)
                .setRefresh(false).setFlush(false)
                .setQueryCache(false).setFieldData(false)
                .setCompletion(false)
                .setSegments(false).setTranslog(false)
                .get());
    }


    @Test
    public void getDoc() {
        String id = "1";
        System.out.println("zyloops_user:");
        System.out.println(transportClient.prepareGet("zyloops_user", "_doc", id).get());
        System.out.println("zyloops_user_single_table:");
        System.out.println(transportClient.prepareGet("zyloops_user_single_table", "_doc", id).get());
    }

}
