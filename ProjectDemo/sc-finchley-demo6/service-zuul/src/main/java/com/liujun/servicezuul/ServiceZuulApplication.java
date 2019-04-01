package com.liujun.servicezuul;

import com.liujun.servicezuul.filter.NameFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
/**
 * 使用@EnableZuulProxy来开启Zuul的支持，如果你不想使用Zuul提供的Filter和反向代理的功能的话，此处可以使用@EnableZuulServer注解
 */
public class ServiceZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceZuulApplication.class, args);
    }

    /**
     * 注意，NameFilter类上没加@Component注解，得配置bean后Filter才生效
     * 这里只做多类型演示，建议按照TokenFilter类写法加@Component注解即可
     * @return
     */
    @Bean
    public NameFilter nameFilter() {
        return new NameFilter();
    }
}
