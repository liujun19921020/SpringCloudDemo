package com.lj.scmusteritemdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.commonshttp.response.Code;
import com.lj.commonshttp.response.ResponseMsg;
import com.lj.scmusteritemdemo.entity.cs1db.vo.OrderProductVo;
import com.lj.scmusteritemdemo.service.cs1db.IOrderAmazonDetailService;
import com.lj.scmusteritemdemo.service.cs2db.ILjProductService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderProduct")
public class OrderProductController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOrderAmazonDetailService orderAmazonDetailService;

    @Autowired
    private ILjProductService ljProductService;

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param data
     * @return
     */
    @PostMapping("/getOrderProductInfo")
    public ResponseMsg getOrderProductInfo(@RequestBody JSONObject data){
        try {
            Integer currentPage = data.getInteger("currentPage");
            Integer pageSize = data.getInteger("pageSize");
            String orderId = data.getString("orderId");
            String orderStatus = data.getString("orderStatus");
            String sku = data.getString("sku");

            //分页规则配置
            Page<OrderProductVo> pagePrams = new Page(currentPage == null ? 1:currentPage, pageSize == null ? 20 : pageSize);
            //查询cs1db库订单数据
            List<OrderProductVo> orderProductList = orderAmazonDetailService.getOrderListInfo(pagePrams, orderId, orderStatus, sku);

            if(orderProductList != null && orderProductList.size() > 0){
                for (OrderProductVo orderProduct : orderProductList) {
                    //查询cs2db商品信息
                    orderProduct.setProductName(ljProductService.getProductName(orderProduct.getSku()));
                }
            }

            pagePrams.setRecords(orderProductList);
            return new ResponseMsg(Code.SUCCESS, pagePrams,"查询订单信息成功");
        } catch (Exception e) {
            logger.error("查询订单信息失败!", e);
            e.printStackTrace();
            return new ResponseMsg<>(Code.FAIL, null, "查询订单信息失败==》" + e.getMessage());
        }
    }


    /**
     * 根据 订单号(批量)/商品sku 修改订单状态
     * @param data
     * @return
     */
    @PostMapping("/updateOrderStatus")
    public ResponseMsg updateOrderStatus(@RequestBody JSONObject data) {
        try {
            //参数校验
            String orderStatus = data.getString("orderStatus");
            if (StringUtils.isEmpty(orderStatus)) {
                return new ResponseMsg<>(Code.UNKNOW, 0, "修改状态orderStatus不可为空！");
            }

            List<String> orderIdList = null;
            if (data.getJSONArray("orderIdList") != null && data.getJSONArray("orderIdList").size() != 0){
                orderIdList = data.getJSONArray("orderIdList").toJavaList(String.class);
            }
            String sku = data.getString("sku");
            if (orderIdList == null && sku == null){
                return new ResponseMsg(Code.UNKNOW,0,"参数orderIdList/sku不可都为空！");
            }

            //执行修改状态(Feign方式调用)
            orderAmazonDetailService.updateOrderStatus(data);

            return new ResponseMsg(Code.SUCCESS,null,"订单状态修改成功！");
        } catch (Exception e) {
            logger.error("订单状态修改失败！", e);
            e.printStackTrace();
            return new ResponseMsg<>(Code.FAIL, 0, "订单状态修改失败==》" + e.getMessage());
        }
    }

}
