package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.controllers.FloorController;
import com.luvsoft.controllers.FoodController;
import com.luvsoft.controllers.OrderController;
import com.luvsoft.entities.Food;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Table;

public class Adapter {
    public static List<OrderInfo> retrieveOrderInfoList(List<String> orderIdList){
        List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
        OrderController orderCtrl = new OrderController();
        FloorController floorCtrl = new FloorController();
        FoodController foodCtrl = new FoodController();
        for( String orderId : orderIdList ){
            Order order = orderCtrl.getOrderById(orderId);
            Table table = floorCtrl.getTableById(order.getTableId());
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setTableName(table.getName());

            // OrderDetails
            List<OrderDetailRecord> orderDetailRecordList = new ArrayList<OrderDetailRecord>();
            List<OrderDetail> orderDetailList = orderCtrl.getOrderDetails(orderId);
            for( OrderDetail orderDetail : orderDetailList ){
                OrderDetailRecord record = new OrderDetailRecord();
                Food food = foodCtrl.getFoodById(orderDetail.getFoodId());
                record.setFoodName(food.getName());
                record.setPrice(food.getPrice());
                record.setQuantity(orderDetail.getQuantity());
                record.setStatus(orderDetail.getState().toString());
                orderDetailRecordList.add(record);
            }
            orderInfo.setOrderDetailList(orderDetailRecordList);
        }
        return orderInfoList;
    }
}
