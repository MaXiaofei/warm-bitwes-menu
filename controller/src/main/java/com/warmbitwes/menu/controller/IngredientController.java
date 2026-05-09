package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.dto.IngredientCreateReq;
import com.warmbitwes.menu.dto.IngredientUpdateReq;
import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 食材接口（V1）。
 */
@Tag(name = "食材管理")
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * 食材列表查询：未传分页参数则返回全量；传入分页参数则返回分页列表。
     *
     * @param pageNum 页码（从1开始，可选）
     * @param pageSize 每页大小（最大100，可选）
     * @return 食材列表
     */
    @Operation(summary = "查询食材列表", description = "未传分页参数返回全量列表，传入分页参数返回分页结果。")
    @GetMapping
    public ApiResponse<List<Ingredient>> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return ApiResponse.success(ingredientService.listAll());
        }
        int safePageNum = Math.max(1, pageNum);
        int safePageSize = Math.min(100, Math.max(1, pageSize));
        int offset = (safePageNum - 1) * safePageSize;
        return ApiResponse.success(ingredientService.listPage(offset, safePageSize));
    }

    /**
     * 按 ID 查询食材详情。
     *
     * @param id 食材ID
     * @return 食材详情
     */
    @Operation(summary = "查询食材详情", description = "根据食材ID查询单个食材详情。")
    @GetMapping("/{id}")
    public ApiResponse<Ingredient> getById(@PathVariable("id") Long id) {
        return ApiResponse.success(ingredientService.getById(id));
    }

    /**
     * 新增食材（返回新建 id）。
     *
     * @param req 创建请求
     * @return id
     */
    @Operation(summary = "新增食材", description = "创建食材并返回主键ID。")
    @PostMapping
    public ApiResponse<IdResp> create(@RequestBody @Valid IngredientCreateReq req) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(req.getName());
        ingredient.setUnit(req.getUnit());
        ingredient.setCaloriesKcalPer100g(req.getCaloriesKcalPer100g());
        ingredient.setGiValue(req.getGiValue());
        ingredient.setRemark(req.getRemark());

        Long id = ingredientService.createAndReturnId(ingredient);
        return ApiResponse.success(new IdResp(id));
    }

    /**
     * 按 ID 更新食材。
     *
     * @param id 食材ID
     * @param req 更新请求
     * @return success
     */
    @Operation(summary = "更新食材", description = "根据食材ID更新食材信息。")
    @PutMapping("/{id}")
    public ApiResponse<Void> updateById(@PathVariable("id") Long id,
                                        @RequestBody @Valid IngredientUpdateReq req) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(req.getName());
        ingredient.setUnit(req.getUnit());
        ingredient.setCaloriesKcalPer100g(req.getCaloriesKcalPer100g());
        ingredient.setGiValue(req.getGiValue());
        ingredient.setRemark(req.getRemark());
        ingredientService.updateById(id, ingredient);
        return ApiResponse.success();
    }

    /**
     * 按 ID 删除食材。
     *
     * @param id 食材ID
     * @return success
     */
    @Operation(summary = "删除食材", description = "根据食材ID删除食材。")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable("id") Long id) {
        ingredientService.deleteById(id);
        return ApiResponse.success();
    }
}
