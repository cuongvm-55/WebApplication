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
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
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
        for( int i = 0; i < orderDetailList.size(); i++ ){
            Integer itemId = new Integer(i);
            OrderDetailRecord orderDetail = orderDetailList.get(i);
            tbOrderInfos.addItem(new Object[]{new Integer(i), orderDetail.getFoodName(),
                    orderDetail.getQuantity(),
                    Types.StateToLanguageString(orderDetail.getStatus()),
                    orderDetail.getOrderDetailId()}, itemId);
        }
        tbOrderInfos.setVisibleColumns(Language.SEQUENCE,Language.FOOD_NAME,Language.QUANTITY,Language.STATUS);
        tbOrderInfos.setTableFieldFactory(new DefaultFieldFactory(){
            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public Field createField(Container container, Object itemId,
                    Object propertyId, Component uiContext) {
                if (Language.STATUS.equals(propertyId)) {
                    ComboBox select = new ComboBox();
                    select.setNullSelectionAllowed(false);
                    select.setScrollToSelectedItem(true);
                    select.setTextInputAllowed(false);
                    select.addItem(Language.CANCELED/*Types.State.CANCELED.toString()*/);
                    select.addItem(Language.WAITING/*Types.State.WAITING.toString()*/);
                    select.addItem(Language.COMPLETED/*Types.State.COMPLETED.toString()*/);
                    select.setRequired(true);

                    select.addValueChangeListener(new ValueChangeListener() {
                        @Override
                        public void valueChange(ValueChangeEvent event) {
                            // Set order detail status
                            Item item = tbOrderInfos.getItem(itemId);
                            String orderDetailId = item.getItemProperty("orderdetailId").getValue().toString();
                            String state = event.getProperty().getValue().toString();
                            System.out.println("Switch orderDetailId: " +orderDetailId+" to state: " + event.getProperty().getValue());
                            if( !Adapter.changeOrderDetailState(orderDetailId, Types.StringToState(state)) ){
                                System.out.println("Cannot change order detail state");
                            }
                        }
                    });
                    return select;
                }
                return null;
                //return super.createField(container, itemId, propertyId, uiContext);
            }
        });

        tbOrderInfos.setEditable(true);
    }

    @Override
    public void createView() {
        lbTableName = new Label();
        tbOrderInfos = new Table();
        btnConfFinish = new Button(Language.ALL_DONE);

        lbTableName.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lbTableName.addStyleName(ValoTheme.LABEL_BOLD);
        lbTableName.addStyleName(ValoTheme.LABEL_HUGE);
        lbTableName.setWidth("100%");

        tbOrderInfos.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderInfos.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.STATUS, String.class, null);
        tbOrderInfos.addContainerProperty(new String("orderdetailId"), String.class, null);
        tbOrderInfos.setPageLength(tbOrderInfos.size());
        tbOrderInfos.setResponsive(true);
        tbOrderInfos.setSizeFull();

        btnConfFinish.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfFinish.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Finish click!, OrderId: " + order.getId());
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

        this.addComponents(lbTableName, tbOrderInfos, btnConfFinish);
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
