package com.luvsoft.entities;

import java.util.Date;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public abstract class AbstractEntity {

    public AbstractEntity(BasicDBObject object) {
        setObject(object);
    }

    public AbstractEntity() {
    }

    public abstract void setObject(BasicDBObject dbobject);

    public abstract HashMap<String, Object> toHashMap();
    
    public String getString(String fieldName, BasicDBObject dbObject){
        if( dbObject.get(fieldName) != null ){
            return dbObject.get(fieldName).toString();
        }
        return "";
    }

    public double getDouble(String fieldName, BasicDBObject dbObject){
        if( dbObject.get(fieldName) != null ){
            return dbObject.getDouble(fieldName);
        }
        return 0;
    }

    public int getInt(String fieldName, BasicDBObject dbObject){
        if( dbObject.get(fieldName) != null ){
            return dbObject.getInt(fieldName);
        }
        return 0;
    }

    public boolean getBoolean(String fieldName, BasicDBObject dbObject) {
        if( dbObject.get(fieldName) != null ){
            return dbObject.getBoolean(fieldName);
        }
        return true;
    }

    public Date getDate(String fieldName, BasicDBObject dbObject){
        if( dbObject.get(fieldName) != null ){
            return dbObject.getDate(fieldName);
        }
        return new Date(); // current datetime
    }
}
