package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 烹饪过程事件（cook_event）。
 */
@Data
@NoArgsConstructor
public class CookEvent {
    private Long id;
    private Long sessionId;
    private String eventType;
    private LocalDateTime eventTime;
    private String content;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
