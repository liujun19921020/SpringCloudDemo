package com.lj.scitemdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableFeignClients
@MapperScan(basePackages = "com.lj.scitemdemo.mapper.*")
public class ScItemDemoApplication {
    private static Logger logger = LoggerFactory.getLogger(ScItemDemoApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ScItemDemoApplication.class, args);
        logger.info("============ SC-ITEM-DEMO 系统启动成功 ===========");
    }

}
