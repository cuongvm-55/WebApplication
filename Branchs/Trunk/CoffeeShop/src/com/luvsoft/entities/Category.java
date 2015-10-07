package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class Category extends AbstractEntity{
    public static final String DB_TABLE_NAME_CATEGORY = "Category";
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_FODD_LIST = "FoodList";

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

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        name = dbobject.get(DB_FIELD_NAME_NAME).toString();
        BasicDBList  list = (BasicDBList)dbobject.get(DB_FIELD_NAME_FODD_LIST);
        foodIdList = new ArrayList<String>(); 
        for(Object item : list)
        {
            foodIdList.add((String)item);
        }
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
