package com.luvsoft.facades;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Configuration;
import com.mongodb.BasicDBObject;

public class ConfigurationFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_CONFIGURATION = "Configuration";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_CONFIGURATION;
    }

    @Override
    public AbstractEntity mapObject(BasicDBObject dbobject) {
        Configuration config = new Configuration(dbobject);
        return config;
    }

}
