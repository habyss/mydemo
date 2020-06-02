package com.mydemo.loveprovider.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kun.han on 2020/6/1 14:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherConfig implements Serializable {
    private Long id;

    private String type;

    private Integer status;

    private String value;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}