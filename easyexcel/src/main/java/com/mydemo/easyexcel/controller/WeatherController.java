package com.mydemo.easyexcel.controller;

import com.mydemo.common.result.BaseAO;
import com.mydemo.common.result.JsonResult;
import com.mydemo.easyexcel.entity.WeatherConfig;
import com.mydemo.easyexcel.service.WeatherConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kun.han on 2020/6/28 15:23
 */
@RestController
public class WeatherController {

    @Resource
    private WeatherConfigService weatherConfigService;

    @GetMapping("test")
    public BaseAO test(String type){

        List<WeatherConfig> byType = weatherConfigService.getByType(type);
        return JsonResult.successMap("su", byType);

    }
}
