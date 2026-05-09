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
            return Set.of(
                    "dropdown-option:list",
                    "dish:view",
                    "ingredient:view",
                    "menu-template:list",
                    "menu-template:detail",
                    "menu-template:ingredient-summary",
                    "cook-session:create",
                    "cook-session:list-mine",
                    "cook-session:event-read",
                    "cook-session:event-write",
                    "cook-session:retrospective-read",
                    "cook-session:retrospective-write",
                    "cook-session:prep-generate",
                    "cook-session:review-taste",
                    "cook-session:review-list"
            );
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
        permissions.add("dropdown-option:list");
        permissions.add("dropdown-option:create");
        permissions.add("dropdown-option:update");
        permissions.add("dropdown-option:delete");
        permissions.add("dish:view");
        permissions.add("ingredient:view");
        permissions.add("menu-template:list");
        permissions.add("menu-template:create");
        permissions.add("menu-template:detail");
        permissions.add("menu-template:update");
        permissions.add("menu-template:ingredient-summary");
        permissions.add("cook-session:create");
        permissions.add("cook-session:list-mine");
        permissions.add("cook-session:event-read");
        permissions.add("cook-session:event-write");
        permissions.add("cook-session:retrospective-read");
        permissions.add("cook-session:retrospective-write");
        permissions.add("cook-session:prep-generate");
        permissions.add("cook-session:review-taste");
        permissions.add("cook-session:review-list");
        return permissions;
    }
}
