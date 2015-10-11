package com.luvsoft.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class Order extends AbstractEntity{
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_ORDER_DETAIL_LIST = "OrderDetailList";
    public static final String DB_FIELD_NAME_STATUS = "Status"; // Wait, Paid, NotPaid
    public static final String DB_FIELD_NAME_PAID_MONEY = "PaidMoney";
    public static final String DB_FIELD_NAME_WAITING_TIME = "WaitingTime";
    public static final String DB_FIELD_NAME_PAID_TIME = "PaidTime";
    public static final String DB_FIELD_TABLE_ID = "TableId";
    public static final String DB_FIELD_NAME_NOTE = "Note";
    
    private String id;
    private List<String> orderDetailIdList; // list of order detail object id
    private Types.State status;
    private float  paidMoney; // thousand vietnam dong
    private LocalDateTime waitingTime;
    private LocalDateTime paidTime;
    private String tableId;
    private String note;
    
    public Order()
    {
        id = "";
        orderDetailIdList = new ArrayList<String>();
        status = Types.State.UNDEFINED;
        paidMoney = 0;
        tableId = "";
        note = "";
    }

    public Order(DBObject object)
    {
        super(object);
    }

    @Override
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DB_FIELD_TABLE_ID, id);
        map.put(DB_FIELD_NAME_NOTE, note);
        map.put(DB_FIELD_NAME_ORDER_DETAIL_LIST, orderDetailIdList.toString());
        map.put(DB_FIELD_NAME_PAID_MONEY, "" + paidMoney);
        map.put(DB_FIELD_NAME_PAID_TIME, paidTime.toString());
        map.put(DB_FIELD_NAME_WAITING_TIME, waitingTime.toString());
        map.put(DB_FIELD_NAME_STATUS, status.toString());
        return map;
    }

    @Override
    public void setObject(DBObject dbobject)
    {
        id = dbobject.get(DB_FIELD_NAME_ID).toString();
        
        BasicDBList orderDetailList = (BasicDBList)dbobject.get(DB_FIELD_NAME_ORDER_DETAIL_LIST);
        orderDetailIdList = new ArrayList<String>(); 
        for(Object item : orderDetailList)
        {
            orderDetailIdList.add((String)item);
        }
        
        switch( dbobject.get(DB_FIELD_NAME_STATUS).toString() )
        {
        case "WAITING":
            status = Types.State.WAITING;
            break;
        case "NOTPAID":
            status = Types.State.NOTPAID;
            break;
        case "PAID":
            status = Types.State.PAID;
            break;
        default:
            status = Types.State.UNDEFINED;
            break;
        }
        paidMoney = Float.parseFloat(dbobject.get(DB_FIELD_NAME_PAID_MONEY).toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Types.DATE_TIME_PARTTERN);
        waitingTime = LocalDateTime.parse(dbobject.get(DB_FIELD_NAME_WAITING_TIME).toString(), formatter);
        paidTime = LocalDateTime.parse(dbobject.get(DB_FIELD_NAME_PAID_TIME).toString(), formatter);
        tableId = dbobject.get(DB_FIELD_TABLE_ID).toString();
        note = dbobject.get(DB_FIELD_NAME_NOTE).toString();
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

    public LocalDateTime getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(LocalDateTime waitingTime) {
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

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderDetailIdList=" + orderDetailIdList
                + ", status=" + status + ", paidMoney=" + paidMoney
                + ", waitingTime=" + waitingTime + ", paidTime=" + paidTime
                + ", tableId=" + tableId + ", note=" + note + "]";
    }
}
