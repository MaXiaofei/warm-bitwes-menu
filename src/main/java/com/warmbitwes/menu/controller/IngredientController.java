package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.IngredientCreateReq;
import com.warmbitwes.menu.dto.IngredientUpdateReq;
import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ApiResponse<List<Ingredient>> listAll() {
        return ApiResponse.success(ingredientService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Ingredient> getById(@PathVariable("id") Long id) {
        return ApiResponse.success(ingredientService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid IngredientCreateReq req) {
        ingredientService.create(req);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateById(@PathVariable("id") Long id,
                                          @RequestBody @Valid IngredientUpdateReq req) {
        ingredientService.updateById(id, req);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable("id") Long id) {
        ingredientService.deleteById(id);
        return ApiResponse.success();
    }
}

