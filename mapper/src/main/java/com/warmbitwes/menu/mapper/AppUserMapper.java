package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppUserMapper {
    AppUser selectByUsername(@Param("username") String username);
    Long selectAnyUserId();
    Long selectIdByUsername(@Param("username") String username);
    int insertDevUser(@Param("username") String username);
}

