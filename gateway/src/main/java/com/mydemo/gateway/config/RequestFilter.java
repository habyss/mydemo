package com.mydemo.gateway.config;

import com.mydemo.gateway.result.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author kun.han
 */
@Component
public class RequestFilter implements GatewayFilter, Ordered {

    public static Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        LOGGER.info("RequestFilter");

        String requestBody = exchange.getAttribute("cachedRequestBodyObject");
        if (Objects.isNull(requestBody)){
            // 没有body 验证失败
            return AuthUtils.authFailure(exchange);
        }else {
            // 验证
            String token = AuthUtils.getTokenByMediaType(requestBody, exchange.getRequest().getHeaders().getContentType());
            if (AuthUtils.authTokenNormal(token)) {
                return chain.filter(exchange);
            }else {
                // 验证失败
                return AuthUtils.authFailure(exchange);
            }
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }
}