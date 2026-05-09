package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "通用ID响应")
public record IdResp(
        @Schema(description = "主键ID", example = "1")
        Long id) {
}

