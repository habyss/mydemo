package com.mydemo.easyexcel.jpa;

import com.mydemo.easyexcel.entity.WeatherConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kun.han on 2020/6/28 15:25
 */
@Repository
public interface WeatherRepository extends JpaRepository<WeatherConfig, Long>, QuerydslPredicateExecutor<WeatherConfig> {

    List<WeatherConfig> findByType(String type);

}
