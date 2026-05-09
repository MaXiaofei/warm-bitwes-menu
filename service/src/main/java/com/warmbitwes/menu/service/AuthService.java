package com.warmbitwes.menu.service;

import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.TokenService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public AuthLoginResult login(String username, String password, String clientType) {
        if (!"admin".equals(username) || !"123456".equals(password)) {
            throw new BizException(10001, "账号或密码错误");
        }
        if (!"admin".equals(clientType) && !"mini".equals(clientType)) {
            throw new BizException(10004, "不支持的登录端类型");
        }
        Set<String> permissions = buildPermissionsByClientType(clientType);
        LoginUser loginUser = new LoginUser(
                1L,
                "admin",
                clientType,
                permissions
        );
        String accessToken = tokenService.issueToken(loginUser);
        String refreshToken = tokenService.issueToken(loginUser);
        return new AuthLoginResult(accessToken, refreshToken, 7200);
    }

    public record AuthLoginResult(String accessToken, String refreshToken, Integer expiresIn) {
    }

    private Set<String> buildPermissionsByClientType(String clientType) {
        if ("mini".equals(clientType)) {
            return Set.of("dish:view", "ingredient:view");
        }
        Set<String> permissions = new HashSet<>();
        permissions.add("auth-user:list");
        permissions.add("auth-user:create");
        permissions.add("auth-user:update");
        permissions.add("auth-user:reset-password");
        permissions.add("auth-role:list");
        permissions.add("auth-role:update");
        permissions.add("auth-role:grant");
        permissions.add("auth-permission:list");
        permissions.add("auth-scope:list");
        permissions.add("auth-scope:update");
        permissions.add("dish:view");
        permissions.add("ingredient:view");
        return permissions;
    }
}
