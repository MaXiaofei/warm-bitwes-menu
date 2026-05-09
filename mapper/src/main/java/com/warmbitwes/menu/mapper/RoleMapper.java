package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper {
    int insert(Role role);

    int updateById(Role role);

    Role selectById(@Param("id") Long id);

    List<Role> selectAll();

    List<Role> selectByUserId(@Param("userId") Long userId);

    int deleteRolePermissions(@Param("roleId") Long roleId);

    int batchInsertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    List<String> selectClientTypesByRoleId(@Param("roleId") Long roleId);

    int deleteRoleScopes(@Param("roleId") Long roleId);

    int insertRoleScope(@Param("roleId") Long roleId, @Param("clientType") String clientType);
}
