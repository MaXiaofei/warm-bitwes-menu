package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.dto.admin.AdminRoleCreateReq;
import com.warmbitwes.menu.dto.admin.RolePermissionBindReq;
import com.warmbitwes.menu.dto.admin.RoleScopeUpdateReq;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.AdminRoleService;
import com.warmbitwes.menu.service.AdminRoleService.AdminRoleItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理端-角色管理")
@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {
    private final AdminRoleService adminRoleService;

    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @Operation(summary = "角色列表", description = "查询管理端角色列表。")
    @GetMapping
    @RequirePermission("auth-role:list")
    public ApiResponse<List<AdminRoleItem>> list() {
        return ApiResponse.success(adminRoleService.list());
    }

    @Operation(summary = "新增角色", description = "新增管理端角色。")
    @PostMapping
    @RequirePermission("auth-role:create")
    public ApiResponse<IdResp> create(@RequestBody @Valid AdminRoleCreateReq req) {
        Long id = adminRoleService.create(req.getName());
        return ApiResponse.success(new IdResp(id));
    }

    @Operation(summary = "角色绑定权限", description = "为角色更新权限点集合。")
    @PutMapping("/{id}/permissions")
    @RequirePermission("auth-role:grant")
    public ApiResponse<Void> bindPermissions(@PathVariable("id") Long id, @RequestBody @Valid RolePermissionBindReq req) {
        adminRoleService.bindPermissions(id, req.getPermissionIds());
        return ApiResponse.success();
    }

    @Operation(summary = "角色端范围更新", description = "兼容前端路径，更新角色可访问端范围。")
    @PutMapping("/{id}/scopes")
    @RequirePermission("auth-scope:update")
    public ApiResponse<Void> updateScopes(@PathVariable("id") Long id, @RequestBody @Valid RoleScopeUpdateReq req) {
        adminRoleService.updateScope(id, req.getClientTypes());
        return ApiResponse.success();
    }
}
