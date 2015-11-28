package com.luvsoft.controllers;

import com.luvsoft.entities.Food;
import com.luvsoft.facades.FoodFacade;

public class FoodController extends AbstractController{
    private static FoodFacade foodFacade = new FoodFacade();

    public Food getFoodById(String foodId){
        Food food = new Food();
        foodFacade = new FoodFacade();
        foodFacade.findById(foodId, food);
        return food;
    }

    public boolean removeFood(String foodId){
        return foodFacade.removeById(foodId);
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
    public boolean updateFood(String foodId, Food food){
        return foodFacade.update(foodId, food);
    }
}
