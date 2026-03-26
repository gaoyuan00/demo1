package com.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // ========== 只有这两个接口完全放行 ==========
        // 1. 登录接口放行
        boolean isLogin = "/api/users/login".equals(uri);
        // 2. 注册接口放行
        boolean isRegister = "POST".equals(method) && "/api/users".equals(uri);

        if (isLogin || isRegister) {
            return true;
        }

        // ========== 其他所有接口（包括 GET /api/users/{id}）必须校验 Token ==========
        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            response.setContentType("application/json;charset=UTF-8");
            String json = "{\"code\":401,\"msg\":\"请先登录，无权限访问\"}";
            response.getWriter().write(json);
            return false;
        }

        return true;
    }
}
