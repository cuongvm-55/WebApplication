package com.luvsoft.entities;

import com.mongodb.DBObject;

public class Table extends AbstractEntity{
    public static final String DB_TABLE_NAME_TABLE = "Table";
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_STATE = "State";

    private String id;
    private String code;
    private String name;
    private Types.State state;

    public Table()
    {
        id = "";
        code = "";
        name = "";
        state = Types.State.EMPTY;
    }

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        name = dbobject.get(DB_FIELD_NAME_NAME).toString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Types.State getState() {
        return state;
    }

    public void setState(Types.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Table [id=" + id + ", code=" + code + ", name=" + name
                + ", state=" + state + "]";
    }

	
}
