package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.Role;
import com.warmbitwes.menu.mapper.RoleMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleService {
    private final RoleMapper roleMapper;

    public AdminRoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public Long create(String name, String remark) {
        Role role = new Role();
        role.setName(name);
        role.setRemark(remark);
        roleMapper.insert(role);
        return role.getId();
    }

    public void bindPermissions(Long roleId, List<Long> permissionIds) {
        roleMapper.deleteRolePermissions(roleId);
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.batchInsertRolePermissions(roleId, permissionIds);
        }
    }

    public void updateScope(Long roleId, List<String> clientTypes) {
        roleMapper.deleteRoleScopes(roleId);
        if (clientTypes != null && !clientTypes.isEmpty()) {
            for (String clientType : clientTypes) {
                roleMapper.insertRoleScope(roleId, clientType);
            }
        }
    }

    public List<AdminRoleItem> list() {
        return roleMapper.selectAll().stream()
                .map(item -> new AdminRoleItem(item.getId(), item.getName(), item.getRemark()))
                .toList();
    }

    public void update(Long id, String name, String remark) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setRemark(remark);
        roleMapper.updateById(role);
    }

    public List<String> getScopes(Long roleId) {
        List<String> scopes = roleMapper.selectClientTypesByRoleId(roleId);
        return scopes == null || scopes.isEmpty() ? List.of("admin") : scopes;
    }

    public record AdminRoleItem(Long id, String name, String remark) {
    }
}
