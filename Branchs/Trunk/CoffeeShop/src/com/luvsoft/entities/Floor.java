package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class Floor extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_CODE = "Code";
    public static final String DB_FIELD_NAME_NAME = "Name";
    public static final String DB_FIELD_NAME_TABLE_LIST = "TableList";

    private String id;
    private String code;
    private String name;
    private List<String> tableIdList; // list of table ObjectId

    public Floor()
    {
        id = "";
        code = "";
        name = "";
        tableIdList = new ArrayList<String>();
    }
    
    public Floor(DBObject object)
    {
        super(object);
    }

    @Override
    public void setObject(DBObject dbobject){
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        code = dbobject.get(DB_FIELD_NAME_CODE).toString();
        name = dbobject.get(DB_FIELD_NAME_NAME).toString();
        BasicDBList  list = (BasicDBList)dbobject.get(DB_FIELD_NAME_TABLE_LIST);
        tableIdList = new ArrayList<String>();
        for(Object item : list)
        {
            tableIdList.add((String)item);
        }
    }

    @Override
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_CODE, code);
        map.put(DB_FIELD_NAME_NAME, name);
        map.put(DB_FIELD_NAME_TABLE_LIST, tableIdList.toString());
        return map;
    }
    
    @Override
    public String toString() {
        return "Floor [id=" + id + ", code=" + code + ", name=" + name
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableIdList() {
        return tableIdList;
    }

    public void setTableIdList(List<String> tableIdList) {
        this.tableIdList = tableIdList;
    }
}
