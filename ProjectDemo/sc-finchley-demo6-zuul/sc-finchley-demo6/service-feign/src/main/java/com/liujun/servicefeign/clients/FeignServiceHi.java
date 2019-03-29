package com.liujun.servicefeign.clients;

import com.liujun.servicefeign.clients.fallback.FeignServiceHiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi",fallback = FeignServiceHiHystrix.class)
public interface FeignServiceHi {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String homeFromClientOne(@RequestParam(value = "name") String name);
}

