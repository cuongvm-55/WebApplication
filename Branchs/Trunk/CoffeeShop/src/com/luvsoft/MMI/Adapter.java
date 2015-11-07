package com.luvsoft.MMI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Order.OrderDetailRecord;
import com.luvsoft.MMI.Order.OrderInfo;
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
    private static FloorController floorCtrl = new FloorController();
    private static OrderController orderCtrl = new OrderController();
    private static FoodController foodCtrl = new FoodController();

    public static List<OrderInfo> retrieveOrderInfoList(List<Order> orderList){
        System.out.println("retrieveOrderInfoList, with inputted orderList: " + orderList.toString());
        List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
        for( Order order : orderList ){
            Table table = floorCtrl.getTableById(order.getTableId());
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(order.getId());
            orderInfo.setTableName(Language.TABLE + " " + table.getNumber());

            // OrderDetails
            List<OrderDetailRecord> orderDetailRecordList = new ArrayList<OrderDetailRecord>();
            List<String> orderDetailIdList = order.getOrderDetailIdList();
            for( String orderDetailId : orderDetailIdList ){
                System.out.println(orderDetailId.toString());
                OrderDetail orderDetail = orderCtrl.getOrderDetailById(orderDetailId);
                OrderDetailRecord record = new OrderDetailRecord();
                Food food = foodCtrl.getFoodById(orderDetail.getFoodId());
                record.setOrderDetailId(orderDetail.getId());
                record.setFoodName(food.getName());
                record.setPrice(food.getPrice());
                record.setQuantity(orderDetail.getQuantity());
                record.setStatus(orderDetail.getState());
                orderDetailRecordList.add(record);
            }
            orderInfo.setOrderDetailList(orderDetailRecordList);
            orderInfoList.add(orderInfo);
        }
        return orderInfoList;
    }
    
    public static List<Floor> retrieveFloorList(){
        System.out.println("retrieveFloorList...");
        return floorCtrl.getAllFloor();
    }

    public static List<Table> retrieveTableList(List<String> tableIdList){
        List<Table> list = new ArrayList<Table>();
        for( String tableId : tableIdList ){
            Table table = floorCtrl.getTableById(tableId);
            list.add(table);
        }
        return list;
    }

    public static boolean changeTableState(String tableId, Types.State state){
        System.out.println("changeTableState tableId: " + tableId +", state: " + state);
        return floorCtrl.setTableStatus(tableId, state);
    }

    public static List<Order> getCurrentOrderList(){
        System.out.println("Get current order list...");
        return orderCtrl.getCurrentOrderList();
    }

    public static boolean changeOrderState(String orderId, Types.State state){
        return orderCtrl.setOrderStatus(orderId, state);
    }

    public static boolean addNewOrder(Order order){
        return orderCtrl.addNewOrder(order);
    }

    public static boolean removeOrder(String orderId){
        return orderCtrl.removeOrder(orderId);
    }

    public static boolean removeOrderDetail(String orderDetailId){
        return orderCtrl.removeOrderDetail(orderDetailId);
    }

    public static boolean addNewOrderDetail(OrderDetail orderDetail){
        return orderCtrl.addNewOrderDetail(orderDetail);
    }
    
    public static boolean updateOrderDetailList(Order order){
        return orderCtrl.updateOrderDetailList(order);
    }
    
    public static void createDataForMongoDB(){
        // Food list
        FoodFacade foodFacade = new FoodFacade();
        //foodFacade.removeAll();
        List<Food> foodList = new ArrayList<Food>();
        List<String> foodIdList = new ArrayList<String>();
        for(int i=0; i<5;i++){
            Food food = new Food();
            ObjectId id = new ObjectId();
            food.setId(id.toString());
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
        //categoryFacade.removeAll();
        for(int i=0;i<3;i++){
            Category category = new Category();
            ObjectId id = new ObjectId();
            category.setId(id.toString());
            category.setCode("CODE " +i);
            category.setName("Category "+i);
            category.setFoodIdList(foodIdList);
            
            categoryFacade.save(category);
        }
        
        // OrderDetail List
        OrderDetailsFacade orderDetailFC = new OrderDetailsFacade();
        //orderDetailFC.removeAll();
        List<OrderDetail> odDetailList = new ArrayList<OrderDetail>();
        List<String> odDetailIdList = new ArrayList<String>();
        int j=0;
        for(int i=0;i<10;i++){
            OrderDetail orderDetail = new OrderDetail();
            ObjectId id = new ObjectId();
            orderDetail.setId(id.toString());
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
        //tbFc.removeAll();
        List<Table> tbList = new ArrayList<Table>();
        List<String> tbIdList_Fl1 = new ArrayList<String>();
        List<String> tbIdList_Fl2 = new ArrayList<String>();
        
        for(int i=0;i<10;i++){
            Table table = new Table();
            ObjectId id = new ObjectId();
            table.setId(id.toString());
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
        //flFc.removeAll();
        for(int i=0;i<2;i++){
            Floor floor = new Floor();
            ObjectId id = new ObjectId();
            floor.setId(id.toString());
            floor.setCode("FLOOR "+i);
            floor.setNumber(""+i);
            if(i==0) floor.setTableIdList(tbIdList_Fl1);
            else floor.setTableIdList(tbIdList_Fl2);
            
            flFc.save(floor);
        }
        
        // Order List
        OrderFacade orderFC = new OrderFacade();
        //orderFC.removeAll();
        int i=0;
        for(Table table : tbList){
            if(table.getState() != Types.State.EMPTY){
                Order order = new Order();
                ObjectId id = new ObjectId();
                order.setId(id.toString());
                order.setNote("Note ..................." +i);
                order.setOrderDetailIdList(odDetailIdList);
                order.setPaidMoney(50.00f);
                LocalDateTime paidTime = LocalDateTime.now();
                order.setPaidTime(paidTime);
                order.setStaffName("Staff name "+i);
                if(i%4==0) order.setStatus(Types.State.CANCELED);
                else if(i%3==0) order.setStatus(Types.State.COMPLETED);
                else order.setStatus(Types.State.WAITING);
                order.setTableId(table.getId());
                order.setWaitingTime(i);
                order.setCreatingTime(paidTime);
                orderFC.save(order);
                i++;
            }
        }
    }
}
