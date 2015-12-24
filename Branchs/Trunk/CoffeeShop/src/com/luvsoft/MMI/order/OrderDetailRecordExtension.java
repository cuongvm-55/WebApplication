package com.luvsoft.MMI.order;

public class OrderDetailRecordExtension {
    private OrderDetailRecord orderDetailRecord;
    private boolean enable;

    public OrderDetailRecordExtension(OrderDetailRecord orderDetailRecord, boolean enable) {
        this.orderDetailRecord = orderDetailRecord;
        this.enable = enable;
    }

    public OrderDetailRecord getOrderDetailRecord() {
        return orderDetailRecord;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "OrderDetailRecordExtension [orderDetailRecord="
                + orderDetailRecord + ", enable=" + enable + "]";
    }
    
}
