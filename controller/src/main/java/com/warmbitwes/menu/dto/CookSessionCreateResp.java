package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 做饭会话创建响应。
 */
@Schema(description = "做饭会话创建响应")
public record CookSessionCreateResp(
        @Schema(description = "会话ID", example = "1")
        Long sessionId) {
}

