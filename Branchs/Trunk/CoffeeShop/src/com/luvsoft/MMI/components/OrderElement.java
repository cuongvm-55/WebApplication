package com.luvsoft.MMI.components;

import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.Order.OrderDetailRecord;
import com.luvsoft.MMI.Order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class OrderElement extends VerticalLayout{
    /*
     * This is a VerticalLayout contains info of an order
     */
    private Label lbTableName;
    private Table tbOrderInfos;
    private Button btnConfFinish;
    //OrderDetailRecord orderInfo;
    private List<OrderDetailRecord> orderDetailList;
    private Order order;
    public OrderElement(Order _order){
        super();
        lbTableName = new Label();
        tbOrderInfos = new Table();
        btnConfFinish = new Button(Language.ALL_DONE);
        lbTableName.setStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lbTableName.setWidth("100%");

        tbOrderInfos.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderInfos.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderInfos.addContainerProperty(Language.STATUS, String.class, null);
        tbOrderInfos.addContainerProperty(new String("orderdetailId"), String.class, null);
        tbOrderInfos.setPageLength(tbOrderInfos.size());
        tbOrderInfos.setSizeFull();

        btnConfFinish.setStyleName("customizationButton text-align-left");
        btnConfFinish.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Finish click!, OrderId: " + order.getId());
                // Set status of order to be UNPAID
                if( Adapter.changeOrderState(order.getId(), Types.State.UNPAID) ){
                    // Set all order details to be COMPLETED
                    for( String orderDetailId : order.getOrderDetailIdList() ){
                        Adapter.changeOrderDetailState(orderDetailId, Types.State.COMPLETED);
                        // @todo: send notification about the finish of making foods
                        OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
                        // notify message
                        Notification notify = new Notification("<b>Alert</b>",
                                "<i>Đồ uống bàn" + orderInfo.getTableName() +" đã xong!</i>",
                                Notification.Type.TRAY_NOTIFICATION  , true);
                        notify.setPosition(Position.BOTTOM_RIGHT);
                        notify.show(Page.getCurrent());
                    }

                    // Set table status to be UNPAID
                    Adapter.changeTableState(order.getTableId(), Types.State.UNPAID);
                }
            }
        });

        this.addComponents(lbTableName, tbOrderInfos, btnConfFinish);
        this.setComponentAlignment(tbOrderInfos, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnConfFinish, Alignment.MIDDLE_CENTER);

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
                    /*orderDetail.getIconFromStatus(orderDetail.getStatus())*/Types.StateToLanguageString(orderDetail.getStatus()),
                    orderDetail.getOrderDetailId()}, itemId);
        }
        tbOrderInfos.setVisibleColumns(Language.SEQUENCE,Language.FOOD_NAME,Language.QUANTITY,Language.STATUS);
        tbOrderInfos.setTableFieldFactory(new DefaultFieldFactory(){
            @Override
            public Field createField(Container container, Object itemId,
                    Object propertyId, Component uiContext) {
                if (Language.STATUS.equals(propertyId)) {
                    ComboBox select = new ComboBox();
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
        // Click listener
        tbOrderInfos.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if( itemClickEvent.getButton().equals(MouseButton.RIGHT) ){
                   if( itemClickEvent.getPropertyId().toString().equals(Language.STATUS) )
                   {
                       OrderDetailRecord orderinfo;
                       orderinfo = orderDetailList.get(Integer.parseInt(itemClickEvent.getItemId().toString()));
                       Image img = orderinfo.getIconFromStatus(orderinfo.getNextState(orderinfo.getStatus()));
                       tbOrderInfos.getItem(itemClickEvent.getItemId()).getItemProperty(Language.STATUS).setValue(img);
                       System.out.println("CHange status! " + orderinfo.getStatus());
                   }
                    //System.out.println("Right Click on\nrow: "+itemClickEvent.getItemId().toString() +
                    //                "\ncolumn: "+itemClickEvent.getPropertyId().toString());
                }
            }
        });
    }
}
