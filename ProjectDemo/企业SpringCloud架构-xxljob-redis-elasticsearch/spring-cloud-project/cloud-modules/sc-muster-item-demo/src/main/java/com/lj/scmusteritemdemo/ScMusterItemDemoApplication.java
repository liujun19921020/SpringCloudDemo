package com.lj.scmusteritemdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ScMusterItemDemoApplication {
    private static Logger logger = LoggerFactory.getLogger(ScMusterItemDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScMusterItemDemoApplication.class, args);
        logger.info("============ SC-MUSTER-ITEM-DEMO 系统启动成功 ===========");
    }

}
