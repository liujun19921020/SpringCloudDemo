package com.lj.scitemdemo.mapper.cs1db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.scitemdemo.entity.cs1db.OrderAmazonDetail;
import com.lj.scitemdemo.entity.cs1db.vo.OrderProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单表 Mapper 接口
 */
public interface OrderAmazonDetailMapper extends BaseMapper<OrderAmazonDetail> {

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param page 分页插件
     * @param orderId 批量订单号
     * @param orderStatus 订单状态
     * @param sku 商品sku
     * @return
     */
    List<OrderProductVo> getOrderListInfo(Page<OrderProductVo> page, @Param("orderId") String orderId, @Param("orderStatus") String orderStatus, @Param("sku") String sku);

    /**
     * 根据 订单号/商品sku 批量修改订单状态
     * @param orderStatus 订单状态
     * @param orderIdList 批量订单号
     * @param sku 商品SKU
     * @return
     */
    Integer updateOrderStatus(@Param("orderStatus") String orderStatus, @Param("orderIdList") List<String> orderIdList, @Param("sku") String sku);
}
