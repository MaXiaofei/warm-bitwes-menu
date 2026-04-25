package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.LoginReq;
import com.warmbitwes.menu.dto.LoginResp;
import com.warmbitwes.menu.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录鉴权接口（V1 最小可联调版本）。
 */
@Tag(name = "认证鉴权")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录获取 Token。
     *
     * @param req 登录请求
     * @return accessToken/refreshToken/expiresIn
     */
    @Operation(summary = "登录获取令牌", description = "使用账号密码登录并返回访问令牌、刷新令牌和过期时间。")
    @PostMapping("/login")
    public ApiResponse<LoginResp> login(@RequestBody @Valid LoginReq req) {
        AuthService.AuthLoginResult result = authService.login(req.username(), req.password(), req.clientType());
        return ApiResponse.success(new LoginResp(result.accessToken(), result.refreshToken(), result.expiresIn()));
    }
}

