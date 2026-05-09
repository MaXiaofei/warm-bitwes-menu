package com.warmbitwes.menu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话-菜品关联（session_dish），用于自选菜品创建会话。
 */
@Data
@NoArgsConstructor
public class SessionDish {
    private Long sessionId;
    private Long dishId;
    private Integer sortOrder;
}
