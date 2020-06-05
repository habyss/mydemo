// package com.mydemo.gateway.config;
//
// import com.alibaba.fastjson.JSONObject;
// import com.mydemo.gateway.result.AuthUtils;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
// import org.springframework.core.Ordered;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferUtils;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.stereotype.Component;
// import org.springframework.util.MultiValueMap;
// import org.springframework.util.StringUtils;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import java.nio.charset.StandardCharsets;
// import java.util.Arrays;
// import java.util.Map;
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
//         // 试图从header和params获取关键验证参数
//         HttpHeaders headers = request.getHeaders();
//         String token = headers.getFirst(AUTHORIZE_TOKEN);
//         if (StringUtils.isEmpty(token)) {
//             token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
//         }
//         // 获取到验证参数 进行验证
//         if (AuthUtils.authTokenNormal(token)){
//             return chain.filter(exchange);
//         }else {
//             return AuthUtils.authFailure(exchange);
//         }
// /*
//         if (StringUtils.hasText(token)){
//             // 验证通过
//             if ("token".equals(token)){
//                 return chain.filter(exchange);
//             }else {
//                 // 验证失败
//                 return AuthUtils.authFailure(exchange);
//             }
//         }else if (StringUtils.isEmpty(token) && !HttpMethod.POST.equals(request.getMethod())){
//             // 若获取不到验证参数并且请求方式不为post 则验证失败
//             return AuthUtils.authFailure(exchange);
//         }*/
//         // else if (HttpMethod.POST.equals(exchange.getRequest().getMethod())){
//         //     // 若获取不到验证参数并且请求方式为post
//         //
//         //     // 重新构造request，参考ModifyRequestBodyGatewayFilterFactory
//         //     ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
//         //     MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
//         //     // 重点
//         //     Mono modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
//         //         //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行发挥
//         //         if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
//         //             JSONObject jsonObject = JSONObject.parseObject(body);
//         //             String tokenT = jsonObject.getString("token");
//         //             if (Objects.isNull(tokenT)){
//         //                 // 验证失败
//         //                 return authFailure(exchange, response);
//         //                 // return Mono.empty();
//         //             }
//         //             String authTokenRedis = "q";
//         //             if (!authTokenRedis.equals(tokenT)) {
//         //                 // 验证失败
//         //                 return authFailure(exchange, response);
//         //             }else {
//         //                 return Mono.just(body);
//         //             }
//         //         }else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)){
//         //             // origin body map
//         //             Map<String, Object> bodyMap = decodeBody(body);
//         //
//         //             // TODO decrypt & auth
//         //
//         //             // new body map
//         //             Map<String, Object> newBodyMap ;
//         //             newBodyMap = bodyMap;
//         //             return Mono.just(encodeBody(newBodyMap));
//         //         }
//         //         return Mono.empty();
//         //     });
//         //     BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
//         //     HttpHeaders httpHeaders = new HttpHeaders();
//         //     httpHeaders.putAll(exchange.getRequest().getHeaders());
//         //     // 猜测这个就是之前报400错误的元凶，之前修改了body但是没有重新写content length
//         //     httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
//         //     // MyCachedBodyOutputMessage 这个类完全就是CachedBodyOutputMessage，只不过CachedBodyOutputMessage不是公共的
//         //     CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);
//         //     return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
//         //         ServerHttpRequest decorator = this.decorate(exchange, httpHeaders, outputMessage);
//         //         return chain.filter(exchange.mutate().request(decorator).build());
//         //     }));
//         // }
//         /*else {
//             return chain.filter(exchange);
//         }*/
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
//
//     private String resolveBodyFromRequest(ServerHttpRequest request){
//         //获取请求体
//         Flux<DataBuffer> body = request.getBody();
//         StringBuilder sb = new StringBuilder();
//
//         body.subscribe(buffer -> {
//             byte[] bytes = new byte[buffer.readableByteCount()];
//             buffer.read(bytes);
//             DataBufferUtils.release(buffer);
//             String bodyString = new String(bytes, StandardCharsets.UTF_8);
//             sb.append(bodyString);
//         });
//         return sb.toString();
//
//     }
// }