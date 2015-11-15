package com.luvsoft.MMI.Order;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
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
    
    // order detail record list, which is updated by Addfood...
    private List<OrderDetailRecord> orderDetailRecordList;
    private boolean isOrderDetailListChanged;

    public OrderInfoView(com.luvsoft.entities.Table table){
        super();
        this.table = table;
        totalAmount = 0.00f;
        paidAmount = 0.00f;
        currentOrder = null;
        orderInfo = null;
        orderDetailRecordList = null;
        isOrderDetailListChanged = false;
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
        tbOrderDetails.addContainerProperty("orderdetailId", String.class, null);
        //tbOrderDetails.setHeight("100%");
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);
        currentOrder = retrieveOrder();
        if( currentOrder != null ){
            orderInfo = Adapter.retrieveOrderInfo(currentOrder);
        }
        if( orderInfo != null ){
            orderDetailRecordList = orderInfo.getOrderDetailRecordList();
        }
        setupUI();
    }

    public void populate() {
        // Fill data
        if( orderInfo == null ){
            System.out.println("No orderinfo for this table!");
            return;
        }
        lbTableName.setValue(orderInfo.getTableName());
        txtNote.setValue(orderInfo.getNote());
        if( orderDetailRecordList == null )
        {
            // just return if no record
            return;
        }
        for(int i = 0; i < orderDetailRecordList.size(); i++){
            Integer itemId = new Integer(i);
            OrderDetailRecord record = orderDetailRecordList.get(i);
            System.out.println(record.toString() );
            // Create the table row.
            tbOrderDetails.addItem(new Object[] {new Integer(i),
                    record.getFoodName(),
                    Types.StateToLanguageString(record.getStatus())
                    /*record.getIconFromStatus(record.getStatus())*/,
                    new Integer(record.getQuantity()),
                    new Float(record.getPrice()),
                    record.getOrderDetailId()},
                          itemId);
            // calculate totalAmount
            totalAmount+= record.getPrice();
        }

        tbOrderDetails.setVisibleColumns(Language.SEQUENCE,Language.FOOD_NAME,Language.QUANTITY,Language.STATUS);
        tbOrderDetails.setTableFieldFactory(new DefaultFieldFactory(){
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
                            Item item = tbOrderDetails.getItem(itemId);
                            String orderDetailId = item.getItemProperty("orderdetailId").getValue().toString();
                            String state = event.getProperty().getValue().toString();
                            for( OrderDetailRecord record : orderDetailRecordList ){
                                if( record.getOrderDetailId().equals(orderDetailId) ){
                                    record.setStatus(Types.StringToState(state));
                                    isOrderDetailListChanged = true;
                                    break;
                                }
                            }
                            //System.out.println("Switch orderDetailId: " +orderDetailId+" to state: " + event.getProperty().getValue());
                            //if( !Adapter.changeOrderDetailState(orderDetailId, Types.StringToState(state)) ){
                            //    System.out.println("Cannot change order detail state");
                            //}
                        }
                    });
                    return select;
                }
                else if( propertyId.equals(Language.QUANTITY) ){
                    Item item = tbOrderDetails.getItem(itemId);
                    String orderDetailId = item.getItemProperty("orderdetailId").getValue().toString();
                    String quantity =  item.getItemProperty(Language.QUANTITY).getValue().toString();
                    TextField textField = new TextField();
                    textField.setValue(quantity);
                    textField.addTextChangeListener(new TextChangeListener() {
                        @Override
                        public void textChange(TextChangeEvent event) {
                            for( OrderDetailRecord record : orderDetailRecordList ){
                                if( record.getOrderDetailId().equals(orderDetailId) ){
                                    record.setQuantity(Integer.parseInt(event.getText()));
                                    isOrderDetailListChanged = true;
                                    break;
                                }
                            }
                        }
                    });
                    return textField;
                }
                return null;
            }
        });
        tbOrderDetails.setEditable(true);
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

        textFieldpaidAmount.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                // TODO Auto-generated method stub
                //layout.replaceComponent(textFieldpaidAmount, lbPaidAmount);
                //paidAmount = Float.parseFloat(event.getText());
                
                // save the amount to db
                //Adapter.updateFieldValueOfOrder(currentOrder.getId(), Order.DB_FIELD_NAME_PAID_MONEY, ""+paidAmount);
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
                // Save the note
                if( currentOrder != null && !currentOrder.getNote().equals(txtNote.getValue()) ){
                    currentOrder.setNote(txtNote.getValue());
                    Adapter.updateFieldValueOfOrder(currentOrder.getId(),
                                                Order.DB_FIELD_NAME_NOTE, txtNote.getValue());
                }
                // If there's no new food was selected, return
                if( !isOrderDetailListChanged )
                {
                    System.out.println("No changes, return");
                    close();
                    return;
                }

                // If there's no order corresponding to current table, create new one
                if( currentOrder == null ){
                    currentOrder = new Order();
                    currentOrder.setStatus(Types.State.WAITING);
                    currentOrder.setNote(txtNote.getValue());
                    //@ todo: Staffname should be editable
                    currentOrder.setStaffName("");
                    currentOrder.setTableId(table.getId());
                    currentOrder.setCreatingTime(new Date());
                }
                List<String> orderDetailIdList = currentOrder.getOrderDetailIdList();
                for( OrderDetailRecord record : orderDetailRecordList ){
                    OrderDetail odDetail = new OrderDetail();
                    String id = record.getOrderDetailId();
                    if( id.equals("") ){
                        id = new ObjectId().toString();
                    }
                    odDetail.setId(id);
                    odDetail.setFoodId(record.getFoodId());
                    odDetail.setQuantity(record.getQuantity());
                    odDetail.setState(record.getStatus());

                    switch( record.getChangeFlag() ){
                    case MODIFIED:
                        // update MODIFIED records
                        if( !Adapter.updateOrderDetail(odDetail.getId(), odDetail) ){
                            System.out.println("Fail to update order detail, Id: " + odDetail.getId());
                        }
                        else{
                            // set table state to WAITING
                            Adapter.changeTableState(currentOrder.getTableId(), Types.State.WAITING);
                        }
                        break;
                    case ADDNEW:
                        // create ADDNEW records
                        if( !Adapter.addNewOrderDetail(odDetail) ){
                            System.out.println("Fail to add new order detail, Id: " + odDetail.getId());
                        }
                        else{
                            orderDetailIdList.add(odDetail.getId());
                            // set table state to WAITING
                            Adapter.changeTableState(currentOrder.getTableId(), Types.State.WAITING);
                        }
                        break;
                    case DELETED:
                        // delete DELETED records
                        if( !Adapter.removeOrderDetail(odDetail.getId()) ){
                            System.out.println("Fail to delete order detail, Id: " + odDetail.getId());
                        }
                        else{
                            orderDetailIdList.remove(odDetail.getId());
                        }
                        break;
                    default:
                        break;
                    }
                }

                // next, update order
                //currentOrder.getOrderDetailIdList().clear();
                currentOrder.setOrderDetailIdList(orderDetailIdList);
                if( Adapter.updateOrderDetailList( currentOrder ) ){
                    System.out.println("Updated orderId: " + currentOrder.getId());
                    System.out.println("\tUpdated orderDetailList: " + currentOrder.getOrderDetailIdList().toString());
                }
                else{
                    System.out.println("Fail to update orderId: " + currentOrder.getId());
                }

                close();
            }
        });
        
        // Confirm paid
        btnConfirmPaid.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if( currentOrder != null ){
                    currentOrder.setPaidMoney(Float.parseFloat(textFieldpaidAmount.getValue()));
                    currentOrder.setPaidTime(new Date());
                    currentOrder.setStatus(Types.State.PAID);
                    if( Adapter.updateOrder(currentOrder.getId(), currentOrder) ){
                        // Change table status to PAID
                        Adapter.changeTableState(currentOrder.getTableId(), Types.State.PAID);
                    }
                    close();// close the window
                }
            }
        });

        return footer;
    }

    /*
     * Rewind the deleted order details
     */
    public void rewindOrderDetailList(List<OrderDetailRecord> oldRecordList){
        for( OrderDetailRecord record : oldRecordList ){
            OrderDetail odDetail = new OrderDetail();
            odDetail.setId(record.getOrderDetailId());
            odDetail.setFoodId(record.getFoodId());
            odDetail.setQuantity(record.getQuantity());
            odDetail.setState(record.getStatus());
            Adapter.addNewOrderDetail(odDetail);
        }
    }

    /*
     * Get order from db
     * return NULL if no order for this table
     */
    private Order retrieveOrder(){
        System.out.println("loadOrder...");
        List<Order> orderList = Adapter.getOrderListIgnoreState(Types.State.COMPLETED);
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