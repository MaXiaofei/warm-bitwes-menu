package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.LoginReq;
import com.warmbitwes.menu.dto.LoginResp;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录鉴权接口（V1 最小可联调版本）。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 登录获取 Token。
     *
     * @param req 登录请求
     * @return accessToken/refreshToken/expiresIn
     */
    @PostMapping("/login")
    public ApiResponse<LoginResp> login(@RequestBody @Valid LoginReq req) {
        // V1-P0：先提供最小可联调实现。后续接入真实鉴权/用户体系时保持响应结构不变。
        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        return ApiResponse.success(new LoginResp(accessToken, refreshToken, 7200));
    }
}

