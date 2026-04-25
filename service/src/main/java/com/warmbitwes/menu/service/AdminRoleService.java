package com.warmbitwes.menu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleService {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, String> roleStore = new HashMap<>();
    private final Map<Long, List<Long>> rolePermissionStore = new HashMap<>();
    private final Map<Long, List<String>> roleScopeStore = new HashMap<>();

    public Long create(String name) {
        long id = idGenerator.getAndIncrement();
        roleStore.put(id, name);
        return id;
    }

    public void bindPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionStore.put(roleId, permissionIds);
    }

    public void updateScope(Long roleId, List<String> clientTypes) {
        roleScopeStore.put(roleId, clientTypes);
    }

    public List<AdminRoleItem> list() {
        return roleStore.entrySet().stream()
                .map(entry -> new AdminRoleItem(entry.getKey(), entry.getValue(), "系统角色"))
                .toList();
    }

    public List<String> getScopes(Long roleId) {
        return roleScopeStore.getOrDefault(roleId, List.of("admin"));
    }

    public record AdminRoleItem(Long id, String name, String remark) {
    }
}
