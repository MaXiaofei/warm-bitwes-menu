package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.admin.RoleScopeUpdateReq;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.AdminRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理端-端范围管理")
@RestController
@RequestMapping("/api/admin/scopes")
public class AdminScopeController {
    private final AdminRoleService adminRoleService;

    public AdminScopeController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @Operation(summary = "查询角色端范围", description = "按角色查询可访问端范围。")
    @GetMapping("/{roleId}")
    @RequirePermission("auth-scope:list")
    public ApiResponse<List<String>> getRoleScope(@PathVariable("roleId") Long roleId) {
        return ApiResponse.success(adminRoleService.getScopes(roleId));
    }

    @Operation(summary = "更新角色端范围", description = "更新角色可访问的 clientType 列表。")
    @PutMapping("/roles/{roleId}")
    @RequirePermission("auth-scope:update")
    public ApiResponse<Void> updateRoleScope(@PathVariable("roleId") Long roleId, @RequestBody @Valid RoleScopeUpdateReq req) {
        adminRoleService.updateScope(roleId, req.getClientTypes());
        return ApiResponse.success();
    }
}
