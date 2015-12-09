package com.luvsoft.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Food extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -3527033998271070447L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_PRICE = "Price";

    private String id;
    private String code;
    private String name;
    private double price;

    public Food()
    {
        id = "";
        code = "";
        name = "";
        price = 0;
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
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NAME, name);
        map.put(DB_FIELD_NAME_PRICE, price);
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        code = getString(DB_FIELD_NAME_CODE, dbObject);
        name = getString(DB_FIELD_NAME_NAME, dbObject);
        price = getDouble(DB_FIELD_NAME_PRICE, dbObject);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Food [id=" + id + ", code=" + code + ", name=" + name
                + ", price=" + price + "]";
    }

}
