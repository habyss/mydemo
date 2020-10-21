package com.mydemo.loveprovider.service;

import com.mydemo.common.result.BaseAO;
import com.mydemo.loveprovider.entity.model.PageParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author kun.han on 2020/6/2 10:31
 */
@FeignClient("love-provider")
public interface WeatherServiceProxy {

    /**
     * 发送邮件
     *
     * @return String base ao
     */
    @RequestMapping("sendWeatherMail")
    BaseAO sendWeatherMail();

    /**
     * 增加subject
     *
     * @param subject subject
     * @return string
     */
    @RequestMapping("addSubject")
    BaseAO addSubject(@RequestParam("subject") String subject);

    /**
     * 删除subject
     *
     * @param id id
     * @return string
     */
    @RequestMapping("deleteSubject")
    BaseAO deleteSubject(@RequestParam("id") Long id);

    /**
     * 获取所有的subject
     *
     * @param pageParam the page info
     * @return list all subject
     */
    @RequestMapping("getAllSubject")
    BaseAO getAllSubject(@RequestBody PageParam pageParam);

    @RequestMapping("test")
    BaseAO test();

    @RequestMapping("getSubject")
    String getOneSubject();
}
