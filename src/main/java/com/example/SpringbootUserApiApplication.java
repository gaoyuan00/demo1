package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 * @SpringBootApplication 注解：包含@ComponentScan（扫描组件）、@EnableAutoConfiguration（自动配置）等核心功能
 */
@SpringBootApplication
public class SpringbootUserApiApplication {
    public static void main(String[] args) {
        // 启动SpringBoot应用
        SpringApplication.run(SpringbootUserApiApplication.class, args);
        System.out.println("======= 用户接口服务启动成功（端口：8080）=======");
    }
}