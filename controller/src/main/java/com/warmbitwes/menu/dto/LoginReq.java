package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank(message = "username不能为空") String username,
        @NotBlank(message = "password不能为空") String password,
        @NotBlank(message = "clientType不能为空") String clientType
) {
}

