package com.luvsoft.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class OrderDetail extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 2116621444779694747L;
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
    
    public OrderDetail(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_FOOD_ID, foodId);
        map.put(DB_FIELD_NAME_QUANTITY, quantity);
        map.put(DB_FIELD_NAME_STATE, state.toString());
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject)
    {
        id = getString(DB_FIELD_NAME_ID, dbObject);
        foodId = getString(DB_FIELD_NAME_FOOD_ID, dbObject);
        quantity = getInt(DB_FIELD_NAME_QUANTITY, dbObject);

        switch( getString(DB_FIELD_NAME_STATE, dbObject) )
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
