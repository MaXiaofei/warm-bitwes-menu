package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品上下架状态更新请求。
 */
@Data
@NoArgsConstructor
public class DishStatusUpdateReq {

    @NotNull(message = "status不能为空")
    @Min(value = 0, message = "status必须是0或1")
    @Max(value = 1, message = "status必须是0或1")
    private Integer status;
}

