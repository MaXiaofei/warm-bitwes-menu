package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话复盘 VO。
 */
@Data
@NoArgsConstructor
public class SessionRetrospectiveVO {
    private Long id;
    private Long sessionId;
    private String summary;
    private String improvement;
    private String retryAdvice;
    private String remark;
    private LocalDateTime updatedAt;
}
