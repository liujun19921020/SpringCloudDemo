package com.lj.scitemdemo.service.cs1db.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.scitemdemo.common.DBTypeEnum;
import com.lj.scitemdemo.common.DataSourceSwitch;
import com.lj.scitemdemo.entity.cs1db.OrderAmazonDetail;
import com.lj.scitemdemo.entity.cs1db.vo.OrderProductVo;
import com.lj.scitemdemo.entity.cs2db.LjProduct;
import com.lj.scitemdemo.mapper.cs1db.OrderAmazonDetailMapper;
import com.lj.scitemdemo.service.cs1db.IOrderAmazonDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderAmazonDetailServiceImpl implements IOrderAmazonDetailService {

    @Autowired
    private OrderAmazonDetailMapper orderAmazonDetailMapper;

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param pagePrams 分页插件配置
     * @param orderId 查询条件
     * @param orderStatus 查询条件
     * @param sku 查询条件
     * @return
     */
    @Override
    @DataSourceSwitch(DBTypeEnum.cs1db)
    public List<OrderProductVo> getOrderListInfo(Page pagePrams, String orderId, String orderStatus, String sku) {
        return orderAmazonDetailMapper.getOrderListInfo(pagePrams, orderId, orderStatus, sku);
    }

    /**
     * 根据 订单号/商品sku 批量修改订单状态
     * @param orderStatus 状态
     * @param orderIdList 批量订单号
     * @param sku 商品SKU
     * @return
     */
    @Override
    @Transactional
    @DataSourceSwitch(DBTypeEnum.cs1db)
    public Integer updateOrderStatus(String orderStatus, List<String> orderIdList, String sku) {
        List<String> list = new ArrayList<>();
        for (String orderId : orderIdList) {
            //通过Mybatis-Plus查看数据库表中 是否存在需要修改的数据（使用该方法是 Mapper接口需要继承BaseMapper<T>）
            Integer orderNum = orderAmazonDetailMapper.selectCount(new QueryWrapper<OrderAmazonDetail>().eq("order_id", orderId));
            if (orderNum > 0) {
                list.add(orderId);
            }
        }

        //当存在修改条件时修改数据
        if (StringUtils.isNotEmpty(sku) || (list != null && list.size() != 0) ) {
            orderAmazonDetailMapper.updateOrderStatus(orderStatus, list, sku);
        }
        return null;
    }
}
