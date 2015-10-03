package com.luvsoft.entities;

import com.mongodb.DBObject;

public abstract class AbstractEntity {
    public AbstractEntity(DBObject object) {
    }

    public AbstractEntity() {
    }
    
    public abstract void setObject(DBObject dbobject);
}
