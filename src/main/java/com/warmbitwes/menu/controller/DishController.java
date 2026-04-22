package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.service.DishService;
import com.warmbitwes.menu.vo.DishDetailVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/{id}")
    public ApiResponse<DishDetailVO> getDetail(@PathVariable("id") Long id) {
        return ApiResponse.success(dishService.getDetailById(id));
    }
}
