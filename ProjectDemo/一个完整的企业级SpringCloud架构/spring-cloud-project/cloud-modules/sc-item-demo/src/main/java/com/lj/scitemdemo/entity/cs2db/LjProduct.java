package com.lj.scitemdemo.entity.cs2db;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 产品对象
 */
public class LjProduct extends Model<LjProduct> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String sku;//SKU
    private String productName;//名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
