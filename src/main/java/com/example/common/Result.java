package com.example.common;

import lombok.Data;

/**
 * 全局统一响应体
 * 泛型T：支持不同类型的返回数据
 */
@Data
public class Result<T> {
    // 响应状态码（200=成功，500=失败）
    private Integer code;
    // 响应提示信息
    private String msg;
    // 响应核心数据
    private T data;

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败响应
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}