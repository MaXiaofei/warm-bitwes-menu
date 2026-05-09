package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话复盘（session_retrospective）。
 */
@Data
@NoArgsConstructor
public class SessionRetrospective {
    private Long id;
    private Long sessionId;
    private String summary;
    private String improvement;
    private String retryAdvice;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
