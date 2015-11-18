package com.luvsoft.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Order extends AbstractEntity implements Comparable<Order> {
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
    private double  paidMoney; // vietnam dong
    private Date creatingTime;
    private int waitingTime; // in minutes
    private Date paidTime;
    private String tableId;
    private String note;
    private String staffName;

    public Order()
    {
        id = "";
        orderDetailIdList = new ArrayList<String>();
        status = Types.State.UNDEFINED;
        paidMoney = 0;
        creatingTime = new Date();
        paidTime = new Date();
        waitingTime = 0;
        tableId = "";
        note = "";
        staffName = "";
    }

    public Order(BasicDBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, Object> toHashMap()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DB_FIELD_NAME_ID, id);
        map.put(DB_FIELD_NAME_TABLE_ID, tableId);
        map.put(DB_FIELD_NAME_NOTE, note);
        map.put(DB_FIELD_NAME_PAID_MONEY, paidMoney);
        map.put(DB_FIELD_NAME_WAITING_TIME, waitingTime);
        map.put(DB_FIELD_NAME_STATUS, status.toString());
        map.put(DB_FIELD_NAME_STAFF_NAME, staffName);
        // Date time need to save as TimeStamp type
        map.put(DB_FIELD_NAME_PAID_TIME, paidTime);
        map.put(DB_FIELD_NAME_CREATING_TIME, creatingTime);
        // Map list
        map.put(DB_FIELD_NAME_ORDER_DETAIL_LIST, Types.formatListToString(orderDetailIdList));
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject)
    {
        id = getString(DB_FIELD_NAME_ID, dbObject);
        String str =getString(DB_FIELD_NAME_ORDER_DETAIL_LIST, dbObject);
        String[] list = str.split(",");
        orderDetailIdList = Arrays.asList(list);
//        BasicDBList orderDetailList = (BasicDBList)dbobject.get(DB_FIELD_NAME_ORDER_DETAIL_LIST);
//        orderDetailIdList = new ArrayList<String>(); 
//        for(Object item : orderDetailList)
//        {
//            orderDetailIdList.add((String)item);
//        }
        switch( getString(DB_FIELD_NAME_STATUS, dbObject) )
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
        paidMoney = getDouble(DB_FIELD_NAME_PAID_MONEY, dbObject);
        creatingTime = getDate(DB_FIELD_NAME_CREATING_TIME, dbObject);
        paidTime = getDate(DB_FIELD_NAME_PAID_TIME, dbObject);

        tableId = getString(DB_FIELD_NAME_TABLE_ID, dbObject);
        note = getString(DB_FIELD_NAME_NOTE, dbObject);
        staffName = getString(DB_FIELD_NAME_STAFF_NAME, dbObject);
        waitingTime = getInt(DB_FIELD_NAME_WAITING_TIME, dbObject);
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

    public double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
    }
    
    public Date getCreatingTime() {
        return creatingTime;
    }

    public void setCreatingTime(Date creatingTime) {
        this.creatingTime = creatingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
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

    @Override
    public int compareTo(Order compareOrder) {
        return this.creatingTime.compareTo(compareOrder.getCreatingTime());
    }
}
