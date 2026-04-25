package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.dto.admin.AdminUserCreateReq;
import com.warmbitwes.menu.dto.admin.AdminUserUpdateReq;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.AdminUserService;
import com.warmbitwes.menu.service.AdminUserService.AdminUserItem;
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

@Tag(name = "管理端-账号管理")
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Operation(summary = "账号列表", description = "查询管理端账号列表。")
    @GetMapping
    @RequirePermission("auth-user:list")
    public ApiResponse<List<AdminUserItem>> list() {
        return ApiResponse.success(adminUserService.list());
    }

    @Operation(summary = "新增账号", description = "新增管理端账号。")
    @PostMapping
    @RequirePermission("auth-user:create")
    public ApiResponse<IdResp> create(@RequestBody @Valid AdminUserCreateReq req) {
        Long id = adminUserService.create(req.getUsername(), req.getNickname(), req.getPhone(), req.getEmail(), req.getRoleIds());
        return ApiResponse.success(new IdResp(id));
    }

    @Operation(summary = "更新账号", description = "更新管理端账号。")
    @PutMapping("/{id}")
    @RequirePermission("auth-user:update")
    public ApiResponse<Void> update(@PathVariable("id") Long id, @RequestBody @Valid AdminUserUpdateReq req) {
        adminUserService.update(id, req.getNickname(), req.getPhone(), req.getEmail(), req.getStatus(), req.getRoleIds());
        return ApiResponse.success();
    }

    @Operation(summary = "重置账号密码", description = "将管理端账号密码重置为默认密码。")
    @PostMapping("/{id}/reset-password")
    @RequirePermission("auth-user:reset-password")
    public ApiResponse<Void> resetPassword(@PathVariable("id") Long id) {
        adminUserService.resetPassword(id);
        return ApiResponse.success();
    }
}
