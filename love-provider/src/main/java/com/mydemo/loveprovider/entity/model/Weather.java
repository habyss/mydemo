package com.mydemo.loveprovider.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kun.han on 2019/6/13 14:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    /**
     * city : 101020100
     * cityname : 上海
     * temp : 24℃
     * tempn : 20℃
     * weather : 小雨
     * wd : 东南风转东北风
     * ws : 3-4级
     * weathercode : d7
     * weathercoden : n7
     * fctime : 20190613113000
     */

    private String city;
    private String cityname;
    private String temp;
    private String tempn;
    private String weather;
    private String wd;
    private String ws;
    private String weathercode;
    private String weathercoden;
    private String fctime;
}
