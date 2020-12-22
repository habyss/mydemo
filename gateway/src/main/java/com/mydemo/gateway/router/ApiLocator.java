package com.mydemo.gateway.router;

import com.mydemo.gateway.config.RequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author kun.han
 */
@EnableAutoConfiguration
@Component
public class ApiLocator {

    @Resource
    private RequestFilter requestFilter;

    public static Logger LOGGER = LoggerFactory.getLogger(ApiLocator.class);

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {

        /*
        route1 是get请求，get请求使用readBody会报错
        route2 是post请求，Content-Type是application/x-www-form-urlencoded，readbody为String.class
        route3 是post请求，Content-Type是application/json,readbody为Object.class
service-url:
  love: http://182.43.148.85:10003
spring:
  cloud:
    gateway:
      routes:
        - id: love #路由的ID
          uri: ${service-url.love}/ #匹配后路由地址
          predicates: # 断言，路径相匹配的进行路由
            - Path=/love/**
          filters:
            - StripPrefix=1 # 截取
         */
        RouteLocatorBuilder.Builder serviceProvider = builder.routes()
                .route("auth",
                        r -> r
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .or()
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                // .and()
                                // .method(HttpMethod.POST)
                                .and()
                                // 读到缓存 cachedRequestBodyObject 中
                                .readBody(String.class, readBody -> {
                                    LOGGER.info("request method POST, body  is:{}", readBody);
                                    return true;
                                })
                                .and()
                                .path("/auth/**")
                                .filters(f -> {
                                    f.filter(requestFilter);
                                    // f.modifyRequestBody(String.class, String.class, (exchange, s) -> {
                                    //     exchange.getRequest();
                                    //     return Mono.just(s.toUpperCase());
                                    // });
                                    f.stripPrefix(1);
                                    return f;
                                })
                                .uri("http://10.0.31.34:10005"));

        RouteLocator routeLocator = serviceProvider.build();
        System.out.println("custom RouteLocator is loading ... {}" + routeLocator);
        return routeLocator;
    }
}