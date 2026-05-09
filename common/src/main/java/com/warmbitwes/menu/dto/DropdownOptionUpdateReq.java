package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉选项更新请求（管理端）。
 */
@Data
@NoArgsConstructor
public class DropdownOptionUpdateReq {

    @NotBlank(message = "category不能为空")
    @Size(max = 64, message = "category长度不能超过64")
    private String category;

    @NotBlank(message = "optionCode不能为空")
    @Size(max = 64, message = "optionCode长度不能超过64")
    private String optionCode;

    @NotBlank(message = "optionLabel不能为空")
    @Size(max = 128, message = "optionLabel长度不能超过128")
    private String optionLabel;

    @NotNull(message = "sortOrder不能为空")
    @Min(value = 0, message = "sortOrder不能小于0")
    private Integer sortOrder;

    @NotNull(message = "enabled不能为空")
    @Min(value = 0, message = "enabled必须是0或1")
    @Max(value = 1, message = "enabled必须是0或1")
    private Integer enabled;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}
