package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        //1.查询该用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if(dbUser != null){
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        //2.组装实体对象
        User user = new User();
        user.setName(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        //3.插入数据库
        userMapper.insert(user);
        return Result.success("注册成功!");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        // 2. 用户不存在
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 3. 校验密码（核心补充）
        if (!dbUser.getPassword( ).equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        // 4. 登录成功
        return Result.success("登录成功！");
    }

    /**
     * 根据ID查询用户（完整实现）
     */
    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        // 返回用户名（可根据需求修改返回内容）
        return Result.success("查询成功，用户名为：" + user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1.创建分页对象（当前页码，每页条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        // 2.执行分页查询（null为无查询条件，框架自动查总数+分页）
        Page<User> resultPage = userMapper.selectPage(pageParam, null);
        // 3.返回结果（含数据列表、总条数、总页数等）
        return Result.success(resultPage);
    }
}
