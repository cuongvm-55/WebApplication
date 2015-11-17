package com.luvsoft.facades;

import com.luvsoft.entities.Table;
import com.mongodb.BasicDBObject;

public class TableFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_TABLE = "Table";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_TABLE;
    }

    @Override
    public Table mapObject(BasicDBObject dbobject) {
        Table table = new Table(dbobject);
        return table;
    }
}
