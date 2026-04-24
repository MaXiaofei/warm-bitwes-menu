package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishCreateReq {

    @NotBlank(message = "name不能为空")
    @Size(max = 120, message = "name长度不能超过120")
    private String name;

    @NotNull(message = "durationMin不能为空")
    @Min(value = 1, message = "durationMin必须大于等于1")
    private Integer durationMin;

    @NotNull(message = "difficulty不能为空")
    @Min(value = 1, message = "difficulty必须在1-5之间")
    @Max(value = 5, message = "difficulty必须在1-5之间")
    private Integer difficulty;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}

