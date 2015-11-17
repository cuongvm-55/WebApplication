package com.luvsoft.facades;

import com.luvsoft.entities.Category;
import com.mongodb.BasicDBObject;

public class CategoryFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_CATEGORY = "Category";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_CATEGORY;
    }

    @Override
    public Category mapObject(BasicDBObject dbobject) {
        Category category = new Category(dbobject);
        return category;
    }
}
