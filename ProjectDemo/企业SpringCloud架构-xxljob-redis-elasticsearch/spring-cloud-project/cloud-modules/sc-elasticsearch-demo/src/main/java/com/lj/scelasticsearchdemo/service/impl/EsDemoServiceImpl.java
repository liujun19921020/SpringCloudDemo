package com.lj.scelasticsearchdemo.service.impl;

import com.lj.scelasticsearchdemo.common.EsPageTool;
import com.lj.scelasticsearchdemo.common.ExtResultMapper;
import com.lj.scelasticsearchdemo.common.vo.PageMap;
import com.lj.scelasticsearchdemo.entity.EsOrderDetail;
import com.lj.scelasticsearchdemo.service.EsDemoService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class EsDemoServiceImpl implements EsDemoService {

    private final static String ES_INDEX = "order_detail";
    private final static String ES_TYPE = "order_detail";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private TransportClient esClient;

    @Resource
    private ExtResultMapper extResultMapper;

    /**
     * 添加es案例信息 （数据来源为多库时 可以通到java代码实现数据插入；数据来源为同一机器时，建议通过sql+conf的形式导入数据）
     * @author Liujun
     * @param pushMapList
     * @return
     */
    @Override
    public Integer saveEsDemoInfo(List<Map<String, Object>> pushMapList) {
        Integer saveNum = 0;//返回的插入多少条数据
        for (Map<String, Object> pushMap : pushMapList) {
            pushMap.put("id",System.currentTimeMillis()+new Random().nextInt(100));//id不能相等，因为要多次插入一条数据,我们生成不一样的id
            IndexResponse response = esClient.prepareIndex(ES_INDEX, ES_TYPE)
                    .setSource(pushMap)
                    .get();
            String _id = response.getId();
            if (StringUtils.isNotBlank(_id)){
                saveNum++;
            }
        }
        return saveNum;
    }


    /**
     * 查询es案例信息 （假设条件为类似sql:select order_id from sku=xx and site=xx and (order_status=1 or order_status = 2)）
     * @param pageable
     * @param sku
     * @param site
     * @return
     */
    @Override
    public PageMap selectEsDemoInfo(Pageable pageable, String sku, String site) {
        //创建builder
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not

        //设置模糊搜索
        if (!StringUtils.isBlank(sku)) {
            builder.must(QueryBuilders.termQuery("sku.keyword", sku));
        }
        if (!StringUtils.isBlank(site)) {
            builder.must(QueryBuilders.termQuery("site.keyword", site));
        }

        BoolQueryBuilder purBuilder = QueryBuilders.boolQuery();
        purBuilder.should(QueryBuilders.matchPhraseQuery("order_status", "1"));
        purBuilder.should(QueryBuilders.matchPhraseQuery("order_status", "2"));
        builder.must(purBuilder);

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //将搜索条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将分页设置到构建中
        nativeSearchQueryBuilder.withPageable(pageable);

        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //执行,返回结果的分页
        Page<EsOrderDetail> resutlList = elasticsearchTemplate.queryForPage(query, EsOrderDetail.class, extResultMapper);
        return EsPageTool.getPageMap(resutlList, pageable);
    }

}
