package com.luvsoft.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Floor extends AbstractEntity implements Comparable<Floor>, Serializable {
    private static final long serialVersionUID = 6493408468507998364L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_NUMBER = "Number";
    public static final String DB_FIELD_NAME_TABLE_LIST = "TableList";

    private String id;
    private String number;
    private List<String> tableIdList; // list of table ObjectId

    public Floor()
    {
        id = "";
        number = "";
        tableIdList = new ArrayList<String>();
    }
    
    public Floor(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public void setObject(BasicDBObject dbObject){
        id = getString(DB_FIELD_NAME_ID, dbObject);
        number = getString(DB_FIELD_NAME_NUMBER, dbObject);
        tableIdList = Types.stringToList(getString(DB_FIELD_NAME_TABLE_LIST, dbObject));
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_NUMBER, number);
        // Map list
        map.put(DB_FIELD_NAME_TABLE_LIST, Types.formatListToString(tableIdList));
        return map;
    }

    @Override
    public String toString() {
        return "Floor [id=" + id + ", number=" + number + ", tableIdList="
                + tableIdList + "]";
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

    public List<String> getTableIdList() {
        return tableIdList;
    }

    public void setTableIdList(List<String> tableIdList) {
        this.tableIdList = tableIdList;
    }

    @Override
    public int compareTo(Floor compareFloor) {
        int compareNumber = Integer.parseInt(compareFloor.getNumber());
        return Integer.parseInt(this.number) - compareNumber;
    }
}
