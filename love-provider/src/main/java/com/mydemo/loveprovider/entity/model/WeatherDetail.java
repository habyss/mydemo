package com.mydemo.loveprovider.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kun.han on 2019/6/13 14:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDetail {
    /**
     * nameen : shanghai
     * cityname : 上海
     * city : 101020100
     * temp : 21
     * tempf : 69
     * WD : 静风
     * wde : NW
     * WS : 0级
     * wse :
     * SD : 87%
     * time : 14:10
     * weather : 雨
     * weathere : rain
     * weathercode : d301
     * qy : 1006
     * njd : 8.34km
     * sd : 87%
     * rain : 0.2
     * rain24h : 0
     * aqi : 36
     * limitnumber :
     * aqi_pm25 : 36
     * date : 06月13日(星期四)
     */

    private String nameen;
    private String cityname;
    private String city;
    private String temp;
    private String tempf;
    private String WD;
    private String wde;
    private String WS;
    private String wse;
    private String SD;
    private String time;
    private String weather;
    private String weathere;
    private String weathercode;
    private String qy;
    private String njd;
    private String sd;
    private String rain;
    private String rain24h;
    private String aqi;
    private String limitnumber;
    private String aqi_pm25;
    private String date;

}
