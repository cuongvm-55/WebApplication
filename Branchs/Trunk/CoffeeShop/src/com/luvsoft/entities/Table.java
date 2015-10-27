package com.luvsoft.entities;

import java.util.HashMap;

import com.mongodb.DBObject;

public class Table extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NUMBER = "Number";
    public static final String DB_FIELD_NAME_STATE = "State";

    private String id;
    private String code;
    private String number;
    private Types.State state;

    public Table()
    {
        id = "";
        code = "";
        number = "";
        state = Types.State.EMPTY;
    }

    public Table(DBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NUMBER, number);
        map.put(DB_FIELD_NAME_STATE, state.toString());
        return map;
    }

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        number = dbobject.get(DB_FIELD_NAME_NUMBER).toString();
        // extract state
        switch( dbobject.get(DB_FIELD_NAME_STATE).toString() )
        {
        case "WAITING":
            state = Types.State.WAITING;
            break;
        case "FULL":
            state = Types.State.FULL;
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

    @Override
    public String toString() {
        return "Table [id=" + id + ", code=" + code + ", name=" + number
                + ", state=" + state + "]";
    }

	
}
