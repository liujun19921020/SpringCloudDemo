package com.lj.scitemdemo.service.cs2db;

/**
 * 商品表 Service 接口
 */
public interface ILjProductService {
    /**
     * 根据商品sku获取商品名称
     * @param sku
     * @return
     */
    String getProductName(String sku);
}
