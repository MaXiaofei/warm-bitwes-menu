package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCaloriesKcalPer100g() {
        return caloriesKcalPer100g;
    }

    public void setCaloriesKcalPer100g(BigDecimal caloriesKcalPer100g) {
        this.caloriesKcalPer100g = caloriesKcalPer100g;
    }

    public BigDecimal getGiValue() {
        return giValue;
    }

    public void setGiValue(BigDecimal giValue) {
        this.giValue = giValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

