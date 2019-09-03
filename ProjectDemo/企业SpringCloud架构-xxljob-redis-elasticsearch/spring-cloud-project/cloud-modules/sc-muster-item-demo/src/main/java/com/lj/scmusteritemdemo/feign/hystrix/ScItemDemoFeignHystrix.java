package com.lj.scmusteritemdemo.feign.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.lj.commonshttp.response.ResponseMsg;
import com.lj.scmusteritemdemo.feign.ScItemDemoFeignClient;
import org.springframework.stereotype.Component;

/**
 * 调用sc-item-demo服务熔断类
 */
@Component
public class ScItemDemoFeignHystrix implements ScItemDemoFeignClient {
    @Override
    public ResponseMsg updateOrderStatus(JSONObject data) {
        return null;
    }
}
