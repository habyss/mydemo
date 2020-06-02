package com.mydemo.loveprovider.task;

import com.mydemo.loveprovider.service.impl.WeatherServiceProxyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author kun.han on 2019/6/12 16:58
 */
@Service
public class WeatherServiceTask {

    private final Logger logger = LoggerFactory.getLogger(WeatherServiceTask.class);

    @Resource
    private WeatherServiceProxyImpl weatherService;

    /**
     * 发送邮件 定时任务
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void getWeather() throws IOException {
        weatherService.sendWeatherMail();
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void test() {
        logger.info("test - scheduled");
    }

    @Scheduled(cron = "0 0/3 8 * * ?")
    public void stealSubject(){
        String subject = weatherService.stealSubject();
        logger.debug(subject);
    }
}
