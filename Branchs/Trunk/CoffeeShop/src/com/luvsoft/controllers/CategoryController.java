package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Category;
import com.luvsoft.facades.CategoryFacade;

/**
 * 
 * @author datnq.55
 *
 */

public class CategoryController extends AbstractController{
    private static CategoryFacade categoryFacade = new CategoryFacade();

    /**
     * Get all category from database
     * @return list of category
     */
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<Category>();
        categoryFacade.findAll(list);
        return list;
    }

    /**
     * Get category by its Id
     * @return Category
     */
    public Category getCategoryById(String categoryId){
        Category cate = new Category();
        categoryFacade.findById(categoryId, cate);
        return cate;
    }
    
    /**
     * Remove category by id
     * @param categoryId
     * @return
     */
    public boolean removeCategory(String categoryId){
        return categoryFacade.removeById(categoryId);
    }
    
    /**
     * Add new category
     */
    public boolean addNewCategory(Category cat){
        return categoryFacade.save(cat);
    }

    /**
     * Update category
     */
    public boolean updateCategory(String cateId, Category cate){
        return categoryFacade.update(cateId, cate);
    }
}
