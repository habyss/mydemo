package com.mydemo.loveprovider.mapper;

import com.mydemo.loveprovider.entity.WeatherConfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author kun.han on 2020/6/1 14:19
 */
@Mapper
public interface WeatherConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WeatherConfig record);

    int insertSelective(WeatherConfig record);

    WeatherConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeatherConfig record);

    int updateByPrimaryKey(WeatherConfig record);

    int batchInsert(@Param("list") List<WeatherConfig> list);

    WeatherConfig getOneByType(@Param("type") String type);

    List<WeatherConfig> getAllByType(@Param("type") String type);

    WeatherConfig getSubject(@Param("type") String type);

    int deleteByIdIn(@Param("idCollection") Collection<Long> idCollection);

    List<WeatherConfig> getAllByValue(@Param("value")String value);

    List<Map> test(@Param("type") String type);
}