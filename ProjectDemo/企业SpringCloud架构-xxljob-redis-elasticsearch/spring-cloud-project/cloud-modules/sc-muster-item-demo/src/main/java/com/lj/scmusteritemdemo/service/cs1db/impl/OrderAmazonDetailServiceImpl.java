package com.lj.scmusteritemdemo.service.cs1db.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.commonshttp.response.Code;
import com.lj.commonshttp.response.ResponseMsg;
import com.lj.scmusteritemdemo.common.DBTypeEnum;
import com.lj.scmusteritemdemo.common.DataSourceSwitch;
import com.lj.scmusteritemdemo.entity.cs1db.vo.OrderProductVo;
import com.lj.scmusteritemdemo.feign.ScItemDemoFeignClient;
import com.lj.scmusteritemdemo.mapper.cs1db.OrderAmazonDetailMapper;
import com.lj.scmusteritemdemo.service.cs1db.IOrderAmazonDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderAmazonDetailServiceImpl implements IOrderAmazonDetailService {

    @Resource
    private OrderAmazonDetailMapper orderAmazonDetailMapper;

    @Autowired
    private ScItemDemoFeignClient scItemDemoFeignClient;

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param pagePrams 分页插件配置
     * @param orderId 查询条件
     * @param orderStatus 查询条件
     * @param sku 查询条件
     * @return
     */
    @Override
    public List<OrderProductVo> getOrderListInfo(Page pagePrams, String orderId, String orderStatus, String sku) {
        return orderAmazonDetailMapper.getOrderListInfo(pagePrams, orderId, orderStatus, sku);
    }

    /**
     * 根据 订单号/商品sku 批量修改订单状态(Feign方式调用)
     * @param data
     */
    @Override
    public void updateOrderStatus(JSONObject data) throws Exception {
        ResponseMsg responseMsg = scItemDemoFeignClient.updateOrderStatus(data);
        if (null == responseMsg) {// 调用服务失败
            throw new Exception("调用sc-item-demo服务失败");
        }
        if (responseMsg.getCode() != Code.SUCCESS) {// 调用服务报错
            throw new Exception(responseMsg.getMsg());
        }
        //List<Map<String,Object>> categoryIdAndNameLine = JSON.parseObject(JSON.toJSONString(responseMsg.getData()), new TypeReference<List<Map<String,Object>>>(){}.getType());//如果data中有list返回值，可以如图所示的转译
    }

}
