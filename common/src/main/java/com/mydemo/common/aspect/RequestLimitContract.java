package com.mydemo.common.aspect;


import com.mydemo.common.annotation.RequestLimit;
import com.mydemo.common.constant.ConstantMsg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author kun.han
 */
@Aspect
@Component
public class RequestLimitContract {

    @Before("@annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) {
        System.out.println("limit - count" + limit.count() + " time" + limit.time());
        String name = joinPoint.getSignature().getName();
        System.out.println("name = " + name);
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
        String ip = getIp();
        System.out.println(ip);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        System.out.println("parameterNames = " + Arrays.toString(parameterNames));
        // throw new RuntimeException("test");
    }

///    @Before("within(com.test.demo.controller.*)")
///    public void requestLimit(final JoinPoint joinPoint){
///        System.out.println("limit - count" + 1 + " time" + 2);
///        String ip = getIp();
///        System.out.println(ip);
///    }

    @Around("@annotation(limit)")
    public Object test(final ProceedingJoinPoint joinPoint, RequestLimit limit) throws Throwable {
        System.out.println("around - " + System.currentTimeMillis());
        Object proceed = joinPoint.proceed();
        System.out.println("around - " + System.currentTimeMillis());
        return proceed;
    }

    private String getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !ConstantMsg.IP_UN_KNOWN.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");

        if (StringUtils.hasText(ip) && !ConstantMsg.IP_UN_KNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
