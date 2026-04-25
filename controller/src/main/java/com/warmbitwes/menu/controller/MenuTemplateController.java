package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.dto.MenuTemplateCreateReq;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.service.MenuTemplateService;
import com.warmbitwes.menu.vo.MenuTemplateDetailVO;
import com.warmbitwes.menu.vo.MenuTemplateIngredientSummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单模板接口（V1）。
 */
@Tag(name = "菜单模板")
@RestController
@RequestMapping("/api/menu-templates")
public class MenuTemplateController {

    private final MenuTemplateService menuTemplateService;

    public MenuTemplateController(MenuTemplateService menuTemplateService) {
        this.menuTemplateService = menuTemplateService;
    }

    /**
     * 新增菜单模板。
     *
     * @param req 创建请求
     * @return 模板ID
     */
    @Operation(summary = "新增菜单模板", description = "创建菜单模板并绑定菜品列表。")
    @PostMapping
    public ApiResponse<IdResp> create(@RequestBody @Valid MenuTemplateCreateReq req) {
        Long id = menuTemplateService.create(req.getName(), req.getTemplateType(), req.getDishIds());
        return ApiResponse.success(new IdResp(id));
    }

    /**
     * 菜单模板分页查询。
     *
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小（最大100）
     * @return 模板列表
     */
    @Operation(summary = "分页查询菜单模板", description = "按页码和每页大小查询菜单模板列表。")
    @GetMapping
    public ApiResponse<List<MenuTemplate>> page(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.success(menuTemplateService.listPage(pageNum, pageSize));
    }

    /**
     * 查询菜单模板详情。
     *
     * @param id 模板ID
     * @return 模板详情
     */
    @Operation(summary = "查询菜单模板详情", description = "根据模板ID查询模板基础信息及关联菜品ID列表。")
    @GetMapping("/{id}")
    public ApiResponse<MenuTemplateDetailVO> getDetail(@Parameter(description = "模板ID", required = true)
                                                       @PathVariable("id") Long id) {
        MenuTemplate template = menuTemplateService.getById(id);
        MenuTemplateDetailVO vo = new MenuTemplateDetailVO();
        vo.setId(template.getId());
        vo.setName(template.getName());
        vo.setTemplateType(template.getTemplateType());
        vo.setScene(template.getScene());
        vo.setFlavor(template.getFlavor());
        vo.setCrowd(template.getCrowd());
        vo.setDescription(template.getDescription());
        vo.setStatus(template.getStatus());
        vo.setRemark(template.getRemark());
        vo.setCreatedAt(template.getCreatedAt());
        vo.setUpdatedAt(template.getUpdatedAt());
        vo.setDishIds(menuTemplateService.getDishIds(id));
        return ApiResponse.success(vo);
    }

    /**
     * 查询模板食材汇总。
     *
     * @param id 模板ID
     * @return 汇总列表
     */
    @Operation(summary = "查询模板食材汇总", description = "按模板聚合食材总量，返回食材、单位和总克重。")
    @GetMapping("/{id}/ingredient-summary")
    public ApiResponse<List<MenuTemplateIngredientSummaryVO>> ingredientSummary(
            @Parameter(description = "模板ID", required = true) @PathVariable("id") Long id) {
        return ApiResponse.success(menuTemplateService.getIngredientSummary(id));
    }
}

