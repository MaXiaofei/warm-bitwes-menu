package com.warmbitwes.menu.vo;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板食材汇总视图。
 */
@Data
@NoArgsConstructor
public class MenuTemplateIngredientSummaryVO {
    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal totalAmountG;
}
