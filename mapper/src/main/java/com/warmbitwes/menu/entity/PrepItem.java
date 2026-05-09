package com.warmbitwes.menu.entity;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 备菜项实体（prep_item）。
 */
@Data
@NoArgsConstructor
public class PrepItem {
    private Long sessionId;
    private Long ingredientId;
    private BigDecimal planAmountG;
    private Integer status;
    private Integer isShortage;
}

