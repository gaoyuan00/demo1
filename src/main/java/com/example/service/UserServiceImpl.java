package com.example.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.mapper.UserInfoMapper;
import com.example.mapper.UserMapper;
import com.example.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @Override
    public Result<String> register(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if(dbUser != null){
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        User user = new User();
        user.setName(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);
        return Result.success("注册成功!");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        if (!dbUser.getPassword( ).equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        return Result.success("登录成功！");
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("查询成功，用户名为：" + user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        Page<User> resultPage = userMapper.selectPage(pageParam, null);
        return Result.success(resultPage);
    }

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        
        // 1. 查缓存
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isBlank()) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(cacheVO);
            } catch (Exception e) {
                // 脏缓存删除，查库
                redisTemplate.delete(key);
            }
        }
        
        // 2. 查数据库
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        
        // 3. 写缓存（10分钟过期）
        redisTemplate.opsForValue().set(
            key,
            JSONUtil.toJsonStr(detail),
            10,
            TimeUnit.MINUTES
        );
        
        return Result.success(detail);
    }

    @Override
    @Transactional
    public Result<String> updateUserInfo(UserInfo userInfo) {
        // 参数校验
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        
        // 数据库更新逻辑
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userInfo.getUserId());
        int rows = userInfoMapper.update(userInfo, queryWrapper);
        
        if (rows <= 0) {
            return Result.error(ResultCode.UPDATE_FAILED);
        }
        
        // 更新成功后删除对应Redis缓存（保证一致性）
        String cacheKey = CACHE_KEY_PREFIX + userInfo.getUserId();
        redisTemplate.delete(cacheKey);
        
        return Result.success("更新成功");
    }

    @Override
    public Result<String> deleteUser(Long userId) {
        return null;
    }
}
