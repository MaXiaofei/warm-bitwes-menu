package com.warmbitwes.menu.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "角色绑定权限请求")
public class RolePermissionBindReq {
    @NotEmpty(message = "permissionIds不能为空")
    @Schema(description = "权限点ID列表", example = "[1,2]")
    private List<Long> permissionIds;
}
