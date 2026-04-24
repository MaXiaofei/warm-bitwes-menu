package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 口味点评请求。
 */
@Data
@NoArgsConstructor
public class ReviewTasteReq {
    @NotNull(message = "score不能为空")
    @Min(value = 1, message = "score必须在1-5之间")
    @Max(value = 5, message = "score必须在1-5之间")
    private Integer score;
}

