package com.mydemo.loveconsumer.config;

import com.mydemo.common.result.BaseAO;
import com.mydemo.common.result.JsonResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kun.han on 2020/6/3 15:28
 * 全局数据处理组件
 *
 * 注解 @ControllerAdvice
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handler bind exception base ao.
     *  BindException 全局异常捕获
     *
     * @param bindException the bind exception
     * @return the base ao
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseAO handlerBindException(BindException bindException){
        StringBuilder builder = new StringBuilder();
        bindException.getAllErrors().forEach(x -> builder.append(x.getDefaultMessage()).append(","));
        return JsonResult.failureMap(builder.toString());
    }
}
