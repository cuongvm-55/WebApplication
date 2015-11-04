package com.luvsoft.MMI;

import java.util.List;

import com.luvsoft.MMI.Order.OrderInfo;
import com.luvsoft.MMI.components.OrderElement;
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
        List<OrderInfo> orderInfoList = Adapter.retrieveOrderInfoList(MainView.getInstance().getOrderList());
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
}
