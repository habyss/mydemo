package com.mydemo.loveprovider.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author kun.han on 2020/6/3 14:11
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageNum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageSize;

}
