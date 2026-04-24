package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.dto.MenuTemplateCreateReq;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.service.MenuTemplateService;
import com.warmbitwes.menu.vo.MenuTemplateDetailVO;
import com.warmbitwes.menu.vo.MenuTemplateIngredientSummaryVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单模板接口（V1）。
 */
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
    @PostMapping
    public ApiResponse<IdResp> create(@RequestBody @Valid MenuTemplateCreateReq req) {
        Long id = menuTemplateService.create(req.getName(), req.getTemplateType(), req.getDishIds());
        return ApiResponse.success(new IdResp(id));
    }

    /**
     * 查询菜单模板详情。
     *
     * @param id 模板ID
     * @return 模板详情
     */
    @GetMapping("/{id}")
    public ApiResponse<MenuTemplateDetailVO> getDetail(@PathVariable("id") Long id) {
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
    @GetMapping("/{id}/ingredient-summary")
    public ApiResponse<List<MenuTemplateIngredientSummaryVO>> ingredientSummary(@PathVariable("id") Long id) {
        return ApiResponse.success(menuTemplateService.getIngredientSummary(id));
    }
}

