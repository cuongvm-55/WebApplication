package com.luvsoft.facades;

import com.luvsoft.entities.Floor;
import com.mongodb.BasicDBObject;

public class FloorFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_FLOOR = "Floor";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_FLOOR;
    }

    @Override
    public Floor mapObject(BasicDBObject dbobject) {
        Floor floor = new Floor(dbobject);
        return floor;
    }

}
