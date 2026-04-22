package com.warmbitwes.menu.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ingredient {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal caloriesKcalPer100g;
    private BigDecimal giValue;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

