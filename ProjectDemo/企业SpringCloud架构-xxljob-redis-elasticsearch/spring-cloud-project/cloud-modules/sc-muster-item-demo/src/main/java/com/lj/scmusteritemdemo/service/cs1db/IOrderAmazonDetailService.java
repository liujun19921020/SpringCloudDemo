package com.lj.scmusteritemdemo.service.cs1db;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.scmusteritemdemo.entity.cs1db.vo.OrderProductVo;

import java.util.List;

/**
 * 订单表 Service 接口
 */
public interface IOrderAmazonDetailService {

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param pagePrams 分页插件配置
     * @param orderId 查询条件
     * @param orderStatus 查询条件
     * @param sku 查询条件
     * @return
     */
    List<OrderProductVo> getOrderListInfo(Page pagePrams, String orderId, String orderStatus, String sku);

    /**
     * 根据 订单号/商品sku 批量修改订单状态(Feign方式调用)
     * @param data
     * @throws Exception
     */
    void updateOrderStatus(JSONObject data) throws Exception;
}
