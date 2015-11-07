package com.luvsoft.entities;

import java.util.HashMap;

import com.mongodb.DBObject;

public class Food extends AbstractEntity{
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

    public Food(DBObject object)
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
        map.put(DB_FIELD_NAME_PRICE, "" + price);
        return map;
    }

    @Override
    public void setObject(DBObject dbObject){
        id = getFieldValue(DB_FIELD_NAME_ID, dbObject);
        code = getFieldValue(DB_FIELD_NAME_CODE, dbObject);
        name = getFieldValue(DB_FIELD_NAME_NAME, dbObject);
        price = Float.parseFloat(getFieldValue(DB_FIELD_NAME_PRICE, dbObject));
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
