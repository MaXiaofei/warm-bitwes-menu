package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话点评视图。
 */
@Data
@NoArgsConstructor
public class SessionReviewVO {
    private Long reviewId;
    private Long sessionId;
    private Long dishId;
    private Integer tasteScore;
    private Integer difficultyScore;
    private String retryIntent;
    private String reviewNote;
    private LocalDateTime createdAt;
}

