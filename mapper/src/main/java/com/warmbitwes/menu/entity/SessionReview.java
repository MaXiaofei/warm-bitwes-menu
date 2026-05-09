package com.warmbitwes.menu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话点评实体（session_review）。
 */
@Data
@NoArgsConstructor
public class SessionReview {
    private Long sessionId;
    private Integer tasteScore;
}

