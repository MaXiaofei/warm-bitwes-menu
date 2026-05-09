package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 做饭会话创建请求。
 */
@Data
@NoArgsConstructor
@Schema(description = "做饭会话创建请求：菜单模板与自选菜品二选一，且开始时间必填")
public class CookSessionCreateReq {
    @Schema(description = "菜单模板ID；与 dishIds 二选一，若传模板则 dishIds 应为空", example = "1")
    private Long templateId;

    @Schema(description = "自选菜品ID列表（有序）；与 templateId 二选一，若传列表则 templateId 应为空", example = "[1,2]")
    private List<Long> dishIds = new ArrayList<>();

    @Schema(description = "开始时间", example = "2026-04-24T18:30:00")
    @NotNull(message = "startedAt不能为空")
    private LocalDateTime startedAt;
}

