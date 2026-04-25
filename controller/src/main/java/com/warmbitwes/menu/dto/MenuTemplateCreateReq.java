package com.warmbitwes.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单模板创建请求。
 */
@Data
@NoArgsConstructor
@Schema(description = "菜单模板创建请求")
public class MenuTemplateCreateReq {
    @Schema(description = "模板名称", example = "工作日晚餐模板")
    @NotBlank(message = "name不能为空")
    @Size(max = 120, message = "name长度不能超过120")
    private String name;

    @Schema(description = "模板类型（1-4）", example = "1")
    @NotNull(message = "templateType不能为空")
    @Min(value = 1, message = "templateType必须在1-4之间")
    @Max(value = 4, message = "templateType必须在1-4之间")
    private Integer templateType;

    @Schema(description = "关联菜品ID列表", example = "[1001,1002]")
    @NotEmpty(message = "dishIds不能为空")
    private List<Long> dishIds;
}

