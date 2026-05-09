package com.warmbitwes.menu.dto;

public record LoginResp(String accessToken, String refreshToken, Integer expiresIn) {
}

