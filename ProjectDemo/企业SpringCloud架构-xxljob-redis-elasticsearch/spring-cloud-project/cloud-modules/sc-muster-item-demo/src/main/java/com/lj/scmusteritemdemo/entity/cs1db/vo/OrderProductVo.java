package com.lj.scmusteritemdemo.entity.cs1db.vo;

import java.io.Serializable;

/**
 * 订单详情即产品信息
 */
public class OrderProductVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;//订单表ID
    private String orderId;//系统订单号
    private String sku;//SKU
    private String site;//购买站:美国站,德国站等
    private String orderStatus;//订单状态
    private String productName;//名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
