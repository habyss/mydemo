package com.mydemo.easyexcel.service;

import com.mydemo.easyexcel.entity.WeatherConfig;

import java.util.List;

/**
 * @author kun.han on 2020/6/28 15:48
 */
public interface WeatherConfigService {

    Integer save(WeatherConfig weatherConfig);

    List<WeatherConfig> getByType(String type);
}
