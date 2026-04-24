package com.warmbitwes.menu.entity;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品-食材关联实体（dish_ingredient）。
 */
@Data
@NoArgsConstructor
public class DishIngredient {
    private Long dishId;
    private Long ingredientId;
    private BigDecimal amountG;
    private Integer sortOrder;
}

