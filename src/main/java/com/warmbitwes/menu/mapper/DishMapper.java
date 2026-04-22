package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.DishDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DishMapper {

    DishDetail selectById(@Param("id") Long id);
}
