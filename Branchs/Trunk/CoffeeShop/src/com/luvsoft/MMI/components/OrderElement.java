package com.luvsoft.MMI.components;

import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.OrderListView;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.order.OrderDetailRecord;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.threads.NewOrderManager;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Food;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OrderElement extends VerticalLayout implements ViewInterface {
    /*
     * This is a VerticalLayout contains info of an order
     */
    private Label lbTableName;
    private Table tbOrderInfos;
    private Button btnConfFinish;
    //OrderDetailRecord orderInfo;
    private List<OrderDetailRecord> orderDetailList;
    private Order order;
    private OrderListView parentView;
    private TextArea txtNote;

    public OrderElement(Order _order){
        super();
        createView();
        this.order = _order;
    }
    
    public void populate(OrderInfo orderInfo){
        orderDetailList = orderInfo.getOrderDetailRecordList();
        if( orderDetailList == null )
        {
            // just return
            return;
        }
        lbTableName.setValue(orderInfo.getTableName());
        txtNote.setValue(orderInfo.getNote());
        
        tbOrderInfos.clear();
        for( int i = 0; i < orderDetailList.size(); i++ ){
            Integer itemId = new Integer(i);
            OrderDetailRecord orderDetail = orderDetailList.get(i);
            ComboBox select = new ComboBox();
            select.setNullSelectionAllowed(false);
            select.setScrollToSelectedItem(true);
            select.setTextInputAllowed(false);
            select.addItem(Language.CANCELED/*Types.State.CANCELED.toString()*/);
            select.addItem(Language.WAITING/*Types.State.WAITING.toString()*/);
            select.addItem(Language.COMPLETED/*Types.State.COMPLETED.toString()*/);
            select.setRequired(true);
            select.select(Types.StateToLanguageString(orderDetail.getStatus()));
            select.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    // Set order detail status
                    String state = event.getProperty().getValue().toString();
                    if( !Adapter.changeOrderDetailState(orderDetail.getOrderDetailId(), Types.StringToState(state)) ){
                        System.out.println("Cannot change order detail state");
                    }
                    else{
                        // notify one food has been completed
                        // Broadcast order change event
                        if( state.equals(Language.COMPLETED) ){
                            OrderDetail od = Adapter.getOrderDetailById(orderDetail.getOrderDetailId());
                            Food food = Adapter.getFoodById(od.getFoodId());
                            Broadcaster.broadcast(CoffeeshopUI.FOOD_WAS_COMPLETED + "::"
                                    + "<i><b>" +orderInfo.getTableName() + "</b></i><br/><i>"
                                    + Language.FOOD + " <b>" +food.getName()+"</b> " + Language.HAS_BEEN_COMPLETED + "</i>");
                            }
                    }
                }
            });
            tbOrderInfos.addItem(new Object[]{
                    new Integer(i),
                    orderDetail.getFoodName(),
                    orderDetail.getQuantity(),
                    select,
                    orderDetail.getOrderDetailId()
                    }, itemId);
        }
        tbOrderInfos.setVisibleColumns(Language.SEQUENCE,Language.FOOD_NAME,Language.QUANTITY,Language.STATUS);
        tbOrderInfos.setPageLength(orderDetailList.size());
        //tbOrderInfos.setCacheRate ( 0.1 );
        //tbOrderInfos.setEditable(true);
    }

    @Override
    public void createView() {
        this.setSpacing(true);

        lbTableName = new Label();
        tbOrderInfos = new Table();
        btnConfFinish = new Button(Language.ALL_DONE);
        btnConfFinish.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfFinish.addStyleName(ValoTheme.BUTTON_HUGE);

        lbTableName.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lbTableName.addStyleName(ValoTheme.LABEL_BOLD);
        lbTableName.addStyleName(ValoTheme.LABEL_HUGE);
        lbTableName.setWidth("100%");

        // Note text field
        txtNote = new TextArea(Language.NOTE);
        txtNote.addStyleName("bold large FONT_TAHOMA");
        txtNote.setSizeFull();

        tbOrderInfos.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderInfos.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.STATUS, ComboBox.class, null);
        tbOrderInfos.addContainerProperty(new String("orderdetailId"), String.class, null);
        tbOrderInfos.setResponsive(true);
        tbOrderInfos.setSizeFull();

        tbOrderInfos.setColumnExpandRatio(Language.SEQUENCE, 0.5f);
        tbOrderInfos.setColumnExpandRatio(Language.FOOD_NAME, 3.9f);
        tbOrderInfos.setColumnExpandRatio(Language.STATUS, 1.4f);
        tbOrderInfos.setColumnExpandRatio(Language.QUANTITY, 0.7f);

        btnConfFinish.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Finish click!, OrderId: " + order.getId());
                // Save the note
                if( !order.getNote().equals(txtNote.getValue()) ){
                    Adapter.updateFieldValueOfOrder(order.getId(), Order.DB_FIELD_NAME_NOTE, txtNote.getValue());
                }

                // Update order if staffName is different
                if( getSession() != null &&
                        getSession().getAttribute("user") != null ){
                    String staffName = getSession().getAttribute("user").toString();
                    if (!staffName.equals(order.getStaffNameConfirmOrderFinish())) {
                        Adapter.updateFieldValueOfOrder(order.getId(), Order.DB_FIELD_NAME_STAFF_NAME_CONFIRM_ORDER_FINISH, staffName);
                    }
                }
                // If all order details are CANCELED, we cancel this order
                List<String> orderDetailIdList = order.getOrderDetailIdList();
                boolean allOrderDetailCanceled = true;
                for(int i=0; i<orderDetailIdList.size();i++){
                    if( Adapter.getOrderDetailById(orderDetailIdList.get(i)).getState() != Types.State.CANCELED ){
                        allOrderDetailCanceled = false;
                        break;
                    }
                }
                if( allOrderDetailCanceled ){
                    // Set status of order to be CANCELED
                    if( Adapter.changeOrderState(order.getId(), Types.State.CANCELED) ){
                        // Set table status to be EMPTY
                        Adapter.changeTableState(order.getTableId(), Types.State.EMPTY);
                        parentView.reloadView();
                    }
                    else{
                        System.out.println("Fail to cancel order: " + order.getId());
                    }
                }
                else if( Adapter.changeOrderState(order.getId(), Types.State.UNPAID) ){
                    // Set status of order to be UNPAID
                    // Set all order details to be COMPLETED except CANCELED one
                    for( String orderDetailId : order.getOrderDetailIdList() ){
                        if(  Adapter.getOrderDetailById(orderDetailId) != null && Adapter.getOrderDetailById(orderDetailId).getState() != Types.State.CANCELED ){
                            Adapter.changeOrderDetailState(orderDetailId, Types.State.COMPLETED);
                        }
                    }

                    // Set table status to be UNPAID
                    Adapter.changeTableState(order.getTableId(), Types.State.UNPAID);

                    OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
                    Broadcaster.broadcast(CoffeeshopUI.ORDER_COMPLETED_MESSAGE+"::"+orderInfo.getTableName());
                    NewOrderManager.interruptWaitingOrderThread(order);
                }
            }
        });

        this.addComponents(lbTableName, txtNote, tbOrderInfos, btnConfFinish);
        this.setComponentAlignment(tbOrderInfos, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnConfFinish, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void reloadView() {
        
    }

    @Override
    public ViewInterface getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        this.parentView = (OrderListView) parentView;
    }
}
