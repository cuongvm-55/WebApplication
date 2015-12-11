package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.luvsoft.MMI.order.OrderDetailRecord;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.controllers.CategoryController;
import com.luvsoft.controllers.ConfigurationController;
import com.luvsoft.controllers.FloorController;
import com.luvsoft.controllers.FoodController;
import com.luvsoft.controllers.OrderController;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Configuration;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Food;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;

public class Adapter {
    private static FloorController floorCtrl = new FloorController();
    private static OrderController orderCtrl = new OrderController();
    private static FoodController foodCtrl = new FoodController();
    private static CategoryController categoryCtrl = new CategoryController();
    private static ConfigurationController configCtrl = new ConfigurationController();

    public static OrderInfo retrieveOrderInfo(Order order){
        System.out.println("retrieveOrderInfo, orderId: " + order.getId());
        Table table = floorCtrl.getTableById(order.getTableId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(order.getId());
        orderInfo.setTableName(Language.TABLE + " " + table.getNumber());
        orderInfo.setNote(order.getNote());
        // OrderDetails
        List<OrderDetailRecord> orderDetailRecordList = new ArrayList<OrderDetailRecord>();
        List<String> orderDetailIdList = order.getOrderDetailIdList();
        for( String orderDetailId : orderDetailIdList ){
            OrderDetail orderDetail = new OrderDetail();
            if( !orderCtrl.getOrderDetailById(orderDetailId, orderDetail)){
                System.out.println("Fail to get orderdetail id: " + orderDetailId);
                continue; // set next index
            }
            OrderDetailRecord record = new OrderDetailRecord();
            Food food = foodCtrl.getFoodById(orderDetail.getFoodId());
            record.setFoodId(food.getId());
            record.setOrderDetailId(orderDetail.getId());
            record.setFoodName(food.getName());
            record.setPrice(food.getPrice());
            record.setQuantity(orderDetail.getQuantity());
            record.setStatus(orderDetail.getState());
            record.setPreviousStatus(orderDetail.getState());
            record.setOriginalStatus(orderDetail.getState());
            record.setOriginalQuantity(orderDetail.getQuantity());
            orderDetailRecordList.add(record);
        }
        orderInfo.setOrderDetailRecordList(orderDetailRecordList);
        return orderInfo;
    }

    public static OrderDetail getOrderDetailById(String odDtId){
        OrderDetail orderDtl = new OrderDetail();
        orderCtrl.getOrderDetailById(odDtId, orderDtl);
        return orderDtl;
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
        Collections.sort(list);
        return list;
    }

    public static List<Category> retrieveCategoryList() {
        List<Category> list = categoryCtrl.getAllCategory();

        for (Category category : list) {
            List<Food> listOfFood = new ArrayList<Food>();
            for (String foodId : category.getFoodIdList()) {
                Food food = foodCtrl.getFoodById(foodId);
 
                // If food is not empty
                if(!food.getId().equals("")) {
                            listOfFood.add(food);
                }
            }
            category.setListOfFoodByCategory(listOfFood);
         } 
         return list;
}

    public static boolean changeTableState(String tableId, Types.State state){
        System.out.println("changeTableState tableId: " + tableId +", state: " + state);
        return floorCtrl.setTableStatus(tableId, state);
    }

    public static List<Order> getOrderListWithStates(List<Types.State> states, Date begDate, Date endDate){
        System.out.println("Get order list with state: " + states.toString());
        return orderCtrl.getOrderListWithStates(states, begDate, endDate);
    }

    public static List<Order> getOrderListIgnoreStates(List<Types.State> states, Date begDate, Date endDate){
        System.out.println("Get order list ignore state: " + states.toString());
        return orderCtrl.getOrderListIgnoreStates(states, begDate, endDate);
    }

    public static boolean updateFieldValueOfOrder(String orderId, String fieldName, Object fieldVale){
        return orderCtrl.updateFieldValueOfOrder(orderId, fieldName, fieldVale);
    }

    public static boolean updateOrder(String orderId, Order order){
        return orderCtrl.updateOrder(orderId, order);
    }
    
    public static boolean updateOrderDetail(String orderDetailId, OrderDetail orderDetail){
        return orderCtrl.updateOrderDetail(orderDetailId, orderDetail);
    }
    
    public static boolean updateFieldValueOfOrderDetail(String orderId, String fieldName, String fieldVale){
        return orderCtrl.updateFieldValueOfOrderDetail(orderId, fieldName, fieldVale);
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
        return orderCtrl.updateFieldValueOfOrder(order.getId(),
                Order.DB_FIELD_NAME_ORDER_DETAIL_LIST,
                Types.formatListToString(order.getOrderDetailIdList()));
    }
    
    public static boolean changeOrderState(String orderId, Types.State state){
        return orderCtrl.updateFieldValueOfOrder(orderId, Order.DB_FIELD_NAME_STATUS, state.toString());
    }

    public static boolean changeOrderDetailState(String orderDetailId, Types.State state){
        return orderCtrl.updateFieldValueOfOrderDetail(orderDetailId, OrderDetail.DB_FIELD_NAME_STATE, state.toString());
    }

    public static Category getCategoryById(String cateId){
        return categoryCtrl.getCategoryById(cateId);
    }

    public static boolean addNewCategory(Category category){
        return categoryCtrl.addNewCategory(category);
    }

    public static boolean removeCategory(String cateId){
        return categoryCtrl.removeCategory(cateId);
    }

    public static boolean removeFood(String foodId){
        return foodCtrl.removeFood(foodId);
    }

    public static boolean addNewFood(Food food){
        return foodCtrl.addNewFood(food);
    }

    public static boolean updateFood(String foodId, Food food){
        return foodCtrl.updateFood(foodId, food);
    }

    public static boolean updateCategory(String cateId, Category cate){
        return categoryCtrl.updateCategory(cateId, cate);
    }

    public static boolean isCategoryNameExist(String categoryName){
        List<Category> cateList = Adapter.retrieveCategoryList();
        if( cateList != null ){
            for( Category cate : cateList ){
                if( cate.getName().equals(categoryName) ){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean addNewFloor(Floor floor){
        return floorCtrl.addNewFloor(floor);
    }

    public static boolean updateFloor(String floorId, Floor floor){
        return floorCtrl.updateFloor(floorId, floor);
    }

    public static boolean addNewTable(Table table){
        return floorCtrl.addNewTable(table);
    }

    public static boolean updateTable(String tableId, Table table){
        return floorCtrl.updateTable(tableId, table);
    }
    
    public static boolean isFloorNumberExist(String floorNbr){
        List<Floor> floorList = Adapter.retrieveFloorList();
        if( floorList != null ){
            for( Floor floor : floorList ){
                if( floor.getNumber().equals(floorNbr) ){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean removeTable(String tableId){
        return floorCtrl.removeTable(tableId);
    }
    
    public static boolean removeFloor(String floorId){
        return floorCtrl.removeFloor(floorId);
    }
    
    public static Table getTableById(String tableId){
        return floorCtrl.getTableById(tableId);
    }
    
    public static Floor getFloorByName(String floorName){
        List<Floor> floorList = Adapter.retrieveFloorList();
        if( floorList != null ){
            for( Floor floor : floorList ){
                if( (Language.FLOOR + " " + floor.getNumber()).equals(floorName) ){
                    return floor;
                }
            }
        }
        return null;
    }

    public static Floor getFloorOfTable(String tableId){
        List<Floor> floorList = Adapter.retrieveFloorList();
        if( floorList != null ){
            for( Floor floor : floorList ){
                if( floor.getTableIdList().contains(tableId) ){
                    return floor;
                }
            }
        }
        return null;
    }
    
    public static Category getCategoryByName(String categoryName){
        List<Category> cateList = Adapter.retrieveCategoryList();
        if( cateList != null ){
            for( Category cate : cateList ){
                if( cate.getName().equals(categoryName) ){
                    return cate;
                }
            }
        }
        return null;
    }

    public static Category getCategoryOfFood(String foodId){
        List<Category> cateList = Adapter.retrieveCategoryList();
        if( cateList != null ){
            for( Category cate : cateList ){
                if( cate.getFoodIdList().contains(foodId) ){
                    return cate;
                }
            }
        }
        return null;
    }

    public static Configuration getConfiguration(){
        return configCtrl.getConfiguration();
    }

    public static boolean loginSUPReq(String pincode){
        return configCtrl.loginSUPReq(pincode);
    }
    public static boolean loginOperatorReq(String pincode){
        return configCtrl.loginOperatorReq(pincode);
    }
    
    public static boolean updateConfiguration(Configuration conf){
        return configCtrl.updateConfiguration( conf);
    }
}
