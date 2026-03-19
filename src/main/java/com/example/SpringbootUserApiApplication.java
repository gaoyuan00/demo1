package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootUserApiApplication {
    public static void main(String[] args) {
        // 启动SpringBoot应用
        SpringApplication.run(SpringbootUserApiApplication.class, args);
        System.out.println("======= 用户接口服务启动成功（端口：8080）=======");
    }
}