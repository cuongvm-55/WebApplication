package com.luvsoft.MMI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.controllers.FloorController;
import com.luvsoft.controllers.FoodController;
import com.luvsoft.controllers.OrderController;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Food;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.luvsoft.facades.CategoryFacade;
import com.luvsoft.facades.FloorFacade;
import com.luvsoft.facades.FoodFacade;
import com.luvsoft.facades.OrderDetailsFacade;
import com.luvsoft.facades.OrderFacade;
import com.luvsoft.facades.TableFacade;

public class Adapter {
    public static List<OrderInfo> retrieveOrderInfoList(List<Order> orderList){
        List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
        OrderController orderCtrl = new OrderController();
        FloorController floorCtrl = new FloorController();
        FoodController foodCtrl = new FoodController();
        for( Order order : orderList ){
            Table table = floorCtrl.getTableById(order.getTableId());
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(order.getId());
            orderInfo.setTableName(Language.TABLE + " " + table.getNumber());

            // OrderDetails
            List<OrderDetailRecord> orderDetailRecordList = new ArrayList<OrderDetailRecord>();
            List<OrderDetail> orderDetailList = orderCtrl.getOrderDetails(order.getId());
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
    
    public static List<Floor> retrieveFloorList(){
        FloorController floorCtrl = new FloorController();
        return floorCtrl.getAllFloor();
    }

    public static List<Table> retrieveTableList(List<String> tableIdList){
        FloorController floorCtrl = new FloorController();
        List<Table> list = new ArrayList<Table>();
        for( String tableId : tableIdList ){
            Table table = floorCtrl.getTableById(tableId);
            list.add(table);
        }
        return list;
    }
    
    public static void createDataForMongoDB(){
        // Food list
        FoodFacade foodFacade = new FoodFacade();
        List<Food> foodList = new ArrayList<Food>();
        List<String> foodIdList = new ArrayList<String>();
        for(int i=0; i<5;i++){
            Food food = new Food();
            food.setId(""+i);
            food.setCode("FOOD " + i);
            food.setName("Food name " + i);
            food.setPrice(50.00f);
            foodList.add(food);
            foodFacade.save(food);
        }
        foodFacade.findAll(foodList);
        for(Food food : foodList){
            foodIdList.add(food.getId());
        }

        // Category list
        CategoryFacade categoryFacade = new CategoryFacade();
        for(int i=0;i<3;i++){
            Category category = new Category();
            category.setId(""+i);
            category.setCode("CODE " +i);
            category.setName("Category "+i);
            category.setFoodIdList(foodIdList);
            
            categoryFacade.save(category);
        }
        
        // OrderDetail List
        OrderDetailsFacade orderDetailFC = new OrderDetailsFacade();
        List<OrderDetail> odDetailList = new ArrayList<OrderDetail>();
        List<String> odDetailIdList = new ArrayList<String>();
        int j=0;
        for(int i=0;i<10;i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId("" +i);
            orderDetail.setQuantity(i+1);
            if(i%4==0) orderDetail.setState(Types.State.CANCELED);
            else if(i%3==0) orderDetail.setState(Types.State.COMPLETED);
            else orderDetail.setState(Types.State.WAITING);
            if(j >= foodIdList.size() ) j=0;
            orderDetail.setFoodId(foodIdList.get(j));
            
            orderDetailFC.save(orderDetail);
            j++;
        }
        orderDetailFC.findAll(odDetailList);
        for(OrderDetail odDetail : odDetailList){
            odDetailIdList.add(odDetail.getId());
        }
        
        // Table List
        TableFacade tbFc = new TableFacade();
        List<Table> tbList = new ArrayList<Table>();
        List<String> tbIdList_Fl1 = new ArrayList<String>();
        List<String> tbIdList_Fl2 = new ArrayList<String>();
        
        for(int i=0;i<10;i++){
            Table table = new Table();
            table.setId(""+i);
            table.setNumber(""+i);
            table.setCode("TABLE "+i);
            if(i%4==0) table.setState(Types.State.PAID);
            else if(i%3==0) table.setState(Types.State.EMPTY);
            else table.setState(Types.State.WAITING);
            
            tbFc.save(table);
        }
        
        tbFc.findAll(tbList);
        for(Table table : tbList){
            int number = Integer.parseInt(table.getNumber());
            if(number < 6) tbIdList_Fl1.add(table.getId());
            else{
                tbIdList_Fl2.add(table.getId());
            }
        }
        
        // Floor List
        FloorFacade flFc = new FloorFacade();
        for(int i=0;i<2;i++){
            Floor floor = new Floor();
            floor.setId(""+i);
            floor.setCode("FLOOR "+i);
            floor.setNumber(""+i);
            if(i==0) floor.setTableIdList(tbIdList_Fl1);
            else floor.setTableIdList(tbIdList_Fl2);
            
            flFc.save(floor);
        }
        
        // Order List
        OrderFacade orderFC = new OrderFacade();
        int i=0;
        for(Table table : tbList){
            if(table.getState() != Types.State.EMPTY){
                Order order = new Order();
                order.setId(""+i);
                order.setNote("Note ..................." +i);
                order.setOrderDetailIdList(odDetailIdList);
                order.setPaidMoney(50.00f);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Types.DATE_TIME_PARTTERN);
                LocalDateTime paidTime = LocalDateTime.parse("28/10/2015 01:51:01", formatter);
                order.setPaidTime(paidTime);
                order.setStaffName("Staff name "+i);
                if(i%4==0) order.setStatus(Types.State.CANCELED);
                else if(i%3==0) order.setStatus(Types.State.COMPLETED);
                else order.setStatus(Types.State.WAITING);
                order.setTableId(table.getId());
                order.setWaitingTime(paidTime);
                
                orderFC.save(order);
                i++;
            }
        }
    }
}
