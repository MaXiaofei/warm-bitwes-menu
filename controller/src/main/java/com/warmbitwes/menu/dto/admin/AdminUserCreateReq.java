package com.warmbitwes.menu.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "管理端新增账号请求")
public class AdminUserCreateReq {
    @NotBlank(message = "username不能为空")
    @Schema(description = "登录名", example = "ops")
    private String username;

    @Schema(description = "昵称", example = "运营")
    private String nickname;

    @Schema(description = "手机号", example = "13800000000")
    private String phone;

    @Email(message = "email格式不正确")
    @Schema(description = "邮箱", example = "ops@example.com")
    private String email;

    @NotBlank(message = "password不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;

    @NotEmpty(message = "roleIds不能为空")
    @Schema(description = "角色ID列表", example = "[1]")
    private List<Long> roleIds;
}
