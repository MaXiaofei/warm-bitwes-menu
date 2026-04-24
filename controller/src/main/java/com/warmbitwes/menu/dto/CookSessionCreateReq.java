package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 做饭会话创建请求。
 */
@Data
@NoArgsConstructor
public class CookSessionCreateReq {
    @NotNull(message = "templateId不能为空")
    private Long templateId;

    @NotNull(message = "startedAt不能为空")
    private LocalDateTime startedAt;
}

