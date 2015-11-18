package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Floor extends AbstractEntity implements Comparable<Floor> {
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NUMBER = "Number";
    public static final String DB_FIELD_NAME_TABLE_LIST = "TableList";

    private String id;
    private String code;
    private String number;
    private List<String> tableIdList; // list of table ObjectId

    public Floor()
    {
        id = "";
        code = "";
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
        code = getString(DB_FIELD_NAME_CODE, dbObject);
        number = getString(DB_FIELD_NAME_NUMBER, dbObject);
        String str = getString(DB_FIELD_NAME_TABLE_LIST, dbObject);
        String[] list = str.split(",");
        tableIdList = Arrays.asList(list);
//        BasicDBList  list = (BasicDBList)dbobject.get(DB_FIELD_NAME_TABLE_LIST);
//        tableIdList = new ArrayList<String>();
//        for(Object item : list)
//        {
//            tableIdList.add((String)item);
//        }
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NUMBER, number);
        // Map list
        map.put(DB_FIELD_NAME_TABLE_LIST, Types.formatListToString(tableIdList));
        return map;
    }
    
    @Override
    public String toString() {
        return "Floor [id=" + id + ", code=" + code + ", name=" + number
                + ", tableIdList=" + tableIdList + "]";
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
