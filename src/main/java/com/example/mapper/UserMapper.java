package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper //标识该接口为 MyBatis 的Mapper,交由 Spring容器管理代理对象
public interface UserMapper extends BaseMapper<User> {
    //继承BaseMapper，无需自定义方法即可使用基础CRUD
}
