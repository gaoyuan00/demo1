package com.example.exception;

import com.example.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @RestControllerAdvice：全局拦截控制器异常，并返回JSON格式响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截所有运行时异常
     * @ExceptionHandler：指定拦截的异常类型
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        // 返回统一的错误响应（包含异常信息）
        return Result.error("系统异常：" + e.getMessage());
    }
}