package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 做饭会话实体（cooking_session）。
 */
@Data
@NoArgsConstructor
public class CookingSession {
    private Long id;
    private Long userId;
    private Long templateId;
    private String scene;
    private String flavor;
    private String crowd;
    private Integer status;
    private LocalDateTime startedAt;
}

