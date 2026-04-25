package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品更新请求（基础字段）。
 */
@Data
@NoArgsConstructor
public class DishUpdateReq {

    @Size(max = 120, message = "name长度不能超过120")
    private String name;

    @Size(max = 255, message = "coverUrl长度不能超过255")
    private String coverUrl;

    private String steps;

    private String notes;

    @Min(value = 1, message = "durationMin必须大于等于1")
    private Integer durationMin;

    @Min(value = 1, message = "difficulty必须在1-5之间")
    @Max(value = 5, message = "difficulty必须在1-5之间")
    private Integer difficulty;

    @Min(value = 0, message = "status必须是0或1")
    @Max(value = 1, message = "status必须是0或1")
    private Integer status;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}

