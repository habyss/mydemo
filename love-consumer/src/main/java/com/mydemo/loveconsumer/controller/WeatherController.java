package com.mydemo.loveconsumer.controller;

import com.mydemo.common.result.BaseAO;
import com.mydemo.loveconsumer.entity.model.PageParam;
import com.mydemo.loveconsumer.service.WeatherServiceProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author kun.han on 2020/6/2 17:02
 */
@RestController
public class WeatherController {

    @Resource
    WeatherServiceProxy weatherServiceProxy;
    /**
     * 发送邮件
     *
     * @return String
     */
    @RequestMapping("sendWeatherMail")
    BaseAO sendWeatherMail(){
        return weatherServiceProxy.sendWeatherMail();
    }

    /**
     * 增加subject
     *
     * @param subject subject
     * @return string
     */
    @RequestMapping("addSubject")
    BaseAO addSubject(@RequestParam("subject") String subject){
        return weatherServiceProxy.addSubject(subject);
    }

    /**
     * 删除subject
     *
     * @param id id
     * @return string
     */
    @RequestMapping("deleteSubject")
    BaseAO deleteSubject(@RequestParam("id") Long id){
        return weatherServiceProxy.deleteSubject(id);
    }

    /**
     * 获取所有的subject
     *
     * @return list
     */
    @GetMapping("getAllSubject")
    BaseAO getAllSubject(@Valid PageParam pageParam){
        BaseAO subject = null;
        try {
            subject = weatherServiceProxy.getAllSubject(pageParam);
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println(subject);
        return subject;
    }

    @RequestMapping("test")
    BaseAO test(){
        return weatherServiceProxy.test();
    }
}
