package com.luvsoft.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;

public class Order extends AbstractEntity implements Comparable<Order>, Serializable {
    private static final long serialVersionUID = -3731403878156493984L;
    public static final String DB_FIELD_NAME_ID = "_id";
    public static final String DB_FIELD_NAME_ORDER_DETAIL_LIST = "OrderDetailList";
    public static final String DB_FIELD_NAME_STATUS = "Status"; // Wait, Paid, NotPaid
    public static final String DB_FIELD_NAME_PAID_MONEY = "PaidMoney";
    public static final String DB_FIELD_NAME_WAITING_TIME = "WaitingTime";
    public static final String DB_FIELD_NAME_PAID_TIME = "PaidTime";
    public static final String DB_FIELD_NAME_TABLE_ID = "TableId";
    public static final String DB_FIELD_NAME_NOTE = "Note";
    public static final String DB_FIELD_NAME_STAFF_NAME_CONFIRM_PAID = "StaffNameConfirmPaid";
    public static final String DB_FIELD_NAME_STAFF_NAME_CONFIRM_ORDER_FINISH = "StaffNameConfirmOrderFinish";
    public static final String DB_FIELD_NAME_CREATING_TIME = "CreatingTime";
    public static final String DB_FIELD_NAME_CREATOR_NAME = "CreatorName";
    public static final String DB_FIELD_NAME_CHECKSUM = "CheckSum";

    private String id;
    private List<String> orderDetailIdList; // list of order detail object id
    private Types.State status;
    private double  paidMoney; // vietnam dong
    private Date creatingTime;
    private int waitingTime; // in minutes
    private Date paidTime;
    private String tableId;
    private String note;
    private String creatorName; // staff name who create this order
    private String staffNameConfirmPaid;
    private String staffNameConfirmOrderFinish;
    private int checkSum;

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
        staffNameConfirmPaid = "";
        staffNameConfirmOrderFinish = "";
        creatorName = "";
        checkSum = 0;
    }

    public Order(Order order)
    {
        id = order.id;
        orderDetailIdList = order.orderDetailIdList;
        status = order.status;
        paidMoney = order.paidMoney;
        creatingTime = order.creatingTime;
        paidTime = order.paidTime;
        waitingTime = order.waitingTime;
        tableId = order.tableId;
        note = order.note;
        staffNameConfirmPaid = order.staffNameConfirmPaid;
        staffNameConfirmOrderFinish = order.staffNameConfirmOrderFinish;
        creatorName = order.creatorName;
        checkSum = order.checkSum;
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
        map.put(DB_FIELD_NAME_STAFF_NAME_CONFIRM_PAID, staffNameConfirmPaid);
        map.put(DB_FIELD_NAME_STAFF_NAME_CONFIRM_ORDER_FINISH, staffNameConfirmOrderFinish);
        map.put(DB_FIELD_NAME_CREATOR_NAME, creatorName);
        map.put(DB_FIELD_NAME_PAID_TIME, paidTime);
        map.put(DB_FIELD_NAME_CREATING_TIME, creatingTime);
        map.put(DB_FIELD_NAME_CHECKSUM, checkSum);
        // Map list
        map.put(DB_FIELD_NAME_ORDER_DETAIL_LIST, Types.formatListToString(orderDetailIdList));
        return map;
    }

    @Override
    public void setObject(BasicDBObject dbObject)
    {
        id = getString(DB_FIELD_NAME_ID, dbObject);
        orderDetailIdList = Types.stringToList(getString(DB_FIELD_NAME_ORDER_DETAIL_LIST, dbObject));
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
        staffNameConfirmPaid = getString(DB_FIELD_NAME_STAFF_NAME_CONFIRM_PAID, dbObject);
        staffNameConfirmOrderFinish = getString(DB_FIELD_NAME_STAFF_NAME_CONFIRM_ORDER_FINISH, dbObject);
        waitingTime = getInt(DB_FIELD_NAME_WAITING_TIME, dbObject);
        creatorName = getString(DB_FIELD_NAME_CREATOR_NAME, dbObject);
        checkSum = getInt(DB_FIELD_NAME_CHECKSUM, dbObject);
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

    public String getStaffNameConfirmPaid() {
        return staffNameConfirmPaid;
    }

    public void setStaffNameConfirmPaid(String staffNameConfirmPaid) {
        this.staffNameConfirmPaid = staffNameConfirmPaid;
    }

    public String getStaffNameConfirmOrderFinish() {
        return staffNameConfirmOrderFinish;
    }

    public void setStaffNameConfirmOrderFinish(String staffNameConfirmOrderFinish) {
        this.staffNameConfirmOrderFinish = staffNameConfirmOrderFinish;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderDetailIdList=" + orderDetailIdList
                + ", status=" + status + ", paidMoney=" + paidMoney
                + ", creatingTime=" + creatingTime + ", waitingTime="
                + waitingTime + ", paidTime=" + paidTime + ", tableId="
                + tableId + ", note=" + note + ", creatorName=" + creatorName
                + ", staffNameConfirmPaid=" + staffNameConfirmPaid
                + ", staffNameConfirmOrderFinish="
                + staffNameConfirmOrderFinish + "checkSum=" + checkSum + "]";
    }

    @Override
    public int compareTo(Order compareOrder) {
        return this.creatingTime.compareTo(compareOrder.getCreatingTime());
    }
}
