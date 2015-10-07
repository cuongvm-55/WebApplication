package com.luvsoft.entities;

import com.luvsoft.entities.Table.State;
import com.mongodb.DBObject;

public class Food extends AbstractEntity{
    public static final String DB_TABLE_NAME_FOOD = "Food";
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_PRICE = "Price";

    private String id;
    private String code;
    private String name;
    private float price;

    public Food()
    {
        id = "";
        code = "";
        name = "";
        price = 0;
    }

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        name = dbobject.get(DB_FIELD_NAME_NAME).toString();
        price = Float.parseFloat(dbobject.get(DB_FIELD_NAME_PRICE).toString());
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Food [id=" + id + ", code=" + code + ", name=" + name
                + ", price=" + price + "]";
    }

}
