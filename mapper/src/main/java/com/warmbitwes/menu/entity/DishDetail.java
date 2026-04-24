package com.warmbitwes.menu.entity;

import com.warmbitwes.menu.vo.DishCategoryLinkVO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DishDetail extends Dish {
    private List<DishCategoryLinkVO> categories = new ArrayList<>();
}
