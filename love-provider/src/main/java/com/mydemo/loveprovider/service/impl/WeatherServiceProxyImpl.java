package com.mydemo.loveprovider.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mydemo.common.constant.ConstantMsg;
import com.mydemo.common.result.BaseAO;
import com.mydemo.common.result.JsonResult;
import com.mydemo.loveprovider.entity.WeatherConfig;
import com.mydemo.loveprovider.entity.model.PageParam;
import com.mydemo.loveprovider.entity.model.Weather;
import com.mydemo.loveprovider.entity.model.WeatherCustom;
import com.mydemo.loveprovider.entity.model.WeatherDetail;
import com.mydemo.loveprovider.mapper.WeatherConfigMapper;
import com.mydemo.loveprovider.service.WeatherServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kun.han on 2019/6/14 11:04
 */
@RestController
public class WeatherServiceProxyImpl implements WeatherServiceProxy {

    private final Logger logger = LoggerFactory.getLogger(WeatherServiceProxyImpl.class);

    @Resource
    RestTemplate restTemplate;
    @Resource
    JavaMailSenderImpl javaMailSender;
    @Resource
    WeatherConfigMapper weatherConfigMapper;
    @Resource
    ObjectMapper objectMapper;

    @Value("${steal-url}")
    private String stealUrl;

    /**
     * 发送邮件
     *
     * @return string
     */
    @Override
    public BaseAO sendWeatherMail(){
        // 设置获取天气途径
        logger.info("设置天气途径");
        String url = "http://d1.weather.com.cn/weather_index/101020100.html?_=" + System.currentTimeMillis();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Referer", "http://www.weather.com.cn/weather1d/101020100.shtml");
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");

        // 设置 请求头中 的  希望服务器返回给客户端的 数据类型
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptableMediaTypes);

        // 获取天气接口返回数据
        logger.info("获取天气数据");
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        //整理数据
        logger.info("整理天气数据");
        String data = responseEntity.getBody();
        String[] split = data.replaceAll(" ", "").split("var");
        Weather weather = null;
        WeatherDetail weatherDetail = null;
        WeatherCustom weatherCustom = null;
        try {
            weather = objectMapper.readValue(split[1].substring(split[1].indexOf(":") + 1, split[1].length() - 2), Weather.class);
            weatherDetail = objectMapper.readValue(split[3].substring(split[3].indexOf("=") + 1, split[3].length() - 1), WeatherDetail.class);
            weatherCustom = objectMapper.readValue(split[4].substring(split[4].indexOf(":") + 1, split[4].lastIndexOf(",")), WeatherCustom.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JsonResult.failureMap(ConstantMsg.JACKSON_ERROR_AS_BEAN);
        }

        // 整理邮件数据
        logger.info("整理邮件数据");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String[] to = weatherConfigMapper.getAllByType(ConstantMsg.TYPE_TO).stream().map(WeatherConfig::getValue).toArray(String[]::new);
        String from = weatherConfigMapper.getOneByType(ConstantMsg.TYPE_FROM).getValue();

        // 设置收件人 寄件人 内容
        try {
            logger.info("收件人 :[{}]", objectMapper.writeValueAsString(to));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JsonResult.failureMap(ConstantMsg.JACKSON_ERROR_AS_BEAN);
        }
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(getSubject());
        simpleMailMessage.setText(getTextBody(weather, weatherCustom));
        // 发送邮件
        logger.info("发送天气邮件");
        javaMailSender.send(simpleMailMessage);
        logger.info(ConstantMsg.SUCCESS_SEND);
        return JsonResult.successMap(ConstantMsg.SUCCESS_SEND);

    }
    //🧐  🤪
    //👻[得意][骷髅][衰][西瓜][啤酒][太阳][月亮][捂脸][奸笑][机智][耶]😝💪🌂🙈🙊🐒🙉☀️🌤⛅️🌥☁️🌦🌧⛈🌩🌨❄️☃️⛄️🌬💨☔️☂️🌫🌪🌈🍻🍺🚶‍♀️🚶‍♂️🕢

    /**
     * 获得邮件体
     *
     * @param weather       天气
     * @param weatherCustom 天气
     * @return string
     */
    private String getTextBody(Weather weather, WeatherCustom weatherCustom) {

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        StringBuilder sb = new StringBuilder();

        sb.append("\n\uD83D\uDC7B 嘻嘻，崽崽天气来了，今天的温度是").append(weather.getTempn()).append("-").append(weather.getTemp()).append("，天气").append(weather.getWeather()).append("\uD83C\uDF24，风力").append(weather.getWs()).append("️\uD83C\uDF2C\n\n")
                .append("还有今天").append(weatherCustom.getCo_des_s()).append("\n\n")
                .append("紫外线呢，[太阳]").append(weatherCustom.getUv_des_s().replaceAll("。", "，")).append("貌似这个也不用我提醒了\uD83E\uDD14️\n\n");
        boolean boo = DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek);
        if (boo) {
            sb.append("周末呢~ 看看今天能不能去逛街\uD83D\uDEB6\u200D♀\uD83E\uDDD0 ");
        } else {
            sb.append(dayOfWeek.toString().toLowerCase()).append("\uD83E\uDD2A，不能出去逛街，但是也看看呗\uD83D\uDC12，");
        }
        sb.append(weatherCustom.getGj_des_s()).append("\n\n")
                .append("不能喝酒的人，还老是想喝酒，今天").append(weatherCustom.getPj_des_s().replaceAll("。", "，")).append("但是啤酒\uD83C\uDF7B不好喝噻\n\n")
                .append("身体是革命的本钱呢\uD83D\uDCAA，").append(weatherCustom.getGm_des_s()).append("\n\n")
                .append("今天洗不洗衣服呢，");
        if (boo) {
            sb.append("周末的早晨，应该可以洗一洗吧\uD83D\uDC12，");
        } else {
            sb.append("工作日呢，不能洗衣服噻\uD83D\uDC12，");
        }
        sb.append(weatherCustom.getLs_des_s()).append("\n\n")
                .append("老是忘记带伞\uD83C\uDF02的小柔柔，").append(weatherCustom.getYs_des_s()).append("\n")
        ;
        return sb.toString();
    }

    /**
     * 获取一个subject
     *
     * @return string
     */
    private String getSubject() {
        LocalDateTime nowDate = LocalDateTime.now();
        WeatherConfig subject = weatherConfigMapper.getSubject(ConstantMsg.TYPE_SUBJECT);
        subject.setUpdateTime(nowDate);
        weatherConfigMapper.updateByPrimaryKey(subject);
        return subject.getValue();
    }

    public String getSubjectFromUrl(){
        return restTemplate.getForObject(stealUrl, String.class);
    }

    /**
     * 增加subject
     *
     * @param subject subject
     * @return string
     */
    @Override
    public BaseAO addSubject(String subject) {
        LocalDateTime now = LocalDateTime.now();
        WeatherConfig weather = new WeatherConfig();
        weather.setUpdateTime(now);
        weather.setStatus(1);
        weather.setType(ConstantMsg.TYPE_SUBJECT);
        weather.setValue(subject);
        weatherConfigMapper.insert(weather);
        logger.info(ConstantMsg.SUCCESS_INSERT + " -- subject:" + weather.getValue());
        return JsonResult.successMap(ConstantMsg.SUCCESS_INSERT);
    }

    /**
     * 删除subject
     *
     * @param id id
     * @return string
     */
    @Override
    public BaseAO deleteSubject(Long id) {
        WeatherConfig weatherConfig = new WeatherConfig();
        weatherConfig.setStatus(0);
        weatherConfig.setId(id);
        weatherConfigMapper.updateByPrimaryKeySelective(weatherConfig);
        logger.info(ConstantMsg.SUCCESS_DELETE + " -- id:" + id);
        return JsonResult.successMap(ConstantMsg.SUCCESS_DELETE);
    }

    /**
     * 获取所有的subject
     *
     * @return list
     */
    @Override
    public BaseAO getAllSubject(@RequestBody PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<WeatherConfig> subjectResult = weatherConfigMapper.getAllByType(ConstantMsg.TYPE_SUBJECT);
        try {
            System.out.println(objectMapper.writeValueAsString(subjectResult));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JsonResult.failureMap(ConstantMsg.JACKSON_ERROR_AS_STRING, subjectResult);
        }
        PageInfo<WeatherConfig> info = new PageInfo<>(subjectResult);
        int i = 1 / 0;
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, info);
    }

    @Override
    public BaseAO test() {
        List<Map> resultList = weatherConfigMapper.test(ConstantMsg.TYPE_SUBJECT);
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, resultList);
    }

    public String stealSubject(){
        LocalDateTime now = LocalDateTime.now();
        String subject = getSubjectFromUrl();
        List<WeatherConfig> allByValue = weatherConfigMapper.getAllByValue(subject);
        if (!CollectionUtils.isEmpty(allByValue)) {
            return "已存在 : " + subject;
        }
        WeatherConfig weatherConfig = new WeatherConfig();
        weatherConfig.setStatus(1);
        weatherConfig.setUpdateTime(now);
        weatherConfig.setType(ConstantMsg.TYPE_SUBJECT);
        weatherConfig.setValue(subject);
        weatherConfigMapper.insertSelective(weatherConfig);
        return subject;
    }

}

