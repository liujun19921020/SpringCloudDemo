package com.lj.scelasticsearchdemo.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 查询的es分片对应的index/type的字段
 */
@Document(indexName = "order_detail", type = "order_detail")
public class EsOrderDetail implements Serializable {

    private String order_id;
    private String order_status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
