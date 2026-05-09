package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户会话列表查询行（含模板名快照）。
 */
@Data
@NoArgsConstructor
public class CookingSessionMineRow {
    private Long id;
    private Long templateId;
    private String templateName;
    private LocalDateTime startedAt;
    private Integer status;
}
