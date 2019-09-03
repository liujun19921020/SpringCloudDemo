package com.lj.scelasticsearchdemo.config;

import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import com.floragunn.searchguard.ssl.util.SSLConfigConstants;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.ResourceUtils;

import java.net.InetAddress;

/**
 * es配置
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lj.scelasticsearchdemo.service")
public class ElasticConfig {
    @Value("${elasticsearch.number}")
    private Integer number;
    @Value("${elasticsearch.host1}")
    private String host1;
    @Value("${elasticsearch.host2}")
    private String host2;
    @Value("${elasticsearch.host3}")
    private String host3;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.cluster-name}")
    private String clusterName;
    @Value("${elasticsearch.ssl.transport.pemkey.password}")
    private String password;
    @Value("${resources.locations1}")
    private String resourcesLocations1;
    @Value("${resources.locations2}")
    private String resourcesLocations2;
    @Value("${resources.locations3}")
    private String resourcesLocations3;
    /**
     * 注入的ElasticSearch实例
     */
    @Bean(name = "esClient")
    public TransportClient getclient()throws Exception {

        Settings settings = Settings.builder()
                // 本地开发使用
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_PEMKEY_FILEPATH, ResourceUtils.getFile(resourcesLocations1))
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_PEMCERT_FILEPATH, ResourceUtils.getFile(resourcesLocations2))
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_PEMTRUSTEDCAS_FILEPATH, ResourceUtils.getFile(resourcesLocations3))
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_PEMKEY_PASSWORD, password)
                .put("cluster.name",clusterName)
                .build();
        TransportClient client = null;
        if(1==number){
            client = new PreBuiltTransportClient(settings, SearchGuardSSLPlugin.class)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host1), port));
        }else if(3==number){
            client = new PreBuiltTransportClient(settings, SearchGuardSSLPlugin.class)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host1), port))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host2), port))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host3), port));
        }else{
            System.out.println("输入节点数量异常！");
            return client;
        }
        // 获取连接
//        client.admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet();

        return client;
    }
}

