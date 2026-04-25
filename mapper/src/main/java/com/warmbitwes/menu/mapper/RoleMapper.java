package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper {
    Role selectById(@Param("id") Long id);

    List<Role> selectByUserId(@Param("userId") Long userId);
}
