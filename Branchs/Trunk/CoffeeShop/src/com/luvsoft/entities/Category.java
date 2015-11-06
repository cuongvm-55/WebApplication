package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mongodb.DBObject;

public class Category extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_FOOD_LIST = "FoodList";

    private String id;
    private String code;
    private String name;
    private List<String> foodIdList; // list of table ObjectId

    public Category()
    {
        id = "";
        code = "";
        name = "";
        foodIdList = new ArrayList<String>();
    }

    public Category(DBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NAME, name);
        //map.put(DB_FIELD_NAME_FOOD_LIST, foodIdList.toString());
        // Map list
        map.put(DB_FIELD_NAME_FOOD_LIST, Types.formatListToString(foodIdList));

        return map;
    }

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        name = dbobject.get(DB_FIELD_NAME_NAME).toString();
        String str = dbobject.get(DB_FIELD_NAME_FOOD_LIST).toString();
        String[] list = str.split(",");
        foodIdList = Arrays.asList(list);
//        BasicDBList  list = (BasicDBList)dbobject.get(DB_FIELD_NAME_FOOD_LIST);
//        foodIdList = new ArrayList<String>(); 
//        for(Object item : list)
//        {
//            foodIdList.add((String)item);
//        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "Category [id=" + id + ", code=" + code + ", name=" + name
                + ", foodIdList=" + foodIdList + "]";
    }
}
