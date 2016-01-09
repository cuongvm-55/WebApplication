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

    public CategoryController() {
        setSUPMode(false);
    }
    /**
     * Get all category from database
     * @return list of category
     */
    public List<Category> getAllCategory() {
        if( isSUPMode() ){
            List<Category> list = new ArrayList<Category>();
            categoryFacade.findAll(list);
            return list;
        }

        return CachedData.categoryList;
    }

    public Food getFood(String catId, int index){
        if( CachedData.categoriesMaps.get(catId) != null){
            return CachedData.categoriesMaps.get(catId).get(index);
        }
        return null;
    }

    public List<Food> getFoodListOfCategory(String catId){
        if( isSUPMode() ){
            List<Food> foodList = new ArrayList<Food>();
            Category cat = new Category();
            if( categoryFacade.findById(catId, cat) ){
                FoodFacade foodFacade = new FoodFacade();
                for( String foodId : cat.getFoodIdList() ){
                    Food food = new Food();
                    if( foodFacade.findById(foodId, food)){
                        foodList.add(food);
                    }
                }
            }
            return foodList;
        }
        else{
            return CachedData.categoriesMaps.get(catId);
        }
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
        if( categoryFacade.removeById(categoryId) ){
            CachedData.removeCategory(categoryId);
            return true;
        }

        return false;
    }
    
    /**
     * Add new category
     */
    public boolean addNewCategory(Category cat){
        if( categoryFacade.save(cat) ){
            CachedData.addNewCategory(cat);
            return true;
        }
        return false;
    }

    /**
     * Update category
     */
    public boolean updateCategory(Category cate){
        if( categoryFacade.update(cate.getId(), cate) ){
            CachedData.updateCategory(cate);
            return true;
        }

        return false;
    }
}
