package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "口味评分请求")
public class ReviewTasteReq {
    @Schema(description = "口味评分（1-5）", example = "4")
    @NotNull(message = "score不能为空")
    @Min(value = 1, message = "score必须在1-5之间")
    @Max(value = 5, message = "score必须在1-5之间")
    private Integer score;
}

