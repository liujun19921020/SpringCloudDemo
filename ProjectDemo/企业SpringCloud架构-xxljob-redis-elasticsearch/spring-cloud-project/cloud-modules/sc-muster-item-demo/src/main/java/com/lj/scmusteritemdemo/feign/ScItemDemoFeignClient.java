package com.lj.scmusteritemdemo.feign;

import com.alibaba.fastjson.JSONObject;
import com.lj.commonshttp.response.ResponseMsg;
import com.lj.scmusteritemdemo.feign.hystrix.ScItemDemoFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 调用sc-item-demo服务
 */
@FeignClient(name = "sc-item-demo",fallback = ScItemDemoFeignHystrix.class)
public interface ScItemDemoFeignClient {

    /**
     * 根据 订单号(批量)/商品sku 修改订单状态
     * @param data
     * @return
     */
    @PostMapping("/orderProduct/updateOrderStatus")
    ResponseMsg updateOrderStatus(@RequestBody JSONObject data);
}
