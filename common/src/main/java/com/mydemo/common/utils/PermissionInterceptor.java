package com.mydemo.common.utils;

import com.mytian.entity.permission.RequestPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Objects;

public class PermissionInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 在整个请求结束之后被调用，也就是在dispatherServlet渲染了对应视图后执行（主要用于资源清理）
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse arg1, Object arg2, Exception arg3) {
        Object requestStartTime = request.getAttribute("requestStart-Time");
        if (Objects.nonNull(requestStartTime)){
            long time = Instant.now().toEpochMilli() - Long.parseLong(requestStartTime.toString());
            logger.info("response=={}, timeConsuming:{}ms",((HandlerMethod)arg2).getMethod().getName(), time);
        }
    }
    /**
     * 在请求处理之后被调用，但是在视图被渲染前（controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object arg2, ModelAndView arg3) {
        logger.info("response=="+((HandlerMethod)arg2).getMethod().getName());
    }

    /**
     *在请求处理之前被调用，controller方法调用之前
     *true向下执行，false取消当前请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestUrl = request.getScheme() //当前链接使用的协议
                +"://" + request.getServerName()//服务器地址
                + ":" + request.getServerPort() //端口号
                + request.getContextPath() //应用名称，如果应用名称为
                + request.getServletPath() //请求的相对url
                + "?" + request.getQueryString(); //请求参数
        logger.info("requestUrl===="+requestUrl);
        request.setAttribute("requestStart-Time", Instant.now().toEpochMilli());

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用方法
        Method method = handlerMethod.getMethod();
        //如果是调用系统error界面，则直接拦截
        String methodName = method.getName();
        if(method.getName().equals("errorHtml")){
            throw new Exception();
        }
        RequestPermission requestPermission = method.getAnnotation(RequestPermission.class);
        if(requestPermission != null){
            logger.info("tmd===:" + request.getHeader("tmd"));
            System.out.println("permission=============" + requestPermission.permission());
        }
        //满足条件的方法直接放过拦截
        return true;
    }
}
