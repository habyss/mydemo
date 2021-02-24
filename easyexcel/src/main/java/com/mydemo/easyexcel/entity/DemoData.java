package com.mydemo.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kun.han on 2020/5/29 11:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoData {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String phone;

    /**
     * 忽略的字段
     */
    @ExcelIgnore
    private String ignore;
}
