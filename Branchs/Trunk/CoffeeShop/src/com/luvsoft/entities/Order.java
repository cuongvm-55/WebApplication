package com.luvsoft.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mongodb.DBObject;

public class Order extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_ORDER_DETAIL_LIST = "OrderDetailList";
    public static final String DB_FIELD_NAME_STATUS = "Status"; // Wait, Paid, NotPaid
    public static final String DB_FIELD_NAME_PAID_MONEY = "PaidMoney";
    public static final String DB_FIELD_NAME_WAITING_TIME = "WaitingTime";
    public static final String DB_FIELD_NAME_PAID_TIME = "PaidTime";
    public static final String DB_FIELD_NAME_TABLE_ID = "TableId";
    public static final String DB_FIELD_NAME_NOTE = "Note";
    public static final String DB_FIELD_NAME_STAFF_NAME = "StaffName";
    public static final String DB_FIELD_NAME_CREATING_TIME = "CreatingTime";

    private String id;
    private List<String> orderDetailIdList; // list of order detail object id
    private Types.State status;
    private float  paidMoney; // thousand vietnam dong
    private LocalDateTime creatingTime;
    private int waitingTime; // in minutes
    private LocalDateTime paidTime;
    private String tableId;
    private String note;
    private String staffName;

    public Order()
    {
        id = "";
        orderDetailIdList = new ArrayList<String>();
        status = Types.State.UNDEFINED;
        paidMoney = 0;
        creatingTime = LocalDateTime.now();
        paidTime = LocalDateTime.now();
        waitingTime = 0;
        tableId = "";
        note = "";
        staffName = "";
    }

    public Order(DBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_TABLE_ID, tableId);
        map.put(DB_FIELD_NAME_NOTE, note);
        map.put(DB_FIELD_NAME_PAID_MONEY, "" + paidMoney);
        map.put(DB_FIELD_NAME_PAID_TIME, paidTime.toString());
        map.put(DB_FIELD_NAME_WAITING_TIME, creatingTime.toString());
        map.put(DB_FIELD_NAME_STATUS, status.toString());
        map.put(DB_FIELD_NAME_STAFF_NAME, staffName);
        map.put(DB_FIELD_NAME_CREATING_TIME, ""+creatingTime);
        // Map list
        map.put(DB_FIELD_NAME_ORDER_DETAIL_LIST, Types.formatListToString(orderDetailIdList));
        return map;
    }

    @Override
    public void setObject(DBObject dbObject)
    {
        id = getFieldValue(DB_FIELD_NAME_ID, dbObject);
        String str =getFieldValue(DB_FIELD_NAME_ORDER_DETAIL_LIST, dbObject);
        String[] list = str.split(",");
        orderDetailIdList = Arrays.asList(list);
//        BasicDBList orderDetailList = (BasicDBList)dbobject.get(DB_FIELD_NAME_ORDER_DETAIL_LIST);
//        orderDetailIdList = new ArrayList<String>(); 
//        for(Object item : orderDetailList)
//        {
//            orderDetailIdList.add((String)item);
//        }
        switch( getFieldValue(DB_FIELD_NAME_STATUS, dbObject) )
        {
        case "WAITING":
            status = Types.State.WAITING;
            break;
        case "UNPAID":
            status = Types.State.UNPAID;
            break;
        case "PAID":
            status = Types.State.PAID;
            break;
        case "COMPLETE":
            status = Types.State.COMPLETED;
            break;
        default:
            status = Types.State.UNDEFINED;
            break;
        }
        paidMoney = Float.parseFloat(getFieldValue(DB_FIELD_NAME_PAID_MONEY, dbObject));
        try{
            creatingTime = LocalDateTime.parse(getFieldValue(DB_FIELD_NAME_CREATING_TIME, dbObject));
        }catch(Exception e){
            System.out.println("Fail to parse creating time");
        }
        try{
            paidTime = LocalDateTime.parse(getFieldValue(DB_FIELD_NAME_PAID_TIME, dbObject));
        }catch( Exception e ){
            System.out.println("Fail to parse paid time");
        }
        tableId = getFieldValue(DB_FIELD_NAME_TABLE_ID, dbObject);
        note = getFieldValue(DB_FIELD_NAME_NOTE, dbObject);
        staffName = getFieldValue(DB_FIELD_NAME_STAFF_NAME, dbObject);
        try{
            waitingTime = Integer.parseInt(getFieldValue(DB_FIELD_NAME_WAITING_TIME, dbObject));
        }catch( Exception e ){
            waitingTime = 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getOrderDetailIdList() {
        return orderDetailIdList;
    }

    public void setOrderDetailIdList(List<String> orderDetailIdList) {
        this.orderDetailIdList = orderDetailIdList;
    }

    public Types.State getStatus() {
        return status;
    }

    public void setStatus(Types.State status) {
        this.status = status;
    }

    public float getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(float paidMoney) {
        this.paidMoney = paidMoney;
    }
    
    public LocalDateTime getCreatingTime() {
        return creatingTime;
    }

    public void setCreatingTime(LocalDateTime creatingTime) {
        this.creatingTime = creatingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public LocalDateTime getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(LocalDateTime paidTime) {
        this.paidTime = paidTime;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderDetailIdList=" + orderDetailIdList
                + ", status=" + status + ", paidMoney=" + paidMoney
                + ", waitingTime=" + waitingTime + ", paidTime=" + paidTime
                + ", tableId=" + tableId + ", note=" + note + ", staffName="
                + staffName + "]";
    }
}
