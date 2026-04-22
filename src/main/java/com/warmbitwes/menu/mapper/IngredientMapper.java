package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Ingredient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IngredientMapper {

    List<Ingredient> selectAll();

    Ingredient selectById(@Param("id") Long id);

    int insert(Ingredient ingredient);

    int updateById(Ingredient ingredient);

    int deleteById(@Param("id") Long id);
}

