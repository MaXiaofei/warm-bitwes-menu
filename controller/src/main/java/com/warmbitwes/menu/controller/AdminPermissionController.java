package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.AdminPermissionService;
import com.warmbitwes.menu.service.AdminPermissionService.PermissionPointItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理端-权限点管理")
@RestController
@RequestMapping("/api/admin/permissions")
public class AdminPermissionController {
    private final AdminPermissionService adminPermissionService;

    public AdminPermissionController(AdminPermissionService adminPermissionService) {
        this.adminPermissionService = adminPermissionService;
    }

    @Operation(summary = "权限点列表", description = "查询系统权限点编码列表。")
    @GetMapping
    @RequirePermission("auth-permission:list")
    public ApiResponse<List<PermissionPointItem>> list() {
        return ApiResponse.success(adminPermissionService.listAll());
    }
}
