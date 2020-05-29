package com.mydemo.easyexcel.entity;

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

    private String name;

    private String phone;

}
