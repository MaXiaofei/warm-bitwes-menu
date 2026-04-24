package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.DishCreateReq;
import com.warmbitwes.menu.dto.DishIngredientReplaceReq;
import com.warmbitwes.menu.entity.Dish;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.DishIngredient;
import com.warmbitwes.menu.service.DishService;
import com.warmbitwes.menu.vo.DishDetailVO;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜品接口（V1）。
 */
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 查询菜品详情。
     *
     * @param id 菜品ID
     * @return 菜品详情（含分类）
     */
    @GetMapping("/{id}")
    public ApiResponse<DishDetailVO> getDetail(@PathVariable("id") Long id) {
        DishDetail dish = dishService.getDetailById(id);
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
        return ApiResponse.success(vo);
    }

    /**
     * 新增菜品（V1 最小字段）。
     *
     * @param req 创建请求
     * @return id + name
     */
    @PostMapping
    public ApiResponse<Dish> create(@RequestBody @Valid DishCreateReq req) {
        Dish dish = new Dish();
        dish.setName(req.getName());
        dish.setDurationMin(req.getDurationMin());
        dish.setDifficulty(req.getDifficulty());
        dish.setRemark(req.getRemark());

        Long id = dishService.create(dish);

        Dish res = new Dish();
        res.setId(id);
        res.setName(req.getName());
        return ApiResponse.success(res);
    }

    /**
     * 菜品分页查询。
     *
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小（最大100）
     * @return 菜品列表
     */
    @GetMapping
    public ApiResponse<List<Dish>> page(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.success(dishService.listPage(pageNum, pageSize));
    }

    /**
     * 全量替换菜品食材关联。
     *
     * @param id 菜品ID
     * @param req 替换请求
     * @return success
     */
    @PutMapping("/{id}/ingredients")
    public ApiResponse<Void> replaceIngredients(@PathVariable("id") Long id,
                                                @RequestBody @Valid DishIngredientReplaceReq req) {
        List<DishIngredient> ingredients = new ArrayList<>();
        for (DishIngredientReplaceReq.Item item : req.getItems()) {
            DishIngredient di = new DishIngredient();
            di.setIngredientId(item.getIngredientId());
            di.setAmountG(item.getAmountG());
            di.setSortOrder(item.getSortOrder());
            ingredients.add(di);
        }
        dishService.replaceIngredients(id, ingredients);
        return ApiResponse.success();
    }
}
