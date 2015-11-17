package com.luvsoft.facades;

import com.luvsoft.entities.OrderDetail;
import com.mongodb.BasicDBObject;

public class OrderDetailsFacade extends AbstractFacade{
    public static final String DB_TABLE_NAME_ORDER_DETAIL = "OrderDetails";
    @Override
    public String getCollectionName() {
        return DB_TABLE_NAME_ORDER_DETAIL;
    }

    @Override
    public OrderDetail mapObject(BasicDBObject dbobject) {
        OrderDetail orderdetail = new OrderDetail(dbobject);
        return orderdetail;
    }
}
