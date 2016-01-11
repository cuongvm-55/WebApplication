package com.luvsoft.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Food extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -3527033998271070447L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_PRICE = "Price";
    public static final String DB_FIELD_NAME_ACTIVE = "isActive";

    private String id;
    private String name;
    private double price;
    private boolean isActive;

    public Food()
    {
        id = "";
        name = "";
        price = 0;
        isActive = true;
    }

    public Food(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_NAME, name);
        map.put(DB_FIELD_NAME_PRICE, price);
        map.put(DB_FIELD_NAME_ACTIVE, isActive);
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        name = getString(DB_FIELD_NAME_NAME, dbObject);
        price = getDouble(DB_FIELD_NAME_PRICE, dbObject);
        isActive = getBoolean(DB_FIELD_NAME_ACTIVE, dbObject);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Food [id=" + id + ", name=" + name + ", price=" + price + "isActive " + isActive + "]";
    }

}
