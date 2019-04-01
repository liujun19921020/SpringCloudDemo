package com.liujun.servicefeign.clients.fallback;

import com.liujun.servicefeign.clients.FeignServiceHi;
import org.springframework.stereotype.Component;

@Component
public class FeignServiceHiHystrix implements FeignServiceHi {
    @Override
    public String homeFromClientOne(String name) {
        return "sorry, you are fail,"+name;
    }
}