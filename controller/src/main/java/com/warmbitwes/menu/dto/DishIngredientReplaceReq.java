package com.warmbitwes.menu.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品食材关联全量替换请求。
 */
@Data
@NoArgsConstructor
public class DishIngredientReplaceReq {

    @NotEmpty(message = "items不能为空")
    @Valid
    private List<Item> items;

    /**
     * 单条关联项。
     */
    @Data
    @NoArgsConstructor
    public static class Item {
        @NotNull(message = "ingredientId不能为空")
        @Min(value = 1, message = "ingredientId必须大于0")
        private Long ingredientId;

        @NotNull(message = "amountG不能为空")
        @Min(value = 0, message = "amountG不能小于0")
        private BigDecimal amountG;

        @NotNull(message = "sortOrder不能为空")
        @Min(value = 0, message = "sortOrder不能小于0")
        private Integer sortOrder;
    }
}

