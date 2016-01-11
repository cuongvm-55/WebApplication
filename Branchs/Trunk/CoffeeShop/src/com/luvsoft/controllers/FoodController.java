package com.luvsoft.controllers;

import com.luvsoft.entities.Food;
import com.luvsoft.facades.FoodFacade;

public class FoodController extends AbstractController{
    private static FoodFacade foodFacade = new FoodFacade();

    public Food getFoodById(String foodId){
        Food food = new Food();
        foodFacade.findById(foodId, food);
        return food;
    }

    public boolean removeFood(String foodId){
        // return foodFacade.removeById(foodId);
        // We don't want to delete this food, just change it's state to inactive 
        return foodFacade.updateFieldValue(foodId, Food.DB_FIELD_NAME_ACTIVE, false);
    }

    /*
     * Add new food
     */
    public boolean addNewFood(Food food){
        return foodFacade.save(food);
    }

    /*
     * Update foodId by information of food
     */
    public boolean updateFood(Food food){
        return foodFacade.update(food.getId(), food);
    }
}
