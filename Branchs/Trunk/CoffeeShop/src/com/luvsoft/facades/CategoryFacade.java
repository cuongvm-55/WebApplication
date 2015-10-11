package com.luvsoft.facades;

import com.luvsoft.entities.Category;
import com.mongodb.DBObject;

public class CategoryFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_CATEGORY = "Category";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_CATEGORY;
    }

    @Override
    public Category mapObject(DBObject dbobject) {
        Category category = new Category(dbobject);
        return category;
    }
}
