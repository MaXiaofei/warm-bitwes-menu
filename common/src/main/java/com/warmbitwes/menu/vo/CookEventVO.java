package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 烹饪事件 VO。
 */
@Data
@NoArgsConstructor
public class CookEventVO {
    private Long id;
    private Long sessionId;
    private String eventType;
    private LocalDateTime eventTime;
    private String content;
    private String remark;
    private LocalDateTime createdAt;
}
