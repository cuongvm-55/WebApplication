package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.facades.OrderDetailsFacade;
import com.luvsoft.facades.OrderFacade;

/*
 * @author cuongvm-55
 */

public class OrderController extends AbstractController{
    public List<OrderDetail> getOrderDetails(String orderId){
        // get list of order detail Id
        OrderFacade orderFacade = new OrderFacade();
        Order order = new Order();
        orderFacade.findById(orderId, order);
        
        // get list of order detail object by list id
        List<OrderDetail> list = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = new OrderDetail();
        OrderDetailsFacade orderDetailFacade = new OrderDetailsFacade();
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
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.findById(orderId, order);
        return order;
    }

}
