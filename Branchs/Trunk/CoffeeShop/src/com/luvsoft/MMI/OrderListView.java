package com.luvsoft.MMI;

import java.util.List;

import com.luvsoft.MMI.Order.OrderInfo;
import com.luvsoft.MMI.components.OrderElement;
import com.luvsoft.entities.Order;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author cuongvm-55
 *
 * This class is used to implement Order list View
 */
@SuppressWarnings("serial")
public class OrderListView extends Panel{
    private Panel panel;
    private VerticalLayout vtcLayout;
    
    // data
    private List<Order> orderList; // Current order list

    public OrderListView() {
        super();
        init();
    }

    public void init(){
        panel = new Panel();
        vtcLayout = new VerticalLayout();
        //vtcLayout.setSizeFull();
        panel.setSizeFull();
        panel.setStyleName("scrollable");
        loadOrderList();
        List<OrderInfo> orderInfoList = Adapter.retrieveOrderInfoList(orderList);
        /* for(int i=0;i<10;i++){
            OrderInfo order = new OrderInfo();
            order.setOrderId("12345");
            order.setTableName("Bàn 1");
            OrderDetailRecord record = new OrderDetailRecord();
            record.setFoodName("Cà phê đen đá");
            record.setPrice(50.00f);
            record.setQuantity(2);
            record.setStatus("WAITING");
            List<OrderDetailRecord> list = new ArrayList<OrderDetailRecord>();
            list.add(record);
            order.setOrderDetailList(list);
            orderInfoList.add(order);
        } */
        for( OrderInfo orderInfo : orderInfoList ){
            OrderElement orderElement = new OrderElement();
            orderElement.populate(orderInfo);
            HorizontalLayout topLine = new HorizontalLayout();
            topLine.setSizeFull();
            topLine.setStyleName("top-line");
            HorizontalLayout bottomLine = new HorizontalLayout();
            bottomLine.setSizeFull();
            bottomLine.setStyleName("bottom-line");
            vtcLayout.addComponents(topLine, orderElement, bottomLine);
        }
        //vtcLayout.setStyleName("scrollable");
        //vtcLayout.setSizeUndefined();
        this.setContent(vtcLayout);
        //panel.getContent().setSizeUndefined();
        //this.addComponent(panel);
    }
    
    /*
     * At a particular moment, there's only 1 Order corresponding to a Table
     * In DB, we should have two types of Orders:
     *  1. Saved orders - could not be modified anymore, that's all complete orders
     *     - Order state must be "COMPLETE"
     *  2. Current orders - user are working with these orders, add food, confirm paid,...
     *     - Order state < "COMPLETE"
     * - When a table changes state from EMPTY-->WAITING, a temporary Order will be created in MMI layer
     * - When user add food to a temporary order, it will be saved to DB as a new "current orders" for a table
     *      After that, we consider temporary as a "current orders" --> set state ...
     * - When a table changes state from other states to EMPTY, the associated Order in MMI layer will be deleted,
     *      "current orders" now is considered as a "saved orders" --> set state "COMPLETE"
     * 
     * This function loads all "current orders"
     */
    public void loadOrderList(){
        orderList = Adapter.getCurrentOrderList();
    }
    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
