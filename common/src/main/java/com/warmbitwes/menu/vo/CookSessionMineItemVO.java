package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 我的做饭会话摘要。
 */
@Data
@NoArgsConstructor
public class CookSessionMineItemVO {
    private Long id;
    private Long templateId;
    private String templateName;
    private LocalDateTime startedAt;
    private Integer status;
}
