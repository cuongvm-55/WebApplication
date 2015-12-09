package com.luvsoft.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Table extends AbstractEntity implements Serializable, Comparable<Table> {
    private static final long serialVersionUID = 4727294623902070131L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NUMBER = "Number";
    public static final String DB_FIELD_NAME_STATE = "State";

    private String id;
    private String code;
    private String number;
    private int waitingTime;
    private Types.State state;
    private String staffName;

    public Table()
    {
        id = "";
        code = "";
        number = "";
        state = Types.State.EMPTY;
    }

    public Table(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NUMBER, number);
        map.put(DB_FIELD_NAME_STATE, state.toString());
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        code = getString(DB_FIELD_NAME_CODE, dbObject);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "Table [id=" + id + ", code=" + code + ", name=" + number
                + ", state=" + state + "]";
    }

    @Override
    public int compareTo(Table compareTable) {
        int compareNumber = Integer.parseInt(compareTable.getNumber());
        return Integer.parseInt(this.number) - compareNumber;
    }
}
