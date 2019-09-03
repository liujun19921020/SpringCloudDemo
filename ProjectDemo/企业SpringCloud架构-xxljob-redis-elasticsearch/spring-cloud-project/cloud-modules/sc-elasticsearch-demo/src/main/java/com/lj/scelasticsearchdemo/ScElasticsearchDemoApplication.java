package com.lj.scelasticsearchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ScElasticsearchDemoApplication {
    private static Logger logger = LoggerFactory.getLogger(ScElasticsearchDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScElasticsearchDemoApplication.class, args);
        logger.info("============ SC-ELASTICSEARCH-DEMO 系统启动成功 ===========");
    }

}
