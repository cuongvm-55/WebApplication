package com.luvsoft.MMI.Order;

import java.util.List;

public class OrderInfo {
    private String orderId;
    private String tableName;
    List<OrderDetailRecord> orderDetailList;
    private String note;

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

    public List<OrderDetailRecord> getOrderDetailRecordList() {
        return orderDetailList;
    }

    public void setOrderDetailRecordList(List<OrderDetailRecord> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
