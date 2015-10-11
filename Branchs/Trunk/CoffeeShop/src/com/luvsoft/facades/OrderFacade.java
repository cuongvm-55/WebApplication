package com.luvsoft.facades;

import com.luvsoft.entities.Order;
import com.mongodb.DBObject;

public class OrderFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_ORDER = "Order";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_ORDER;
    }

    @Override
    public Order mapObject(DBObject dbobject) {
        Order order = new Order(dbobject);
        return order;
    }

}
