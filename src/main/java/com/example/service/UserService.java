package com.example.service;

import com.example.common.Result;
import com.example.dto.UserDTO;
import com.example.entity.UserInfo;
import com.example.vo.UserDetailVO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);
    
    Result<UserDetailVO> getUserDetail(Long userId);
    
    Result<String> updateUserInfo(UserInfo userInfo);

    Result<String> deleteUser(Long userId);
}