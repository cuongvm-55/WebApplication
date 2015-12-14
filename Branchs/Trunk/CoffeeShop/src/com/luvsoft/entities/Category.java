package com.luvsoft.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Category extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -4581044646108147940L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_FOOD_LIST = "FoodList";

    private String id;
    private String name;
    private List<String> foodIdList; // list of table ObjectId
    private List<Food> listOfFoodByCategory;

    public Category()
    {
        id = "";
        name = "";
        foodIdList = new ArrayList<String>();
    }

    public Category(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_NAME, name);
        // Map list
        map.put(DB_FIELD_NAME_FOOD_LIST, Types.formatListToString(foodIdList));

        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        name = getString(DB_FIELD_NAME_NAME, dbObject);
        foodIdList = Types.stringToList(getString(DB_FIELD_NAME_FOOD_LIST, dbObject));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFoodIdList() {
        return foodIdList;
    }

    public void setFoodIdList(List<String> foodIdList) {
        this.foodIdList = foodIdList;
    }

    public List<Food> getListOfFoodByCategory() {
        return listOfFoodByCategory;
    }

    public void setListOfFoodByCategory(List<Food> listOfFoodByCategory) {
        this.listOfFoodByCategory = listOfFoodByCategory;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", foodIdList="
                + foodIdList + ", listOfFoodByCategory=" + listOfFoodByCategory
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Category)obj).getId());
    }
}
