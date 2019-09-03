package com.lj.scxxljobdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScXxlJobDemoApplication {
    private static Logger logger = LoggerFactory.getLogger(ScXxlJobDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScXxlJobDemoApplication.class, args);
        logger.info("============ SC-XXL-JOB-DEMO 系统启动成功 ===========");
    }

}
