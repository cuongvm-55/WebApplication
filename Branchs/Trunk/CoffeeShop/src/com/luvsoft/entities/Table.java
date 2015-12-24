package com.luvsoft.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Table extends AbstractEntity implements Serializable, Comparable<Table> {
    private static final long serialVersionUID = 4727294623902070131L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_NUMBER = "Number";
    public static final String DB_FIELD_NAME_STATE = "State";

    private String id;
    private String number;
    private int waitingTime;
    private Types.State state;
    private String staffName;

    public Table()
    {
        id = "";
        number = "";
        state = Types.State.EMPTY;
        staffName = "";
    }

    public Table(BasicDBObject object)
    {
        super(object);
    }

    public void setTable(Table table) {
        this.id = table.getId();
        this.number = table.getNumber();
        this.waitingTime = table.getWaitingTime();
        this.state = table.getState();
        this.staffName = table.getStaffName();
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_NUMBER, number);
        map.put(DB_FIELD_NAME_STATE, state.toString());
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        number = getString(DB_FIELD_NAME_NUMBER, dbObject);
        // extract state
        switch( getString(DB_FIELD_NAME_STATE, dbObject) )
        {
            case "WAITING":
                state = Types.State.WAITING;
                break;
            case "PAID":
                state = Types.State.PAID;
                break;
            case "UNPAID":
                state = Types.State.UNPAID;
                break;
            default:
                state = Types.State.EMPTY;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Types.State getState() {
        return state;
    }

    public void setState(Types.State state) {
        this.state = state;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public String toString() {
        return "Table [id=" + id + ", number=" + number + ", waitingTime="
                + waitingTime + ", state=" + state + ", staffName=" + staffName
                + "]";
    }

    @Override
    public int compareTo(Table compareTable) {
        int compareNumber = Integer.parseInt(compareTable.getNumber());
        return Integer.parseInt(this.number) - compareNumber;
    }
}
