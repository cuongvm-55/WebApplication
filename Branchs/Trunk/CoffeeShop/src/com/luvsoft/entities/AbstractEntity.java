package com.luvsoft.entities;

import java.util.HashMap;

import com.mongodb.DBObject;

public abstract class AbstractEntity {

    public AbstractEntity(DBObject object) {
        setObject(object);
    }

    public AbstractEntity() {
    }

    public abstract void setObject(DBObject dbobject);

    public abstract HashMap<String, String> toHashMap();
    
    public String getFieldValue(String fieldName, DBObject dbObject){
        if( dbObject.get(fieldName) != null ){
            return dbObject.get(fieldName).toString();
        }
        return "";
    }
}
