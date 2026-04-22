package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.DishMapper;
import com.warmbitwes.menu.vo.DishDetailVO;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    private final DishMapper dishMapper;

    public DishService(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    public DishDetailVO getDetailById(Long id) {
        DishDetail dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BizException(40402, "菜品不存在，id=" + id);
        }
        DishDetailVO vo = new DishDetailVO();
        vo.setId(dish.getId());
        vo.setName(dish.getName());
        vo.setCoverUrl(dish.getCoverUrl());
        vo.setSteps(dish.getSteps());
        vo.setNotes(dish.getNotes());
        vo.setDurationMin(dish.getDurationMin());
        vo.setDifficulty(dish.getDifficulty());
        vo.setStatus(dish.getStatus());
        vo.setRemark(dish.getRemark());
        vo.setCreatedAt(dish.getCreatedAt());
        vo.setUpdatedAt(dish.getUpdatedAt());
        vo.setCategories(dish.getCategories());
        return vo;
    }
}
