package com.luvsoft.MMI;

import java.util.List;

import com.luvsoft.MMI.Order.OrderInfo;
import com.luvsoft.MMI.components.OrderElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
        if( orderInfoList.isEmpty() ){
            // No Order in orderlist
            Label lbl = new Label(Language.NO_ORDER_IN_ORDER_LIST);
            vtcLayout.addComponent(lbl);
            this.setContent(vtcLayout);
            return;
        }
        for( OrderInfo orderInfo : orderInfoList ){
            OrderElement orderElement = new OrderElement(orderInfo.getOrderId());
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
     * - When user confirm order foods for the first time, an Order will be added to db as a new "current orders" for a table
     * - When a table changes state from other states to EMPTY, the associated Order will be set to "COMPLETED" 
     *   and can't be modified anymore
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
