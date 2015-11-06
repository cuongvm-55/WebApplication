package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.luvsoft.facades.OrderDetailsFacade;
import com.luvsoft.facades.OrderFacade;
import com.mongodb.BasicDBObject;

/*
 * @author cuongvm-55
 */

public class OrderController extends AbstractController{
    private static OrderFacade orderFacade = new OrderFacade();
    private static OrderDetailsFacade orderDetailFacade = new OrderDetailsFacade();

    public List<OrderDetail> getOrderDetails(String orderId){
        // get list of order detail Id
        Order order = new Order();
        orderFacade.findById(orderId, order);
        
        // get list of order detail object by list id
        List<OrderDetail> list = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = new OrderDetail();
        for( String id : order.getOrderDetailIdList() ){
            if( orderDetailFacade.findById(id, orderDetail) ){
                list.add(orderDetail);
            }
            else{
                System.out.println("Can not get order detail id: " + id);
            }
        }
        return list;
    }

    public Order getOrderById(String orderId){
        Order order = new Order();
        orderFacade.findById(orderId, order);
        return order;
    }

    /*
     * Get all order that has Status != COMPLETED
     */
    public List<Order> getCurrentOrderList(){
        List<Order> list = new ArrayList<Order>();
        BasicDBObject query = new BasicDBObject(Order.DB_FIELD_NAME_STATUS, new BasicDBObject("$ne", Types.State.COMPLETED.toString())); 
        orderFacade.findByQuery(query, list);
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
     * Update orderdetails list for order
     */
    public boolean updateOrderDetailList(Order order){
        return orderFacade.updateFieldValue(order.getId(), Order.DB_FIELD_NAME_ORDER_DETAIL_LIST, Types.formatListToString(order.getOrderDetailIdList()));
    }
}
