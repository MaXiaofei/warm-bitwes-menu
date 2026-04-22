package com.warmbitwes.menu.entity;

import com.warmbitwes.menu.vo.DishCategoryLinkVO;

import java.util.ArrayList;
import java.util.List;

public class DishDetail extends Dish {
    private List<DishCategoryLinkVO> categories = new ArrayList<>();

    public List<DishCategoryLinkVO> getCategories() {
        return categories;
    }

    public void setCategories(List<DishCategoryLinkVO> categories) {
        this.categories = categories;
    }
}
