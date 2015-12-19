package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.components.LuvsoftNotification;
import com.luvsoft.MMI.components.OrderElement;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author cuongvm-55
 *
 * This class is used to implement Order list View
 */
@SuppressWarnings("serial")
public class OrderListView extends VerticalLayout implements ViewInterface{
    public static OrderListView orderListView;

    // data
    private List<Order> orderList; // Current order list

    public OrderListView() {
        super();
    }

    @Override
    public void createView() {
        addStyleName("table-list-view");
        setWidth("100%");
        setHeightUndefined();
        loadContent();
    }

    @Override
    public void reloadView() {
        createView();
    }

    public void haveNewOrder(String tableNumber) {
        reloadView();

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>",
                "<i>" + Language.TABLE + " " + tableNumber + Language.ORDERED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    public void haveCanceledOrder(String tableNumber) {
        reloadView();

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.ORDER_IN_TABLE + tableNumber + Language.HAS_BEEN_CANCELED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    public void haveNewOrderUpdated(String tableNumber) {
        reloadView();

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.ORDER_IN_TABLE + tableNumber + Language.HAS_BEEN_UPDATED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }
    private void loadContent() {
        loadOrderList();
        if( orderList == null || orderList.isEmpty() ){
            // No Order in orderlist
            Label lbl = new Label(Language.NO_ORDER_IN_ORDER_LIST);
            this.addComponent(lbl);
            return;
        }
        for( Order order : orderList ){
            OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
            OrderElement orderElement = new OrderElement(order);
            orderElement.setParentView(this);
            orderElement.populate(orderInfo);
            HorizontalLayout bottomLine = new HorizontalLayout();
            bottomLine.setSizeFull();
            bottomLine.setStyleName("bottom-line");
            this.addComponents(/*topLine,*/ orderElement, bottomLine);
        }
    }

    @Override
    public ViewInterface getParentView() {
        // Nothing to do
        return null;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        // Nothing to do
    }

    /*
     * At a particular moment, there's only 1 Order corresponding to a Table
     * In DB, we should have two types of Orders:
     *  1. Saved orders - could not be modified anymore, that's all complete orders
     *     - Order state must be "PAID" or "CANCELED"
     *  2. Current orders - user are working with these orders, add food, confirm paid,...
     *     - Order state < "PAID"
     * - When user confirm order foods for the first time, an Order will be added to db as a new "current orders" for a table
     * - When user cancel or confirm paid, the associated Order will be set to "PAID", "CANCELED" and can't be modified anymore
     * 
     * This function loads all "current orders" that has state WAITING
     */
    public void loadOrderList(){
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.WAITING);
        orderList = Adapter.getOrderListWithStates(states, null, null);
    }
    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
