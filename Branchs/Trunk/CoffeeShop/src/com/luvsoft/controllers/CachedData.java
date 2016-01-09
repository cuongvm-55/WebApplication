package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.luvsoft.facades.CategoryFacade;
import com.luvsoft.facades.FoodFacade;

public class CachedData {
    // dynamic data (orders, order details)
    //protected static List<Order> orderList = null;
    //protected static Map<Order, List<OrderDetail>> orderMaps = new HashMap<Order, List<OrderDetail>>(); // <Order, OrderDetailList>

    // static data (floors, tables, categories, foods)
    //protected static List<Floor> floorList = null;
    // protected static Map<String, List<Table>> floorMaps = new HashMap<String, List<Table>>(); // <FloorId, TableList>
    
    protected static List<Category> categoryList = new ArrayList<Category>();
    protected static Map<String, List<Food>> categoriesMaps = new HashMap<String, List<Food>>();; // <CategoryIds, FoodList>

    /**
     * This function init the cached data of Category and Food
     */
    static public void initFoodsCachedData(){
        // init category list
        categoryList.clear();
        initCategoryList();
        
        // init categoriesMaps
        categoriesMaps.clear();
        for( int i=0;i< categoryList.size();i++){
            initFoodList(categoryList.get(i));
        }
    }

    static private void initCategoryList(){
        CategoryFacade categoryFacade = new CategoryFacade();
        categoryFacade.findAll(categoryList);
    }

    static private void initFoodList(Category cate){
        if( cate != null){
            List<Food> foodList = new ArrayList<Food>();
            FoodFacade foodFacade = new FoodFacade();
            for( String foodId : cate.getFoodIdList() ){
                Food food = new Food();
                if( foodFacade.findById(foodId, food)){
                    foodList.add(food);
                }
                // first, remove the old map if it exist
                categoriesMaps.remove(cate.getId());

                // re put
                categoriesMaps.put(cate.getId(), foodList);
            }
        }
    }

    public static void removeCategory(String categoryId){
        // remove category in category list
        for(Iterator<Category> it = categoryList.iterator(); it.hasNext(); ) {
            Category category = it.next();
            if( category.getId().equals(categoryId) ) {
                it.remove();
                break;
            }
        }

        // remove food list
        categoriesMaps.remove(categoryId);
    }

    public static void addNewCategory(Category cat){
        categoryList.add(cat);
        categoriesMaps.put(cat.getId(), new ArrayList<Food>());
    }

    public static void updateCategory(Category cat){
        // add category into category list
        for(int i=0;i<categoryList.size();i++ ) {
            if( categoryList.get(i).getId().equals(cat.getId()) ) {
                categoryList.set(i, cat); 
                break;
            }
        }

        // init food list
        initFoodList(cat);
    }
}
