package com.luvsoft.MMI.Order;

import java.util.List;

public class OrderInfo {
    private String orderId;
    private String tableName;
    List<OrderDetailRecord> orderDetailList;

    public OrderInfo(){
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderDetailRecord> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetailRecord> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
