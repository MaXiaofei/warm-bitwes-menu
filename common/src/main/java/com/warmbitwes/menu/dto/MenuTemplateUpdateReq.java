package com.warmbitwes.menu.dto;

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
 * 菜单模板更新请求。
 */
@Data
@NoArgsConstructor
public class MenuTemplateUpdateReq {

    @NotBlank(message = "name不能为空")
    @Size(max = 120, message = "name长度不能超过120")
    private String name;

    @NotNull(message = "templateType不能为空")
    @Min(value = 1, message = "templateType必须在1-4之间")
    @Max(value = 4, message = "templateType必须在1-4之间")
    private Integer templateType;

    @Size(max = 64, message = "scene长度不能超过64")
    private String scene;

    @Size(max = 64, message = "flavor长度不能超过64")
    private String flavor;

    @Size(max = 64, message = "crowd长度不能超过64")
    private String crowd;

    @Size(max = 500, message = "description长度不能超过500")
    private String description;

    @NotNull(message = "status不能为空")
    @Min(value = 0, message = "status必须是0或1")
    @Max(value = 1, message = "status必须是0或1")
    private Integer status;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;

    @NotEmpty(message = "dishIds不能为空")
    private List<Long> dishIds;
}
