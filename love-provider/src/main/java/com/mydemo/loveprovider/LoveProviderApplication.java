package com.mydemo.loveprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kun.han
 */
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableFeignClients
@EnableHystrixDashboard
public class LoveProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoveProviderApplication.class, args);
    }

}
