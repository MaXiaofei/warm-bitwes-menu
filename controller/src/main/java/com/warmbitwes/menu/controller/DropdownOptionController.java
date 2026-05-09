package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.DropdownOptionService;
import com.warmbitwes.menu.vo.DropdownOptionItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下拉选项查询（管理与小程序共用，需登录）。
 */
@Tag(name = "下拉选项配置")
@RestController
@RequestMapping("/api/dropdown-options")
public class DropdownOptionController {

    private final DropdownOptionService dropdownOptionService;

    public DropdownOptionController(DropdownOptionService dropdownOptionService) {
        this.dropdownOptionService = dropdownOptionService;
    }

    /**
     * 返回当前存在「已启用项」的全部分类编码，便于前端拼装路由或缓存。
     *
     * @return 分类编码列表（如 MENU_TEMPLATE_TYPE）
     */
    @Operation(summary = "下拉分类编码列表", description = "列出至少有一条启用选项的分类编码（字母序）。")
    @GetMapping("/categories")
    @RequirePermission("dropdown-option:list")
    public ApiResponse<List<String>> categories() {
        return ApiResponse.success(dropdownOptionService.listCategories());
    }

    /**
     * 按分类返回已启用选项（code ↔ label）。
     *
     * @param category 分类编码，必填，如 MENU_TEMPLATE_TYPE、DISH_STATUS、CUISINE
     * @return 选项列表（按 sortOrder 升序）
     */
    @Operation(summary = "按分类查询下拉项", description = "返回已启用选项：code 为存储/绑定值，label 为展示文案。")
    @GetMapping
    @RequirePermission("dropdown-option:list")
    public ApiResponse<List<DropdownOptionItemVO>> listByCategory(
            @Parameter(description = "分类编码，如 MENU_TEMPLATE_TYPE", required = true)
            @RequestParam("category") String category) {
        return ApiResponse.success(dropdownOptionService.listEnabledItems(category));
    }
}
