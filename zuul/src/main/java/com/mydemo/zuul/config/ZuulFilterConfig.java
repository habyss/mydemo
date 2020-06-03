package com.mydemo.zuul.config;

import com.mydemo.zuul.filter.CommonFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFilterConfig {

    @Bean
    public CommonFilter commonFilter() {
        return new CommonFilter();
    }
}