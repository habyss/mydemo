package com.mydemo.easyexcel.service.impl;

import com.mydemo.easyexcel.entity.DemoData;
import com.mydemo.easyexcel.entity.QWeatherConfig;
import com.mydemo.easyexcel.entity.WeatherConfig;
import com.mydemo.easyexcel.jpa.WeatherRepository;
import com.mydemo.easyexcel.service.WeatherConfigService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kun.han on 2020/6/28 15:50
 */
@Service
public class WeatherConfigServiceImpl extends BaseService implements WeatherConfigService  {


    @Resource
    private WeatherRepository weatherRepository;

    @Override
    public Integer save(WeatherConfig weatherConfig) {
        QWeatherConfig qConfig = QWeatherConfig.weatherConfig;
        Predicate predicate = qConfig.status.eq(1);
        Optional<WeatherConfig> one = weatherRepository.findOne(predicate);
        if (one.isPresent()) {
            WeatherConfig weatherConfig1 = one.get();
        }


        weatherRepository.save(weatherConfig);

        return null;
    }

    @Override
    public List<WeatherConfig> getByType(String type) {
        List<WeatherConfig> byType = weatherRepository.findByType(type);
        List<WeatherConfig> qq = getWeatherConfigs(type, byType);
        return qq;
    }

    private List<WeatherConfig> getWeatherConfigs(String type, List<WeatherConfig> byType) {
        QWeatherConfig qConfig = QWeatherConfig.weatherConfig;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!StringUtils.isEmpty(type)){
            booleanBuilder.and(qConfig.type.eq(type));
        }
        if (!CollectionUtils.isEmpty(byType)) {
            List<Integer> integerList = byType.stream().map(WeatherConfig::getStatus).collect(Collectors.toList());
            booleanBuilder.and(qConfig.status.in(integerList));
        }

        List<DemoData> test = queryFactory.select(Projections.bean(DemoData.class, qConfig.status.as("test")))
                .from(qConfig)
                .where(booleanBuilder)
                .fetch();

        queryFactory.delete(qConfig);


        return null;
    }
}
