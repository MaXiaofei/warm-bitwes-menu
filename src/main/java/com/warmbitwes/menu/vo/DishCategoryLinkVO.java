package com.warmbitwes.menu.vo;

/**
 * 菜品与分类的关联视角：包含关联上的排序与备注。
 */
public class DishCategoryLinkVO {
    private Long categoryId;
    private String categoryName;
    private Integer sortOrder;
    private String remark;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
