package com.luvsoft.entities;

import com.mongodb.DBObject;

public class OrderDetail extends AbstractEntity{
    public static final String DB_TABLE_NAME_ORDER_DETAIL = "OrderDetails";
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_FOOD_ID = "FoodId";
    public static final String DB_FIELD_NAME_QUANTITY = "Quantity";
    public static final String DB_FIELD_NAME_STATE = "State";

    private String id;
    private String foodId;
    private int quantity;
    private Types.State state;

    public OrderDetail()
    {
        id = "";
        foodId = "";
        quantity = 0;
        state = Types.State.UNDEFINED;
    }
    
    public void setObject(DBObject dbObject)
    {
        id = dbObject.get(DB_FIELD_NAME_ID).toString();
        foodId = dbObject.get(DB_FIELD_NAME_FOOD_ID).toString();
        quantity = Integer.parseInt( dbObject.get(DB_FIELD_NAME_QUANTITY).toString() );

        switch( dbObject.get(DB_FIELD_NAME_STATE).toString() )
        {
        case "WAITING":
            state = Types.State.WAITING;
            break;
        case "COMPLETED":
            state = Types.State.COMPLETED;
            break;
        case "CANCELED":
            state = Types.State.CANCELED;
            break;
        default:
            state = Types.State.UNDEFINED;
            break;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Types.State getState() {
        return state;
    }

    public void setState(Types.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", foodId=" + foodId + ", quantity="
                + quantity + ", state=" + state + "]";
    }
}
