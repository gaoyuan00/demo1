package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户RESTful接口控制器
 * @RestController：= @Controller + @ResponseBody，返回JSON而非页面
 * @RequestMapping：接口根路径
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 模拟数据库（内存列表）
    private static List<User> userList = new ArrayList<>();
    static {
        // 初始化测试数据
        userList.add(new User(1L, "张三", 20));
        userList.add(new User(2L, "李四", 22));
    }

    /**
     * 1. 查询所有用户（GET请求）
     * 接口地址：GET http://localhost:8080/api/users
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userList);
    }

    /**
     * 2. 根据ID查询单个用户（GET请求 + 路径参数）
     * 接口地址：GET http://localhost:8080/api/users/1
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        // 模拟异常：测试全局异常处理器（删除注释即可触发）
        // int a = 1 / 0;

        User user = userList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        return Result.success(user);
    }

    /**
     * 3. 添加用户（POST请求 + JSON请求体）
     * 接口地址：POST http://localhost:8080/api/users
     * 请求体：{"id":3,"name":"王五","age":25}
     */
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        userList.add(user);
        return Result.success("用户添加成功");
    }

    /**
     * 4. 修改用户（PUT请求 + 路径参数 + JSON请求体）
     * 接口地址：PUT http://localhost:8080/api/users/1
     * 请求体：{"id":1,"name":"张三-修改","age":21}
     */
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        // 查找要修改的用户
        User user = userList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (user != null) {
            user.setName(updateUser.getName());
            user.setAge(updateUser.getAge());
            return Result.success("用户修改成功");
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 5. 删除用户（DELETE请求 + 路径参数）
     * 接口地址：DELETE http://localhost:8080/api/users/1
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        // 过滤掉要删除的用户
        userList = userList.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());
        return Result.success("用户删除成功");
    }
}