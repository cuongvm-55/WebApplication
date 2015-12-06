package com.luvsoft.MMI.order;

import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
public class OrderDetailRecord{
    private String orderDetailId;
    private String foodName;
    private String foodId;
    private Types.State status;
    private Types.State previousStatus;
    private int quantity;
    private double price;
    static enum ChangedFlag{
        UNMODIFIED,
        MODIFIED,
        ADDNEW,
        DELETED
    };
    private ChangedFlag changeFlag; 

    static Types.State[] states = {State.WAITING, State.COMPLETED, State.CANCELED};
    
    public OrderDetailRecord()
    {
        orderDetailId = "";
        foodName = "";
        status = State.WAITING;
        quantity = 0;
        price = 0.00f;
        changeFlag = ChangedFlag.UNMODIFIED;
        previousStatus = State.CANCELED;
    }

    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public Types.State getStatus() {
        return status;
    }
    public void setStatus(Types.State status) {
        this.status = status;
    }
    public Types.State getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(Types.State previousStatus) {
        this.previousStatus = previousStatus;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }
    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public ChangedFlag getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(ChangedFlag changeFlag) {
        this.changeFlag = changeFlag;
    }

    @Override
    public String toString() {
        return "OrderDetailRecord [orderDetailId=" + orderDetailId
                + ", foodName=" + foodName + ", foodId=" + foodId + ", status="
                + status + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
