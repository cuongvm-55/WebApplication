package com.luvsoft.MMI.order;

import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
public class OrderDetailRecord{
    private String orderDetailId;
    private String foodName;
    private String foodId;
    private Types.State status;
    private Types.State previousStatus;
    private Types.State originalStatus;
    private int quantity;
    private int originalQuantity;
    private double price;
    static enum ChangedFlag{
        UNMODIFIED,
        MODIFIED,
        ADDNEW,
        DELETED,
    };
    private ChangedFlag changeFlag;
    private ChangedFlag tempStatusChangeFlag;
    private ChangedFlag tempQuantityChangeFlag;

    static Types.State[] states = {State.WAITING, State.COMPLETED, State.CANCELED};
    
    public OrderDetailRecord()
    {
        orderDetailId = "";
        foodName = "";
        status = State.WAITING;
        quantity = 0;
        price = 0.00f;
        changeFlag = ChangedFlag.UNMODIFIED;
        setTempQuantityChangeFlag(ChangedFlag.UNMODIFIED);
        setTempQuantityChangeFlag(ChangedFlag.UNMODIFIED);
        previousStatus = State.UNDEFINED;
        setOriginalQuantity(-1);
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

    public Types.State getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(Types.State originalStatus) {
        this.originalStatus = originalStatus;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
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

    public ChangedFlag getTempStatusChangeFlag() {
        return tempStatusChangeFlag;
    }

    public void setTempStatusChangeFlag(ChangedFlag tempStatusChangeFlag) {
        this.tempStatusChangeFlag = tempStatusChangeFlag;
    }

    public ChangedFlag getTempQuantityChangeFlag() {
        return tempQuantityChangeFlag;
    }

    public void setTempQuantityChangeFlag(ChangedFlag tempQuantityChangeFlag) {
        this.tempQuantityChangeFlag = tempQuantityChangeFlag;
    }

    @Override
    public String toString() {
        return "OrderDetailRecord [orderDetailId=" + orderDetailId
                + ", foodName=" + foodName + ", foodId=" + foodId + ", status="
                + status + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
