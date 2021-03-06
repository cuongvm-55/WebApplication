package com.luvsoft.MMI.components;

import java.util.List;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
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
    private ExpandingTextArea txtNote;

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
            NativeSelect select = new NativeSelect();
            select.setNullSelectionAllowed(false);
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
                                    + Language.FOOD + " <b>" +food.getName()+"</b> " + Language.HAS_BEEN_COMPLETED + "</i>::" + order.getId());
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
    }

    @Override
    public void createView() {
        this.setSpacing(true);

        removeAllComponents();

        lbTableName = new Label();
        tbOrderInfos = new Table();
        btnConfFinish = new Button(Language.ALL_DONE);
        btnConfFinish.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfFinish.addStyleName("BUTTON_GIGANTIC");

        lbTableName.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE FONT_OVERSIZE");
        lbTableName.addStyleName(ValoTheme.LABEL_BOLD);
        lbTableName.setWidth("100%");

        // Note text field
        txtNote = new ExpandingTextArea(Language.NOTE);
        txtNote.addStyleName("bold FONT_OVERSIZE TEXT_RED FONT_TAHOMA");
        txtNote.setWidth("100%");

        tbOrderInfos.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderInfos.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.STATUS, NativeSelect.class, null);
        tbOrderInfos.addContainerProperty(new String("orderdetailId"), String.class, null);
        tbOrderInfos.setResponsive(true);
        tbOrderInfos.setSizeFull();

        tbOrderInfos.setColumnExpandRatio(Language.SEQUENCE, 0.5f);
        tbOrderInfos.setColumnExpandRatio(Language.FOOD_NAME, 3.4f);
        tbOrderInfos.setColumnExpandRatio(Language.STATUS, 1.9f);
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
                    // save staff name who confirm paid (cancel in this case)
                    if( getSession() != null &&
                            getSession().getAttribute("user") != null ){
                        String staffName = getSession().getAttribute("user").toString();
                        if (!staffName.equals(order.getStaffNameConfirmPaid())) {
                            order.setStaffNameConfirmPaid(staffName);
                            Adapter.updateFieldValueOfOrder(order.getId(), Order.DB_FIELD_NAME_STAFF_NAME_CONFIRM_PAID, order.getStaffNameConfirmPaid());
                        }
                    }
                    if( Adapter.changeOrderState(order.getId(), Types.State.CANCELED) ){
                        // Set table status to be EMPTY
                        Adapter.changeTableState(order.getTableId(), Types.State.EMPTY);
                        Adapter.setCheckSum(order);
                        Broadcaster.broadcast(CoffeeshopUI.CANCELED_ORDER+"::"+order.getTableId() + "::" + order.getId());
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
                    Adapter.setCheckSum(order);
                    Broadcaster.broadcast(CoffeeshopUI.ORDER_COMPLETED_MESSAGE+"::"+order.getTableId() + "::" + order.getId());
                }
                // We always interrupt thread when order is finished
                NewOrderManager.interruptWaitingOrderThread(order);
            }
        });

        HorizontalLayout bottomLine = new HorizontalLayout();
        bottomLine.setSizeFull();
        bottomLine.setStyleName("bottom-line");
        this.addComponents(lbTableName, txtNote, tbOrderInfos, btnConfFinish, bottomLine);
        this.setComponentAlignment(tbOrderInfos, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnConfFinish, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void reloadView() {
        createView();
    }

    @Override
    public ViewInterface getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        this.parentView = (OrderListView) parentView;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
