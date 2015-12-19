package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.luvsoft.facades.CategoryFacade;
import com.luvsoft.facades.FoodFacade;

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
        if( CachedData.categoryList == null ){
            List<Category> list = new ArrayList<Category>();
            categoryFacade.findAll(list);
            CachedData.categoryList = list;
        }
        return CachedData.categoryList;
    }

    public void initFoodList(Category cate){
        List<Food> foodList = new ArrayList<Food>();
        if( cate != null){
            FoodFacade foodFacade = new FoodFacade();
            for( String foodId : cate.getFoodIdList() ){
                Food food = new Food();
                if( foodFacade.findById(foodId, food)){
                    foodList.add(food);
                }
            }
            if( CachedData.categoriesMaps.get(cate.getId()) != null){
                CachedData.categoriesMaps.replace(cate.getId(), foodList);
            }
            else{
                CachedData.categoriesMaps.put(cate.getId(), foodList);
            }
        }
        
    }

    public Food getFood(String catId, int index){
        return CachedData.categoriesMaps.get(catId).get(index);
    }

    public List<Food> getFoodListOfCategory(String catId){
        return CachedData.categoriesMaps.get(catId);
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
    public boolean updateCategory(Category cate){
        return categoryFacade.update(cate.getId(), cate);
    }
}
