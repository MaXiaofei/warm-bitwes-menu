package com.warmbitwes.menu.vo;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 备菜项视图。
 */
@Data
@NoArgsConstructor
public class PrepItemVO {
    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal planAmountG;
    private Integer status;
    private Integer isShortage;
}
