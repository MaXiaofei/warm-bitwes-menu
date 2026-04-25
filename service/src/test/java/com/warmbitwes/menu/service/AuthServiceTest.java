package com.warmbitwes.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.TokenService;
import org.junit.jupiter.api.Test;

class AuthServiceTest {
    private final TokenService tokenService = new TokenService();
    private final AuthService authService = new AuthService(tokenService);

    @Test
    void login_should_throw_when_client_type_not_supported() {
        BizException ex = assertThrows(
                BizException.class,
                () -> authService.login("admin", "123456", "unknown")
        );
        assertEquals(10004, ex.getCode());
    }

    @Test
    void login_should_return_tokens_when_credentials_valid() {
        AuthService.AuthLoginResult result = authService.login("admin", "123456", "admin");
        assertTrue(result.accessToken() != null && !result.accessToken().isBlank());
        assertTrue(result.refreshToken() != null && !result.refreshToken().isBlank());
        assertEquals(7200, result.expiresIn());
    }

    @Test
    void login_mini_should_not_get_admin_write_permission() {
        AuthService.AuthLoginResult result = authService.login("admin", "123456", "mini");
        LoginUser loginUser = tokenService.parse(result.accessToken());
        assertTrue(loginUser.getPermissions().contains("dish:view"));
        assertTrue(!loginUser.getPermissions().contains("auth-user:create"));
    }
}
