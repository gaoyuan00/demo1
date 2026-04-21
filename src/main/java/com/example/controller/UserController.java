package com.example.controller;

import com.example.common.Result;
import com.example.dto.UserDTO;
import com.example.entity.UserInfo;
import com.example.service.UserService;
import com.example.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    // 分页查询用户列表 GET /api/users/page
    @GetMapping("/page")
    public Result<Object> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long userId) {
        return userService.getUserDetail(userId);
    }
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(@PathVariable("id") Long userId,
                                         @RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }
}