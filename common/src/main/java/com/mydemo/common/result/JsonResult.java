package com.mydemo.common.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydemo.common.constant.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kun.han
 * 接口统一返回工具类，数据格式为JSON，每个接口默认返回RESULT,DESCRIPTION,SYSTEMTIME,
 * 具体业务信息放在INFO中
 */
public class JsonResult {
    private static final Logger logger = LoggerFactory.getLogger(JsonResult.class);

    private static <T> BaseAO getMap(int code, String msg, T t) {
        BaseAO baseAO = new BaseAO();
        try {
            baseAO.setResult(code);
            baseAO.setDescription(msg);
            baseAO.setSystemTime(System.currentTimeMillis());
            baseAO.setInfo(t);
            ObjectMapper objectMapper = new ObjectMapper();
            logger.info(objectMapper.writeValueAsString(baseAO));
        } catch (JsonProcessingException e) {
            logger.info("JSON出错");
        }
        return baseAO;
    }

    public static <T> BaseAO successMap(String msg, T T) {
        return getMap(Code.SUCCESS_CODE, msg, T);
    }

    public static <T> BaseAO successMap(String msg) {
        return getMap(Code.SUCCESS_CODE, msg, null);
    }

    public static <T> BaseAO errorMap(String msg, T T) {
        return getMap(Code.ERROR_CODE, msg, T);
    }

    public static <T> BaseAO errorMap(String msg) {
        return getMap(Code.ERROR_CODE, msg, null);
    }

    public static <T> BaseAO failureMap(String msg, T t) {
        return getMap(Code.FAILURE_CODE, msg, t);
    }

    public static <T> BaseAO failureMap(String msg) {
        return getMap(Code.FAILURE_CODE, msg, null);
    }

    public static <T> BaseAO authFailureMap(String msg, T t) {
        return getMap(Code.AUTH_FAILURE, msg, t);
    }

    public static <T> BaseAO authFailureMap(String msg) {
        return getMap(Code.AUTH_FAILURE, msg, null);
    }
}
