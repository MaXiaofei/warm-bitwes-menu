package com.warmbitwes.menu.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "管理端新增角色请求")
public class AdminRoleCreateReq {
    @NotBlank(message = "name不能为空")
    @Schema(description = "角色名称", example = "运营")
    private String name;

    @Schema(description = "备注", example = "管理后台运营角色")
    private String remark;
}
