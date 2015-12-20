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
            System.out.println("Supmode...");
            List<Category> list = new ArrayList<Category>();
            categoryFacade.findAll(list);
            return list;
        }
        else{
            System.out.println("Operator...");
        }
        return CachedData.categoryList;
    }

    public List<Category> initCategoryList(){
        CachedData.categoryList.clear();
        CachedData.categoriesMaps.clear();
        categoryFacade.findAll(CachedData.categoryList);
        return CachedData.categoryList;
    }

    public void initFoodList(Category cate){
        if( cate != null){
            List<Food> foodList = new ArrayList<Food>();
            FoodFacade foodFacade = new FoodFacade();
            for( String foodId : cate.getFoodIdList() ){
                Food food = new Food();
                if( foodFacade.findById(foodId, food)){
                    foodList.add(food);
                }
                CachedData.categoriesMaps.put(cate.getId(), foodList);
            }
        }
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
