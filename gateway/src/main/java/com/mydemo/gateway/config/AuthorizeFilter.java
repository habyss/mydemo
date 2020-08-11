// package com.mydemo.gateway.config;
//
// import com.alibaba.fastjson.JSONObject;
// import com.mydemo.gateway.result.AuthUtils;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
// import org.springframework.cloud.gateway.support.BodyInserterContext;
// import org.springframework.core.Ordered;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferUtils;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.stereotype.Component;
// import org.springframework.util.MultiValueMap;
// import org.springframework.util.StringUtils;
// import org.springframework.web.reactive.function.BodyInserter;
// import org.springframework.web.reactive.function.BodyInserters;
// import org.springframework.web.reactive.function.server.HandlerStrategies;
// import org.springframework.web.reactive.function.server.ServerRequest;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import java.nio.charset.StandardCharsets;
// import java.util.Arrays;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// /**
//  * token校验全局过滤器
//  * @author kun.han
//  */
// @Component
// public class AuthorizeFilter implements GlobalFilter, Ordered {
//     private static final String AUTHORIZE_TOKEN = "token";
//
//
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         System.out.println("AuthorizeFilter");
//
//         Object requestBody = exchange.getAttribute("cachedRequestBodyObject");
//
//         System.out.println(JSONObject.toJSONString(requestBody));
//         ServerHttpRequest request = exchange.getRequest();
//         // 某些路径不作验证
//         String path = request.getPath().toString();
//         if (path.contains("/lov/")){
//             return chain.filter(exchange);
//         }
//
//         if (HttpMethod.POST.equals(exchange.getRequest().getMethod())){
//             // 重新构造request
//             ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
//             MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
//             // 重点
//             Mono modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
//                 // APPLICATION_JSON
//                 if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
//                     // body为string
//                     JSONObject jsonObject = JSONObject.parseObject(body);
//                     String newBody = "对body进行处理之后的新body";
//                     return Mono.just(newBody);
//                 }else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)){
//                     // xxx-form- 类型的数据解析成map
//                     Map<String, Object> bodyMap = decodeBody(body);
//                     // 对bodyMap进行处理之后的newBodyMap
//                     Map<String, Object> newBodyMap = bodyMap ;
//                     // 通过自定义工具重新组装 xxx-form-
//                     String encodeBody = encodeBody(newBodyMap);
//                     return Mono.just(encodeBody);
//                 }
//                 return Mono.empty();
//             });
//             BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
//             HttpHeaders httpHeaders = new HttpHeaders();
//             httpHeaders.putAll(exchange.getRequest().getHeaders());
//             // 先删除 content length
//             httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
//             // MyCachedBodyOutputMessage 这个类完全就是CachedBodyOutputMessage，只不过CachedBodyOutputMessage不是公共的
//             CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);
//             return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
//                 ServerHttpRequest decorator = this.decorate(exchange, httpHeaders, outputMessage);
//                 return chain.filter(exchange.mutate().request(decorator).build());
//             }));
//         } else {
//             return chain.filter(exchange);
//         }
//     }
//
//     @Override
//     public int getOrder() {
//         return 0;
//     }
//
//     ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
//         return new ServerHttpRequestDecorator(exchange.getRequest()) {
//             @Override
//             public HttpHeaders getHeaders() {
//                 long contentLength = headers.getContentLength();
//                 HttpHeaders httpHeaders = new HttpHeaders();
//                 httpHeaders.putAll(super.getHeaders());
//                 if (contentLength > 0L) {
//                     // 重新写 content length
//                     httpHeaders.setContentLength(contentLength);
//                 } else {
//                     httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
//                 }
//                 return httpHeaders;
//             }
//             @Override
//             public Flux<DataBuffer> getBody() {
//                 return outputMessage.getBody();
//             }
//         };
//     }
//
//     private Map<String, Object> decodeBody(String body) {
//         return Arrays.stream(body.split("&"))
//                 .map(s -> s.split("="))
//                 .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
//     }
//
//     private String encodeBody(Map<String, Object> map) {
//         return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
//     }
// }