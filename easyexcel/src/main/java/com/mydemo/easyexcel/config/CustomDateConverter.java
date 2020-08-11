package com.mydemo.easyexcel.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 其他  比如`@RequestParam`和`@PathVariable`以及不加注解的单参数和对象参数
 */
public class CustomDateConverter {

    public static class LocalDateConvert implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestamp)), ZoneId.systemDefault());
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return null;
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return null;
        }
    }
}