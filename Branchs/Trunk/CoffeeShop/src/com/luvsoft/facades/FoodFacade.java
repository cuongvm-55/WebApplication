package com.luvsoft.facades;

import com.luvsoft.entities.Food;
import com.mongodb.BasicDBObject;

public class FoodFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_FOOD = "Food";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_FOOD;
    }

    @Override
    public Food mapObject(BasicDBObject dbobject) {
        Food food = new Food(dbobject);
        return food;
    }
}
