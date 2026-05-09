package com.warmbitwes.menu.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "管理端更新账号请求")
public class AdminUserUpdateReq {
    @Schema(description = "昵称", example = "运营")
    private String nickname;

    @Schema(description = "手机号", example = "13800000000")
    private String phone;

    @Email(message = "email格式不正确")
    @Schema(description = "邮箱", example = "ops@example.com")
    private String email;

    @Schema(description = "状态：1启用 0停用", example = "1")
    private Integer status;

    @NotEmpty(message = "roleIds不能为空")
    @Schema(description = "角色ID列表", example = "[1]")
    private List<Long> roleIds;
}
