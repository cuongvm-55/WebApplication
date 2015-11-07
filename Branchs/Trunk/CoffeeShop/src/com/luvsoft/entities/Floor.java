package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mongodb.DBObject;

public class Floor extends AbstractEntity{
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
    
    public Floor(DBObject object)
    {
        super(object);
    }

    @Override
    public void setObject(DBObject dbObject){
        id = getFieldValue(DB_FIELD_NAME_ID, dbObject);
        code = getFieldValue(DB_FIELD_NAME_CODE, dbObject);
        number = getFieldValue(DB_FIELD_NAME_NUMBER, dbObject);
        String str = getFieldValue(DB_FIELD_NAME_TABLE_LIST, dbObject);
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
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NUMBER, number);
        //map.put(DB_FIELD_NAME_TABLE_LIST, tableIdList.toString());
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
}
