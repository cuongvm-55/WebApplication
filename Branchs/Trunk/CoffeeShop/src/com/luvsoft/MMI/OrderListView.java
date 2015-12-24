package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.luvsoft.MMI.components.OrderElement;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

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
    public List<OrderElement> orderListElements = new ArrayList<OrderElement>();

    public OrderListView() {
        super();
    }

    @Override
    public void createView() {
        addStyleName("table-list-view");
        setWidth("100%");
        setHeightUndefined();
        removeAllComponents();
        if(orderListElements.isEmpty()) {
            loadContent();
        } else {
            for (OrderElement orderElement : orderListElements) {
                this.addComponent(orderElement);
            }
        }
    }

    @Override
    public void reloadView() {
        createView();
    }

    /**
     * This function is used to update any order view when it was added new
     * 
     * @param messageData
     */
    public void haveNewOrder(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doAddNewOrderElement(orderId);
    }

    /**
     * This function is used to update order, table when it performs a change action
     * 
     * @param messageData
     */
    public void doChangeTableAndOrder(String messageData) {
        System.out.println("messageData " + messageData);
        String srcOrderId, desOrderId;
        String str[] = messageData.split("::");
        srcOrderId = str[2];
        desOrderId = str[3];

        if(srcOrderId.equals(desOrderId)) {
            doUpdateOrderElementList(desOrderId);
        } else {
            doUpdateOrderElementList(srcOrderId);
            doUpdateOrderElementList(desOrderId);
        }
    }

    /**
     * This function is used to update any order view when it was canceled
     * 
     * @param messageData
     */
    public void haveCanceledOrder(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doRemoveOrderElementList(orderId);
    }

    /**
     * This function is used to update any order view when it was updated
     * 
     * @param messageData
     */
    public void haveNewOrderUpdated(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doUpdateOrderElementList(orderId);
    }

    /**
     * This function is used to update any order view when it was completed
     * 
     * @param messageData
     */
    public void haveNewOrderCompleted(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doRemoveOrderElementList(orderId);
    }

    public void foodWasCompleted(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doRemoveOrderElementList(orderId);
    }

    /**
     * This function is used to update any order view when it was paid
     * 
     * @param messageData
     */
    public void orderWasPaid(String messageData) {
        String orderId;
        String str[] = messageData.split("::");
        orderId = str[1];

        doRemoveOrderElementList(orderId);
    }

    /**
     * This function is used to update or AddNew order element to list
     * 
     * @param orderId
     */
    private void doUpdateOrderElementList(String orderId) {
        Order order = Adapter.getOrder(orderId);
        if(order.getStatus().equals(Types.State.UNPAID) || order.getStatus().equals(Types.State.PAID)) {
            return;
        }

        boolean isFounded = false;
        if(!orderListElements.isEmpty()) {
            for (OrderElement orderElement : orderListElements) {
                if(orderElement.getOrder().getId().equals(orderId)) {
                    OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
                    if(!orderInfo.getOrderDetailRecordList().isEmpty()) {
                        orderElement.reloadView();
                        orderElement.populate(orderInfo);
                    } else {
                        this.removeComponent(orderElement);
                    }
                    isFounded = true;
                    break;
                }
            }
        }
        if(!isFounded) {
            OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
            if(!orderInfo.getOrderDetailRecordList().isEmpty()) {
                OrderElement orderElement = new OrderElement(order);
                orderElement.setParentView(this);
                orderElement.populate(orderInfo);
    
                this.addComponent(orderElement);
                orderListElements.add(orderElement);
            }
        }
    }

    /**
     * This function is used to AddNew order element to list
     * 
     * @param orderId
     */
    private void doAddNewOrderElement(String orderId) {
        Order order = Adapter.getOrder(orderId);
        OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
        OrderElement orderElement = new OrderElement(order);
        orderElement.setParentView(this);
        orderElement.populate(orderInfo);

        this.addComponent(orderElement);
        orderListElements.add(orderElement);
    }

    /**
     * This function is used to remove order element in list
     * 
     * @param orderId
     */
    private void doRemoveOrderElementList(String orderId) {
        for(Iterator<OrderElement> it = orderListElements.iterator(); it.hasNext(); ) {
            OrderElement orderElement = it.next();
            Order order = orderElement.getOrder();
            if(order.getId().equals(orderId)) {
                OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
                orderElement.reloadView();
                orderElement.populate(orderInfo);

                this.removeComponent(orderElement);
                it.remove();
                break;
            }
        }
    }

    private void loadContent() {
        loadOrderList();
        if( orderList == null || orderList.isEmpty() ){
            // No Order in orderlist
            Label lbl = new Label(Language.NO_ORDER_IN_ORDER_LIST);
            lbl.addStyleName(ValoTheme.LABEL_HUGE);
            this.addComponent(lbl);
            return;
        }
        for( Order order : orderList ){
            OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
            OrderElement orderElement = new OrderElement(order);
            orderElement.setParentView(this);
            orderElement.populate(orderInfo);
            this.addComponents(orderElement);
            orderListElements.add(orderElement);
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
