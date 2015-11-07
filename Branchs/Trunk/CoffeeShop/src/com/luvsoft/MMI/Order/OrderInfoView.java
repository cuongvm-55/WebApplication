package com.luvsoft.MMI.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
/*
 *  @author cuongvm-55
 */

@SuppressWarnings("serial")
public class OrderInfoView extends Window{
    final int TABLE_NUMBER_OF_ROWS = 4;
    /*
     * This is a vertical layout contains list of orders 
     */
    private Label lbTableName;
    private Label lbTotalAmount; // label of amount caculate automatically, the amount can not be modified
    private Label lbPaidAmount; // label real amount that customer pay for the bill, the amount can be modified

    //private Panel panelContentContainer;
    private com.vaadin.ui.Table tbOrderDetails;
    private float totalAmount;
    private float paidAmount;
    private VerticalLayout container;
    private TextField txtNote;
    private TextField textFieldpaidAmount;

    // Data
    private Order currentOrder;
    private com.luvsoft.entities.Table table; 
    
    // current orderinfo
    // it will be loaded form db
    private OrderInfo orderInfo;
    
    // order detail list return from AddFood
    private List<OrderDetail> orderDetailList;

    public OrderInfoView(com.luvsoft.entities.Table table){
        super();
        this.table = table;
        totalAmount = 0.00f;
        paidAmount = 0.00f;
        currentOrder = null;
        orderInfo = null;
        orderDetailList = new ArrayList<OrderDetail>();
        init();
        //populate();
    }

    public void init(){
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        container = new VerticalLayout();

        lbTableName = new Label();
        lbTableName.setStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lbTableName.setWidth("100%");
        lbTableName.setValue(Language.TABLE + " " + table.getNumber());
        // Set table properties
        tbOrderDetails = new com.vaadin.ui.Table("");
        tbOrderDetails.addStyleName("bold large FONT_TAHOMA");
        tbOrderDetails.setSizeFull();
        tbOrderDetails.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderDetails.addContainerProperty(Language.STATUS, String.class, null);
        tbOrderDetails.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.PRICE, Float.class, null);
        // tbOrderDetails.addContainerProperty(Language.NOTE, String.class, null);
        //tbOrderDetails.setHeight("100%");
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);
        
        currentOrder = retrieveOrder();
        loadOrderInfo(); // load orderinfo from database
        setupUI();
    }

    public void populate() {
        // Fill data
        if( orderInfo == null ){
            System.out.println("No orderinfo, return!");
            return;
        }
        lbTableName.setValue(orderInfo.getTableName());
        // It's should be the first element
        List<OrderDetailRecord> recordList = orderInfo.getOrderDetailList();
        orderInfo.toString();
        for(int i = 0; i < recordList.size(); i++){
            Integer itemId = new Integer(i);
            OrderDetailRecord record = recordList.get(i);
            System.out.println(record.toString() );
            // Create the table row.
            tbOrderDetails.addItem(new Object[] {new Integer(i), new String(record.getFoodName()), new String(record.getStatus().toString())
                    /*record.getIconFromStatus(record.getStatus())*/, new Integer(record.getQuantity()), new Float(record.getPrice())},
                          itemId);
            // calculate totalAmount
            totalAmount+= record.getPrice();
        }

        paidAmount = totalAmount; // default value of paid amount is equal total amount
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " " + Language.CURRENCY_SYMBOL);
        textFieldpaidAmount.setValue(paidAmount+"");
    }
    private void setupUI(){
     // Note text field
        txtNote = new TextField(Language.NOTE);
        txtNote.addStyleName("bold large FONT_TAHOMA");
        txtNote.setSizeFull();

        // total amount label
        lbTotalAmount = new Label();
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " " + Language.CURRENCY_SYMBOL);
        lbTotalAmount.addStyleName("bold large FONT_TAHOMA");

        // paid amount label
        HorizontalLayout layout = new HorizontalLayout();
        lbPaidAmount = new Label();
        lbPaidAmount.setValue(Language.PAID_AMOUNT);
        lbPaidAmount.addStyleName("bold large FONT_TAHOMA");
        Label lbCurrencySymbol = new Label( " " + Language.CURRENCY_SYMBOL);
        lbCurrencySymbol.addStyleName("bold large FONT_TAHOMA");

        // editable paid amount textfield
        textFieldpaidAmount = new TextField();
        textFieldpaidAmount.setHeight("25");
        textFieldpaidAmount.setWidth("100");
        textFieldpaidAmount.setMaxLength(15);
        textFieldpaidAmount.setValue(paidAmount+"");
        textFieldpaidAmount.addStyleName("v-textfield-dashing bold TEXT_RED FONT_TAHOMA");
        layout.addComponents(lbPaidAmount, textFieldpaidAmount, lbCurrencySymbol);
        layout.setSpacing(true);
        
        container.addComponents(lbTableName, txtNote, tbOrderDetails, lbTotalAmount, layout, buildFooter());
        container.setExpandRatio(lbTableName, 0.7f);
        container.setExpandRatio(txtNote, 1.0f);
        container.setExpandRatio(tbOrderDetails, 7.0f);
        //container.setExpandRatio(layoutOrderHandling, 3.0f);
        container.setResponsive(true);
        container.setSizeFull();
        container.setComponentAlignment(lbTableName, Alignment.TOP_CENTER);
//        // Click listener
//        layout.addLayoutClickListener(new LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutClickEvent event) {
//                // TODO Auto-generated method stub
//                layout.replaceComponent(lbPaidAmount, textFieldpaidAmount);
//            }
//        });
        textFieldpaidAmount.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                // TODO Auto-generated method stub
                //layout.replaceComponent(textFieldpaidAmount, lbPaidAmount);
                paidAmount = Float.parseFloat(event.getText());
            }
        });

        tbOrderDetails.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if( itemClickEvent.getButton().equals(MouseButton.RIGHT) ){
                    System.out.println("Right Click on\nrow: "+itemClickEvent.getItemId().toString() +
                                    "\ncolumn: "+itemClickEvent.getPropertyId().toString());
                }
            }
        });

        this.setContent(container);
    }
    private Component buildFooter() {
        VerticalLayout footer = new VerticalLayout();
        footer.setWidth("100%");;
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        // add food button
        Button btnAddFood = new Button(Language.ADD_FOOD);
        btnAddFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddFood.addStyleName("customizationButton");
        btnAddFood.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().addWindow(new AddFood());
            }
        });

        HorizontalLayout confirmButtonsContainer = new HorizontalLayout();
        // confirm button
        Button btnConfirmPaid = new Button(Language.CONFIRM_PAID);
        btnConfirmPaid.addStyleName("customizationButton");
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_HUGE);

        Button btnConfirmOrder = new Button(Language.CONFIRM_ORDER);
        btnConfirmOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirmOrder.addStyleName("customizationButton");
     // Don't allow some button when no order exist
        if( currentOrder == null )
        {
            btnConfirmOrder.setEnabled(false);
            btnConfirmPaid.setEnabled(false);
        }
        confirmButtonsContainer.addComponents(btnConfirmPaid, btnConfirmOrder);

        footer.addComponents(btnAddFood, confirmButtonsContainer);
        footer.setComponentAlignment(btnAddFood, Alignment.MIDDLE_CENTER);
        footer.setComponentAlignment(confirmButtonsContainer, Alignment.MIDDLE_CENTER);
        
        // Confirm order
        btnConfirmOrder.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // If there's no new food was selected, return
                // close();
                // If there's no order corresponding to current table, create new one
                if( currentOrder == null ){
                    currentOrder = new Order();
                    currentOrder.setStatus(Types.State.WAITING);
                    currentOrder.setNote(txtNote.getValue());
                    //@ todo: Staffname should be editable
                    currentOrder.setStaffName("");
                    currentOrder.setTableId(table.getId());
                    currentOrder.setCreatingTime(LocalDateTime.now());
                }
                
                // save all the data to database
                // firstly, delete all old order details
                List<OrderDetailRecord> oldRecordList = orderInfo.getOrderDetailList();
                for( OrderDetailRecord record : oldRecordList ){
                    if( Adapter.removeOrderDetail(record.getOrderDetailId()) ){
                        System.out.println("removed orderdetailId: " + record.getOrderDetailId());
                    }
                    else{
                        System.out.println("Fail to removed orderdetailId: " + record.getOrderDetailId());
                    }
                }

                // second, add new order details
                List<String> orderDetailIdList = new ArrayList<String>();
                for( OrderDetail orderDetail : orderDetailList ){
                    ObjectId obj = new ObjectId();
                    orderDetail.setId(obj.toString());
                    if( Adapter.addNewOrderDetail(orderDetail) ){
                        orderDetailIdList.add(orderDetail.getId());
                        System.out.println("Added orderdetail");
                    }
                    else{
                        System.out.println("Fail to add orderdetail");
                    }
                }

                // next, update order
                currentOrder.getOrderDetailIdList().clear();
                currentOrder.setOrderDetailIdList(orderDetailIdList);
                if( Adapter.updateOrderDetailList( currentOrder ) ){
                    System.out.println("Updated orderId: " + currentOrder.getId());
                    System.out.println("\tUpdated orderDetailList: " + currentOrder.getOrderDetailIdList().toString());
                }
                else{
                    System.out.println("Fail to update orderId: " + currentOrder.getId());
                }
            }
        });
        
        // Confirm paid
        btnConfirmPaid.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if( currentOrder != null ){
                    // Change order status to PAID
                    Adapter.changeOrderState(currentOrder.getId(), Types.State.PAID);
                    // Change table status to PAID
                    Adapter.changeTableState(currentOrder.getTableId(), Types.State.PAID);
                    close();// close the window
                }
            }
        });

        return footer;
    }

    /*
     * Load orderinfo of currentOrder from db
     */
    public void loadOrderInfo(){
        if( currentOrder == null ){
            System.out.println("There's no order for this table!");
            return;
        }

        List<Order> orderList = new ArrayList<Order >();
        orderList.add(currentOrder);
        List<OrderInfo> orderInfoList = Adapter.retrieveOrderInfoList(orderList);
        if( orderInfoList.isEmpty() ){
            System.out.println("orderId is not exist!");
            return;
        }
        // It's should be the first element
        orderInfo = orderInfoList.get(0);
    }
    
    /*
     * Get order from db
     * return NULL if no order for this table
     */
    private Order retrieveOrder(){
        System.out.println("loadOrder...");
        List<Order> orderList = Adapter.getCurrentOrderList();
        System.out.println(orderList.toString());
        for( Order order : orderList ){
            if( order.getTableId().equals(this.table.getId()) ){
                System.out.println("OrderId: "+ order.getId());
                return order;
            }
        }
        System.out.println("No order for tableId: " + this.table.getId());
        return null;
    }
    
    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}