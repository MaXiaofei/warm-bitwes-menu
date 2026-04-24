package com.warmbitwes.menu.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ingredient {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal caloriesKcalPer100g;
    private BigDecimal giValue;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
