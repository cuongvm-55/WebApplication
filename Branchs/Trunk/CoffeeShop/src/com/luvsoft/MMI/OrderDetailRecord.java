package com.luvsoft.MMI;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
public class OrderDetailRecord{
    private String foodName;
    private String status;
    private int quantity;
    private float price;
    static String[] states = {"WAITING", "COMPLETE", "CANCEL"};
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
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

    public Image getIconFromStatus(String _status){
        Image img = new Image();
        switch(_status){
        case "CANCELED":
            img.setSource(new ThemeResource("images/cancel.png"));
            break;
        case "COMPLETED":
            img.setSource(new ThemeResource("images/complete.png"));
            break;
        default:
            img.setSource(new ThemeResource("images/waiting.png"));
            break;
        }
        return img;
    }

    public String getNextState(String curState){
        int index = 0;
        for( int i=0; i < states.length; i++ ){
            if( states[i].equals(curState)){
                index = i;
            }
        }
        return states[(index + 1)%states.length];
    }
}