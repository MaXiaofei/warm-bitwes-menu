package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientCreateReq {

    @NotBlank(message = "name不能为空")
    private String name;

    @NotBlank(message = "unit不能为空")
    private String unit;

    @NotNull(message = "caloriesKcalPer100g不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "caloriesKcalPer100g不能小于0")
    private BigDecimal caloriesKcalPer100g;

    @NotNull(message = "giValue不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "giValue不能小于0")
    private BigDecimal giValue;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}
