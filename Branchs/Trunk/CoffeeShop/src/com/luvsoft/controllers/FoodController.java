package com.luvsoft.controllers;

import com.luvsoft.entities.Food;
import com.luvsoft.facades.FoodFacade;

public class FoodController extends AbstractController{
    public Food getFoodById(String foodId){
        Food food = new Food();
        FoodFacade foodFacade = new FoodFacade();
        foodFacade.findById(foodId, food);
        return food;
    }
}
