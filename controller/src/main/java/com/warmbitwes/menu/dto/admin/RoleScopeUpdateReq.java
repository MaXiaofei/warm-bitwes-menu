package com.warmbitwes.menu.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "角色端范围更新请求")
public class RoleScopeUpdateReq {
    @NotEmpty(message = "clientTypes不能为空")
    @Schema(description = "端范围列表", example = "[\"admin\",\"mini\"]")
    private List<String> clientTypes;
}
