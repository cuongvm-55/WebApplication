package com.luvsoft.MMI.Order;

import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
public class OrderDetailRecord{
    private String orderDetailId;
    private String foodName;
    private String foodId;
    private Types.State status;
    private int quantity;
    private float price;
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
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public Image getIconFromStatus(Types.State state){
        Image img = new Image();
        switch(state){
        case CANCELED:
            img.setSource(new ThemeResource("images/cancel.png"));
            break;
        case COMPLETED:
            img.setSource(new ThemeResource("images/complete.png"));
            break;
        default:
            img.setSource(new ThemeResource("images/waiting.png"));
            break;
        }
        return img;
    }

    public State getNextState(State curState){
        int index = 0;
        for( int i=0; i < states.length; i++ ){
            if( states[i].equals(curState)){
                index = i;
            }
        }
        return states[(index + 1)%states.length];
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
