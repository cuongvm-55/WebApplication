package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.components.OrderElement;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author cuongvm-55
 *
 * This class is used to implement Order list View
 */
@SuppressWarnings("serial")
public class OrderListView extends Panel implements ViewInterface{
    private VerticalLayout vtcLayout;
    public static OrderListView orderListView;

    // data
    private List<Order> orderList; // Current order list

    public OrderListView() {
        super();
    }

    @Override
    public void createView() {
        addStyleName("table-list-view");
        vtcLayout = new VerticalLayout();
        loadContent();
        this.setContent(vtcLayout);
        this.setSizeFull();
    }

    @Override
    public void reloadView() {
        createView();
    }

    public void haveNewOrder(String tableNumber) {
        reloadView();

        Notification notify = new Notification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.TABLE + " " + tableNumber + Language.ORDERED + "</i>",
                Notification.Type.WARNING_MESSAGE, true);
        notify.setPosition(Position.BOTTOM_RIGHT);
        notify.show(Page.getCurrent());
    }

    public void haveCanceledOrder(String tableNumber) {
        reloadView();

        Notification notify = new Notification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.ORDER_IN_TABLE + tableNumber + Language.HAS_BEEN_CANCELED + "</i>",
                Notification.Type.WARNING_MESSAGE, true);
        notify.setPosition(Position.BOTTOM_RIGHT);
        notify.show(Page.getCurrent());
    }

    private void loadContent() {
        loadOrderList();
        if( orderList.isEmpty() ){
            // No Order in orderlist
            Label lbl = new Label(Language.NO_ORDER_IN_ORDER_LIST);
            vtcLayout.addComponent(lbl);
            this.setContent(vtcLayout);
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
            vtcLayout.addComponents(/*topLine,*/ orderElement, bottomLine);
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
     *     - Order state must be "PAID"
     *  2. Current orders - user are working with these orders, add food, confirm paid,...
     *     - Order state < "PAID"
     * - When user confirm order foods for the first time, an Order will be added to db as a new "current orders" for a table
     * - When a table changes state from other states to EMPTY, the associated Order will be set to "COMPLETED" 
     *   and can't be modified anymore
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
