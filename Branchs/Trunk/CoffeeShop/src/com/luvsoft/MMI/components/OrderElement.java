package com.luvsoft.MMI.components;

import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.Order.OrderDetailRecord;
import com.luvsoft.MMI.Order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
        tbOrderInfos.setPageLength(5);
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
        orderDetailList = orderInfo.getOrderDetailList();
        lbTableName.setValue(orderInfo.getTableName());
        for( int i = 0; i < orderDetailList.size(); i++ ){
            Integer itemId = new Integer(i);
            OrderDetailRecord orderDetail = orderDetailList.get(i);
            tbOrderInfos.addItem(new Object[]{new Integer(i), orderDetail.getFoodName(), orderDetail.getQuantity(), /*orderDetail.getIconFromStatus(orderDetail.getStatus())*/orderDetail.getStatus().toString()}, itemId);
        }
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
