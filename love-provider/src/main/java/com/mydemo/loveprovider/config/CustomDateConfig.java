package com.mydemo.loveprovider.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

/**
 * @author kun.han
 */
@Configuration
public class CustomDateConfig implements WebMvcConfigurer {
    
    /**
     * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //不显示为null的字段
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 过滤对象的null属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略transient
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDateTime 这里只需要LocalDateTime 如果需要转其他的,相应放开注释, 并在上面两个类中适当修改
        javaTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class,new CustomLocalDateDeserializer());
        // // LocalDate
        // javaTimeModule.addSerializer(LocalDate.class, new CustomDateSerializer.LocalDateSerializer());
        // javaTimeModule.addDeserializer(LocalDate.class, new CustomDateDeserializer.LocalDateDeserializer());
        // //Date序列化和反序列化
        // javaTimeModule.addSerializer(Date.class,new CustomDateSerializer.DateSerializer());
        // javaTimeModule.addDeserializer(Date.class,new CustomDateDeserializer.DateDeserializer());

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    /**
     * 其他  比如`@RequestParam`和`@PathVariable`以及不加注解的单参数和对象参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateConverter() {
        //此处不能替换为lambda表达式
        return new CustomLocalDateConverter();
    }
}