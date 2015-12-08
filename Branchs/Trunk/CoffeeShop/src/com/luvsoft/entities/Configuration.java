package com.luvsoft.entities;

import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Configuration extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_OPERATOR_PINCODE = "OperatorPincode";
    public static final String DB_FIELD_NAME_SUP_PINCODE = "SUPPinCode";
    public static final String DB_FIELD_NAME_REPORT_OUTPUT_DIR = "ReportOutputDir";

    public static final int PINCODE_LENGTH = 5;
    public static final String DEFAULT_SUP_PINCODE = "09430";
    public static final String DEFAULT_OPERATOR_PINCODE = "00000";
    public static final String DEFAULT_REPORT_OUTPUT_DIR = "./";
    private String id;
    private String operatorPincode;
    private String supPincode;
    private String reportOutputDir;

    public Configuration(){
        operatorPincode = "";
        supPincode = "";
        reportOutputDir = "";
    }

    public Configuration(BasicDBObject object)
    {
        super(object);
    }
    
    @Override
    public void setObject(BasicDBObject dbobject) {
        id = dbobject.getString(DB_FIELD_NAME_ID);
        operatorPincode = dbobject.getString(DB_FIELD_NAME_OPERATOR_PINCODE);;
        supPincode = dbobject.getString(DB_FIELD_NAME_SUP_PINCODE);
        reportOutputDir = dbobject.getString(DB_FIELD_NAME_REPORT_OUTPUT_DIR);
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_OPERATOR_PINCODE, operatorPincode);
        map.put(DB_FIELD_NAME_SUP_PINCODE, supPincode);
        map.put(DB_FIELD_NAME_REPORT_OUTPUT_DIR, reportOutputDir);
        return map;
    }

    public String getSupPincode() {
        return supPincode;
    }

    public void setSupPincode(String supPincode) {
        this.supPincode = supPincode;
    }

    public String getReportOutputDir() {
        return reportOutputDir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReportOutputDir(String reportOutputDir) {
        this.reportOutputDir = reportOutputDir;
    }

    public String getOperatorPincode() {
        return operatorPincode;
    }

    public void setOperatorPincode(String operatorPincode) {
        this.operatorPincode = operatorPincode;
    }

}
