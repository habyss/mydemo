package com.mydemo.loveprovider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author kun.han on 2019/6/14 10:46
 */
@Configuration
public class JavaMailSenderConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private Integer port;

    @Bean
    public JavaMailSenderImpl javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        // 邮件设置465端口发送
        Properties properties = new Properties();
        //登录服务器是否需要认证
        properties.put("mail.smtp.auth", "true");
        //SSL证书Socket工厂
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //使用SMTPS协议465端口
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}
