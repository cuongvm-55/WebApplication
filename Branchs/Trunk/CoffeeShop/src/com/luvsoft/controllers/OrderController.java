package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.luvsoft.facades.OrderDetailsFacade;
import com.luvsoft.facades.OrderFacade;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;

/*
 * @author cuongvm-55
 */

public class OrderController extends AbstractController{
    private static OrderFacade orderFacade = new OrderFacade();
    private static OrderDetailsFacade orderDetailFacade = new OrderDetailsFacade();

    public boolean getOrderById(String orderId, Order order){
        return orderFacade.findById(orderId, order);
    }
    
    public boolean getOrderDetailById(String orderDetailId, OrderDetail orderDtl){
        return orderDetailFacade.findById(orderDetailId, orderDtl);
    }
    /*
     * Get all order that has Status = state in date range [begin, end]
     * begin & end = null --> get all
     */
    public List<Order> getOrderListWithState(Types.State state, Date begDate, Date endDate){
        List<Order> list = new ArrayList<Order>();
        BasicDBObject query = new BasicDBObject(Order.DB_FIELD_NAME_STATUS, new BasicDBObject("$eq", state.toString()));
        if( begDate != null && endDate != null ){
            query.append(Order.DB_FIELD_NAME_CREATING_TIME, BasicDBObjectBuilder.start("$gte", begDate).add("$lte", endDate).get());
        }
        orderFacade.findByQuery(query, list);
        Collections.sort(list);
        return list;
    }

    /*
     * Get all order that has Status different from state
     */
    public List<Order> getOrderListIgnoreState(Types.State state, Date begDate, Date endDate){
        List<Order> list = new ArrayList<Order>();
        BasicDBObject query = new BasicDBObject(Order.DB_FIELD_NAME_STATUS, new BasicDBObject("$ne", state.toString())); 
        if( begDate != null && endDate != null ){
            query.append(Order.DB_FIELD_NAME_CREATING_TIME, BasicDBObjectBuilder.start("$gte", begDate).add("$lte", endDate).get());
        }
        orderFacade.findByQuery(query, list);
        Collections.sort(list);
        return list;
    }

    /*
     * Change status of an Order
     */
    public boolean setOrderStatus(String orderId, Types.State status){
        return orderFacade.updateFieldValue(orderId, Order.DB_FIELD_NAME_STATUS, status.toString());
    }
    
    /*
     * Add new Order
     */
    public boolean addNewOrder(Order order){
        return orderFacade.save(order);
    }
    
    /*
     * Remove an Order
     */
    public boolean removeOrder(String orderId){
        return orderFacade.removeById(orderId);
    }
 
    /*
     * Add new OrderDetail
     */
    public boolean addNewOrderDetail(OrderDetail orderDetail){
        return orderDetailFacade.save(orderDetail);
    }
    
    /*
     * Remove an OrderDetail
     */
    public boolean removeOrderDetail(String orderDetailId){
        return orderDetailFacade.removeById(orderDetailId);
    }

    /*
     * Update fieldValue
     */
    public boolean updateFieldValueOfOrder(String orderId, String fieldName, String fieldVale){
        return orderFacade.updateFieldValue(orderId, fieldName, fieldVale);
    }

    /*
     * Update fieldValue
     */
    public boolean updateFieldValueOfOrderDetail(String orderId, String fieldName, String fieldVale){
        return orderDetailFacade.updateFieldValue(orderId, fieldName, fieldVale);
    }

    /*
     * Update order
     */
    public boolean updateOrder(String orderId, Order order){
        return orderFacade.update(orderId, order);
    }
    
    /*
     * Update order detail
     */
    public boolean updateOrderDetail(String orderDetailId, OrderDetail orderDetail){
        return orderDetailFacade.update(orderDetailId, orderDetail);
    }
}
