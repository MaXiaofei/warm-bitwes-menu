package com.warmbitwes.menu.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminPermissionService {
    public List<PermissionPointItem> listAll() {
        return List.of(
                new PermissionPointItem(1L, "auth-user:list", "账号列表"),
                new PermissionPointItem(2L, "auth-user:create", "新增账号"),
                new PermissionPointItem(3L, "auth-role:list", "角色列表"),
                new PermissionPointItem(4L, "auth-role:grant", "角色授权"),
                new PermissionPointItem(5L, "auth-permission:list", "权限点列表"),
                new PermissionPointItem(6L, "auth-scope:list", "端范围列表"),
                new PermissionPointItem(7L, "dish:list", "菜品列表"),
                new PermissionPointItem(8L, "ingredient:list", "食材列表")
        );
    }

    public record PermissionPointItem(Long id, String code, String name) {
    }
}
