package com.warmbitwes.menu.security;

import com.warmbitwes.menu.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BizException(10002, "未登录或令牌无效");
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        LoginUser loginUser = tokenService.parse(token);
        if (loginUser == null) {
            throw new BizException(10002, "未登录或令牌无效");
        }
        validateClientScope(request.getRequestURI(), loginUser);
        SecurityContextHolder.set(loginUser);
        RequirePermission requirePermission = method.getMethodAnnotation(RequirePermission.class);
        if (requirePermission == null) {
            return true;
        }
        Set<String> permissions = loginUser.getPermissions();
        if (permissions == null || !permissions.contains(requirePermission.value())) {
            throw new BizException(10003, "无权限访问");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clear();
    }

    private void validateClientScope(String requestUri, LoginUser loginUser) {
        String clientType = loginUser.getClientType();
        if (requestUri != null && requestUri.contains("/api/admin") && !"admin".equals(clientType)) {
            throw new BizException(10003, "无权限访问");
        }
        if (requestUri != null && requestUri.contains("/api/mini") && !"mini".equals(clientType)) {
            throw new BizException(10003, "无权限访问");
        }
    }
}
