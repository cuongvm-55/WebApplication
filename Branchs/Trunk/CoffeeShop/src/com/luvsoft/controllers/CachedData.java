package com.luvsoft.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;

public class CachedData {
    // dynamic data (orders, order details)
    //protected static List<Order> orderList = null;
    //protected static Map<Order, List<OrderDetail>> orderMaps = new HashMap<Order, List<OrderDetail>>(); // <Order, OrderDetailList>

    // static data (floors, tables, categories, foods)
    //protected static List<Floor> floorList = null;
    // protected static Map<String, List<Table>> floorMaps = new HashMap<String, List<Table>>(); // <FloorId, TableList>
    
    protected static List<Category> categoryList = null;
    protected static Map<String, List<Food>> categoriesMaps = new HashMap<String, List<Food>>();; // <CategoryIds, FoodList>
}
