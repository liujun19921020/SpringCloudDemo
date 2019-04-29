package com.lj.scitemdemo.entity.cs1db;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 订单详情对象
 */
public class OrderAmazonDetail extends Model<OrderAmazonDetail> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String orderId;//系统订单号
    private String sku;//SKU
    private String site;//购买站:美国站,德国站等
    private String orderStatus;//订单状态

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
}
