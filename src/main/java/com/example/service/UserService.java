package com.example.service;

import com.example.common.Result;
import com.example.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
}