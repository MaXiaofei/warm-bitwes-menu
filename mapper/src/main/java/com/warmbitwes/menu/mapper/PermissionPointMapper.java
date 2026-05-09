package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.PermissionPoint;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionPointMapper {
    int insert(PermissionPoint entity);

    PermissionPoint selectByCode(@Param("code") String code);

    List<PermissionPoint> selectAll();
}
