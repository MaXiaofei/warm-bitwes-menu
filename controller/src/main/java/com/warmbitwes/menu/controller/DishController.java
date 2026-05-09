package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.DishCreateReq;
import com.warmbitwes.menu.dto.DishIngredientReplaceReq;
import com.warmbitwes.menu.dto.DishStatusUpdateReq;
import com.warmbitwes.menu.dto.DishUpdateReq;
import com.warmbitwes.menu.entity.Dish;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.DishIngredient;
import com.warmbitwes.menu.service.DishService;
import com.warmbitwes.menu.vo.DishDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "菜品管理")
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
    @Operation(summary = "查询菜品食材关联", description = "返回当前菜品下食材用量列表。")
    @GetMapping("/{id}/ingredients")
    public ApiResponse<List<DishIngredient>> listIngredients(@PathVariable("id") Long id) {
        return ApiResponse.success(dishService.listIngredientsByDishId(id));
    }

    @Operation(summary = "查询菜品详情", description = "根据菜品ID查询菜品详情及分类信息。")
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
    @Operation(summary = "新增菜品", description = "创建菜品并返回新建菜品的基础信息。")
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
    @Operation(summary = "分页查询菜品", description = "按页码和每页大小查询菜品列表。")
    @GetMapping
    public ApiResponse<List<Dish>> page(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "status", required = false) Integer status,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.success(dishService.listPage(name, status, pageNum, pageSize));
    }

    /**
     * 更新菜品基础信息（过滤软删）。
     *
     * @param id 菜品ID
     * @param req 更新请求
     * @return success
     */
    @Operation(summary = "更新菜品基础信息", description = "更新菜品基础字段（name/时长/难度/备注/封面/步骤/笔记/状态等），过滤软删。")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable("id") Long id,
                                    @RequestBody @Valid DishUpdateReq req) {
        dishService.updateBase(id, req);
        return ApiResponse.success();
    }

    /**
     * 更新菜品上下架状态（过滤软删）。
     *
     * @param id 菜品ID
     * @param req 状态更新请求
     * @return success
     */
    @Operation(summary = "更新菜品上下架状态", description = "设置菜品 status：1上架 0下架，过滤软删。")
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable("id") Long id,
                                          @RequestBody @Valid DishStatusUpdateReq req) {
        dishService.updateStatus(id, req.getStatus());
        return ApiResponse.success();
    }

    /**
     * 软删除菜品（is_deleted=1）。
     *
     * @param id 菜品ID
     * @return success
     */
    @Operation(summary = "软删除菜品", description = "将菜品标记为已删除（is_deleted=1），列表与详情查询将过滤。")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        dishService.softDelete(id);
        return ApiResponse.success();
    }

    /**
     * 全量替换菜品食材关联。
     *
     * @param id 菜品ID
     * @param req 替换请求
     * @return success
     */
    @Operation(summary = "替换菜品食材关联", description = "全量替换指定菜品的食材关联关系。")
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
