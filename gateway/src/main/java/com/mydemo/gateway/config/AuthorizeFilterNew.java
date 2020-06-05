package com.mydemo.gateway.config;

import com.mydemo.gateway.constant.ConstantMsg;
import com.mydemo.gateway.result.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Objects;

/**
 * token校验全局过滤器
 *
 * @author kun.han
 */
@Component
public class AuthorizeFilterNew implements GlobalFilter, Ordered {

    public static Logger LOGGER = LoggerFactory.getLogger(AuthorizeFilterNew.class);

    private static void run() {
        LOGGER.info("请求结束");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");

        InetSocketAddress remoteAddress = request.getRemoteAddress();
        assert remoteAddress != null : "remoteAddress 为空";
        String hostName = remoteAddress.getHostName();
        URI uri = request.getURI();
        LOGGER.info("remoteAddress : {} --- hostName : {} --- uri : {}", remoteAddress, hostName, uri);

        // 某些路径不作验证
        String path = request.getPath().toString();
        if (path.contains(ConstantMsg.URI_ANON)) {
            return chain.filter(exchange).then(Mono.fromRunnable(AuthorizeFilterNew::run));
        }
        // 试图从header获取关键验证参数
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(ConstantMsg.AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            // 试图从params获取关键验证参数
            token = request.getQueryParams().getFirst(ConstantMsg.AUTHORIZE_TOKEN);
        }
        // 试图从xxx-www-f 和 application/json 获取关键验证参数
        if (!StringUtils.hasText(token) && Objects.nonNull(requestBody)) {
            MediaType mediaType = request.getHeaders().getContentType();
            token = AuthUtils.getTokenByMediaType(requestBody, mediaType);
        }
        // 获取到验证参数 进行验证
        if (AuthUtils.authTokenNormal(token)) {
            return chain.filter(exchange).then(Mono.fromRunnable(AuthorizeFilterNew::run));
        } else {
            return AuthUtils.authFailure(exchange);
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }


}