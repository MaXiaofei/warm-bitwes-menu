package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 做饭会话创建请求。
 */
@Data
@NoArgsConstructor
@Schema(description = "做饭会话创建请求")
public class CookSessionCreateReq {
    @Schema(description = "模板ID", example = "1")
    @NotNull(message = "templateId不能为空")
    private Long templateId;

    @Schema(description = "开始时间", example = "2026-04-24T18:30:00")
    @NotNull(message = "startedAt不能为空")
    private LocalDateTime startedAt;
}

