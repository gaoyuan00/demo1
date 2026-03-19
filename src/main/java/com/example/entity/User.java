package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * Lombok注解：
 * @Data：自动生成getter、setter、toString、equals、hashCode等方法
 * @NoArgsConstructor：无参构造方法
 * @AllArgsConstructor：全参构造方法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;     // 用户ID
    private String name; // 用户名
    private Integer age; // 年龄
}