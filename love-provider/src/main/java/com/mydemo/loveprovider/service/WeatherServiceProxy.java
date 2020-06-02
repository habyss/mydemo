package com.mydemo.loveprovider.service;

import com.mydemo.loveprovider.entity.WeatherConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kun.han on 2020/6/2 10:31
 */
@FeignClient("love-provider")
public interface WeatherServiceProxy {

    /**
     * 发送邮件
     *
     * @return String
     */
    @RequestMapping("sendWeatherMail")
    String sendWeatherMail() throws IOException;

    /**
     * 增加subject
     *
     * @param subject subject
     * @return string
     */
    @RequestMapping("addSubject")
    String addSubject(@RequestParam("subject") String subject);

    /**
     * 删除subject
     *
     * @param id id
     * @return string
     */
    @RequestMapping("deleteSubject")
    String deleteSubject(@RequestParam("id") Long id);

    /**
     * 获取所有的subject
     *
     * @return list
     */
    @RequestMapping("getAllSubject")
    List<WeatherConfig> getAllSubject();

    @RequestMapping("test")
    List<Map> test();
}
