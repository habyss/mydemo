package com.mydemo.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Objects;

/**
 * token校验全局过滤器
 * @author kun.han
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "token";
    private static final String AUTHORIZE_UID = "uid";


    @Resource
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String methodValue = request.getMethodValue();
        HttpMethod method = request.getMethod();
        boolean equals = HttpMethod.GET.equals(method);
        MultiValueMap<String, String> queryParams = request.getQueryParams();




        String path = request.getPath().toString();
        if (path.contains("/lov/")){
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        String uid = headers.getFirst(AUTHORIZE_UID);
        if (token == null) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (uid == null) {
            uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
        }

        ServerHttpResponse response = exchange.getResponse();
        boolean boo = (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) && !HttpMethod.POST.equals(request.getMethod());
        if (boo) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }else if (HttpMethod.POST.equals(exchange.getRequest().getMethod())){
            //重新构造request，参考ModifyRequestBodyGatewayFilterFactory
            ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            //重点
            Mono modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
                //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行发挥
                System.out.println(mediaType.toString());
                if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                    JsonNode jsonNode = null;
                    try {
                        jsonNode = objectMapper.readTree(body);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assert jsonNode != null : "jsonNode为null";
                    JsonNode param = jsonNode.get("token");
                    if (Objects.isNull(param)){
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return Mono.error(new Exception("失败"));
                    }
                    String tokenT = param.asText();
                    String newBody;
                    System.out.println(jsonNode.toString());
                    String authTokenRedis = "stringRedisTemplate.opsForValue().get(uid)";
                    if (Objects.isNull(authTokenRedis) || !authTokenRedis.equals(tokenT)) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        newBody = "验签";
                        return Mono.error(new Exception("失败"));
                    }else {
                        newBody = "验签";
                        return Mono.just(tokenT);
                    }
                }else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)){
                    System.out.println(body);
                    return Mono.empty();
                }
                return Mono.empty();
            });
            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(exchange.getRequest().getHeaders());
            //猜测这个就是之前报400错误的元凶，之前修改了body但是没有重新写content length
            httpHeaders.remove("Content-Length");
            //MyCachedBodyOutputMessage 这个类完全就是CachedBodyOutputMessage，只不过CachedBodyOutputMessage不是公共的
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                ServerHttpRequest decorator = this.decorate(exchange, httpHeaders, outputMessage);
                return chain.filter(exchange.mutate().request(decorator).build());
            }));
        }
        String authTokenRedis = "stringRedisTemplate.opsForValue().get(uid)";
        if (Objects.isNull(authTokenRedis) || !authTokenRedis.equals(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0L) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set("Transfer-Encoding", "chunked");
                }
                return httpHeaders;
            }
            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}