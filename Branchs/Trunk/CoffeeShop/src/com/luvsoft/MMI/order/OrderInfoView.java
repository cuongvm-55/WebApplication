package com.luvsoft.MMI.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.order.OrderDetailRecord.ChangedFlag;
import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.threads.NewOrderManager;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/*
 *  @author cuongvm-55
 */

@SuppressWarnings("serial")
public class OrderInfoView extends AbstractOrderView {
    final int TABLE_NUMBER_OF_ROWS = 6;
    /*
     * This is a vertical layout contains list of orders
     */
    private Label lbTableName;
    private Label lbTotalAmount; // label of amount caculate automatically, the
                                 // amount can not be modified
    private Label lbPaidAmount; // label real amount that customer pay for the
                                // bill, the amount can be modified
                                // we consider that currency format in VN is almost the same as ITALY
    private Button btnAddFood;
    private Button btnConfirmOrder;
    private Button btnConfirmPaid;
    // private Panel panelContentContainer;
    private com.vaadin.ui.Table tbOrderDetails;
    private float totalAmount;
    private float paidAmount;
    private VerticalLayout container;
    private TextArea txtNote;
    private TextField textFieldpaidAmount;

    // Data
    private Order currentOrder;

    // current orderinfo
    // it will be loaded form db
    private OrderInfo orderInfo;

    // order detail record list, which is updated by Addfood...
    private List<OrderDetailRecord> orderDetailRecordList;
    private boolean isOrderDetailListChanged;

    // Flag: is new order?
    private boolean isNewOrder;
    private boolean isNewFoodAdded;
    private boolean allOrderDetailCanceled;
    private String previousTextValue = "";

    public static enum ViewMode{ORDER_SUMMRY, ORDER_DETAIL_VIEW};
    private ViewMode currentMode;
    public OrderInfoView(ViewMode mode) {
        super();
        totalAmount = 0.00f;
        paidAmount = 0.00f;
        currentOrder = null;
        orderInfo = null;
        orderDetailRecordList = null;
        isOrderDetailListChanged = false;
        isNewOrder = false;

        currentMode = mode;
    }

    @Override
    public void createView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        container = new VerticalLayout();

        lbTableName = new Label();
        lbTableName
                .setStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lbTableName.setWidth("100%");
        lbTableName.setValue(Language.TABLE + " "
                + getCurrentTable().getNumber());

        buildTable();

        currentOrder = retrieveOrder();
        if( currentOrder != null ) {
            orderInfo = Adapter.retrieveOrderInfo(currentOrder);
            if( orderInfo != null ) {
                orderDetailRecordList = orderInfo.getOrderDetailRecordList();
            }
            previousTextValue = currentOrder.getNote();
            isNewOrder = false;
        }
        else {
            // If there's no order corresponding to current table, create new
            // one
            currentOrder = new Order();
            currentOrder.setId(new ObjectId().toString());
            currentOrder.setStatus(Types.State.WAITING);
            currentOrder.setNote("");
            //currentOrder.setStaffName(staffName);
            currentOrder.setTableId(getCurrentTable().getId());
            currentOrder.setCreatingTime(new Date());

            // Create new order detail record list
            orderDetailRecordList = new ArrayList<OrderDetailRecord>();

            // Create new order info
            orderInfo = new OrderInfo();
            orderInfo.setTableName(Language.TABLE + " "
                    + getCurrentTable().getNumber());
            orderInfo.setOrderDetailRecordList(orderDetailRecordList);

            // Change flag
            isNewOrder = true;
        }

        updateTable();

        setupUI();
    }

    @Override
    public void reloadView() {
        // Fill data
        if( orderInfo == null ) {
            System.out.println("No orderinfo for this table!");
            return;
        }
        if( orderDetailRecordList == null ) {
            // just return if no record
            return;
        }

        updateTable();

        paidAmount = totalAmount; // default value of paid amount is equal total
                                  // amount
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);
        textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));

        // Set state of buttons depend on current order detail list
        setControlButtonsState();
    }

    private void buildTable() {
        // Set table properties
        tbOrderDetails = new com.vaadin.ui.Table("");
        tbOrderDetails.addStyleName("bold large FONT_TAHOMA");
        tbOrderDetails.setSizeFull();
        tbOrderDetails.setResponsive(true);
        tbOrderDetails.addContainerProperty(Language.SEQUENCE, Integer.class,
                null);
        tbOrderDetails.addContainerProperty(Language.FOOD_NAME, String.class,
                null);
        tbOrderDetails.addContainerProperty(Language.STATUS, ComboBox.class,
                null);
        tbOrderDetails.addContainerProperty(Language.QUANTITY, TextField.class,
                null);
        tbOrderDetails.addContainerProperty(Language.PRICE, Double.class, null);
        tbOrderDetails
                .addContainerProperty(Language.DELETE, Button.class, null);
        tbOrderDetails
                .addContainerProperty("orderdetailId", String.class, null);

        tbOrderDetails.setColumnCollapsingAllowed(true);
        tbOrderDetails.setColumnCollapsed("orderdetailId", true);
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);

        tbOrderDetails.setColumnExpandRatio(Language.SEQUENCE, 0.5f);
        tbOrderDetails.setColumnExpandRatio(Language.FOOD_NAME, 2.0f);
        tbOrderDetails.setColumnExpandRatio(Language.STATUS, 1.4f);
        tbOrderDetails.setColumnExpandRatio(Language.QUANTITY, 0.9f);
        tbOrderDetails.setColumnExpandRatio(Language.PRICE, 1.0f);
        tbOrderDetails.setColumnExpandRatio(Language.DELETE, 0.7f);
    }

    private void updateTable() {
        totalAmount = 0;
        tbOrderDetails.removeAllItems();

        for (int i = 0; i < orderDetailRecordList.size(); i++) {
            OrderDetailRecord record = orderDetailRecordList.get(i);

            // If status of record is COMPLETED or CANCELED, mark as READONLY
            if(record.getStatus() == Types.State.COMPLETED || record.getStatus() == Types.State.CANCELED){
                record.setChangeFlag(ChangedFlag.READONLY);
            }

            // If item already deleted, nothing to do
            if( record.getChangeFlag() == ChangedFlag.DELETED ) {
                continue;
            }

            Button btnRemove = new Button(Language.DELETE);
            btnRemove.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            btnRemove.setIcon(FontAwesome.BAN);
            btnRemove.setData(i);

            ComboBox cbStatus = new ComboBox();
            if(record.getStatus() == Types.State.COMPLETED){
                cbStatus.addItem(Language.COMPLETED);
            }
            else if(record.getStatus() == Types.State.CANCELED){
                cbStatus.addItem(Language.CANCELED);
            }
            else{
                cbStatus.addItems(Language.CANCELED, Language.WAITING);
            }
            
            cbStatus.setData(i);
            cbStatus.setValue(Types.StateToLanguageString(record.getStatus()));
            cbStatus.setScrollToSelectedItem(true);
            cbStatus.setNullSelectionAllowed(false);
            cbStatus.setTextInputAllowed(false);
            cbStatus.setRequired(true);
            cbStatus.setResponsive(true);

            TextField txtQuantity = new TextField();
            txtQuantity.setValue(record.getQuantity() + "");
            txtQuantity.setWidth("100%");
            txtQuantity.setResponsive(true);

            // Mark red color for canceled order details
            if( record.getStatus() == Types.State.CANCELED ) {
                btnRemove.addStyleName("TEXT_RED");
                txtQuantity.addStyleName("TEXT_RED");
            }

            // If item is not available, we will add new one
            if( tbOrderDetails.addItem(new Object[] { i, record.getFoodName(),
                    // Types.StateToLanguageString(record.getStatus()),
                    cbStatus, txtQuantity, new Double(record.getPrice()),
                    btnRemove, record.getOrderDetailId() }, i) == null ) {
                System.out.println("Fail to add new item to tbOrderDetails.");
            }
            btnRemove.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    record.setChangeFlag(ChangedFlag.DELETED);
                    if( record.getStatus() != State.CANCELED ) {
                        totalAmount -= record.getPrice() * record.getQuantity();
                        paidAmount -= record.getPrice() * record.getQuantity();
                        textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);
                    }
                    tbOrderDetails.removeItem(event.getButton().getData());
                    tbOrderDetails.setImmediate(true);
                }
            });

            cbStatus.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    // Set order detail status
                    String state = event.getProperty().getValue().toString();
                    record.setStatus(Types.StringToState(state));
                    // Mark red color for canceled order details
                    if( record.getStatus() == Types.State.CANCELED ) {
                        btnRemove.addStyleName("TEXT_RED");
                        txtQuantity.addStyleName("TEXT_RED");

                        totalAmount -= record.getPrice() * record.getQuantity();
                        paidAmount -= record.getPrice() * record.getQuantity();

                        textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);
                        record.setPreviousStatus(Types.State.CANCELED);

                    }
                    else if( record.getStatus() != Types.State.CANCELED
                            && record.getPreviousStatus() == Types.State.CANCELED ) {
                        totalAmount += record.getPrice() * record.getQuantity();
                        paidAmount += record.getPrice() * record.getQuantity();

                        textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);

                        btnRemove.removeStyleName("TEXT_RED");
                        txtQuantity.removeStyleName("TEXT_RED");
                        record.setPreviousStatus(record.getStatus());
                    }

                    // We do not change flag to Modified if this record is a new
                    // one
                    if( record.getChangeFlag() != ChangedFlag.ADDNEW ) {
                        if( record.getOriginalStatus() != record.getStatus() ) {
                            record.setTempStatusChangeFlag(ChangedFlag.MODIFIED);
                        } else {
                            record.setTempStatusChangeFlag(ChangedFlag.UNMODIFIED);
                        }
                    }
                }
            });

            tbOrderDetails.setCellStyleGenerator(new Table.CellStyleGenerator() {
                @Override
                public String getStyle(Table source, Object itemId,
                        Object propertyId) {
                    Item item = tbOrderDetails.getItem(itemId);
                    ComboBox states = (ComboBox) item.getItemProperty(Language.STATUS).getValue();
                    if( states.getValue() != null &&
                            states.getValue().equals(Types.StateToLanguageString(Types.State.CANCELED))){
                        return "highlight-red";
                    }
                    return null;
                }
              });
            
            txtQuantity.addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(TextChangeEvent event) {
                    Integer quantity;
                    try {
                        quantity = Integer.parseInt(event.getText());
                    }
                    catch ( NumberFormatException e ) {
                        System.out
                                .println("OrderInfoView::UpdateTable::Cannot parse from String to Integer");
                        return;
                    }
                    // update total amount
                    int offset = quantity - record.getQuantity();
                    totalAmount += record.getPrice() * offset;
                    paidAmount += record.getPrice() * offset;
                    textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));
                    lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);

                    record.setQuantity(quantity);
                    // We do not change flag to Modified if this record is a new
                    // one
                    if( record.getChangeFlag() != ChangedFlag.ADDNEW ) {
                        if( record.getOriginalQuantity() != record.getQuantity() ) {
                            record.setTempQuantityChangeFlag(ChangedFlag.MODIFIED);
                        } else {
                            record.setTempQuantityChangeFlag(ChangedFlag.UNMODIFIED);
                        }
                    }
                }
            });
            if( record.getChangeFlag() == ChangedFlag.READONLY){
                btnRemove.setEnabled(false);
                cbStatus.setReadOnly(true);
                txtQuantity.setReadOnly(true);
            }

            if( record.getStatus() != Types.State.CANCELED ) {
                totalAmount += record.getPrice() * record.getQuantity();
                paidAmount += record.getPrice() * record.getQuantity();
            }
        }
        tbOrderDetails.setImmediate(true);
    }

    private void setupUI() {
        // table name
        lbTableName.setValue(orderInfo.getTableName());

        // Note text field
        txtNote = new TextArea(Language.NOTE);
        txtNote.addStyleName("bold large FONT_TAHOMA");
        txtNote.setSizeFull();
        txtNote.setValue(currentOrder.getNote());

        // total amount label
        lbTotalAmount = new Label();
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + Types.getNumberFormat().format(totalAmount) + " "+ Language.CURRENCY_SYMBOL);
        lbTotalAmount.addStyleName("bold large FONT_TAHOMA");

        // paid amount label
        HorizontalLayout layout = new HorizontalLayout();
        lbPaidAmount = new Label();
        lbPaidAmount.setValue(Language.PAID_AMOUNT);
        lbPaidAmount.addStyleName("bold large FONT_TAHOMA");
        Label lbCurrencySymbol = new Label(" " + Language.CURRENCY_SYMBOL);
        lbCurrencySymbol.addStyleName("bold large FONT_TAHOMA");

        // editable paid amount textfield
        textFieldpaidAmount = new TextField();
        textFieldpaidAmount.setHeight("25");
        textFieldpaidAmount.setWidth("100");
        textFieldpaidAmount.setMaxLength(15);
        textFieldpaidAmount.setValue(Types.getNumberFormat().format(paidAmount));
        textFieldpaidAmount.addStyleName("v-textfield-dashing bold TEXT_RED FONT_TAHOMA");
        layout.addComponents(lbTotalAmount, lbPaidAmount, textFieldpaidAmount,lbCurrencySymbol);
        layout.setSpacing(true);

        container.addComponents(lbTableName, txtNote, tbOrderDetails, layout, buildFooter());
        container.setExpandRatio(txtNote, 2.0f);
        container.setExpandRatio(tbOrderDetails, 7.0f);
        // container.setExpandRatio(layoutOrderHandling, 3.0f);
        container.setResponsive(true);
        container.setSizeFull();
        container.setComponentAlignment(lbTableName, Alignment.TOP_CENTER);

        tbOrderDetails.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if( itemClickEvent.getButton().equals(MouseButton.RIGHT) ) {
                    System.out.println("Right Click on\nrow: "
                            + itemClickEvent.getItemId().toString()
                            + "\ncolumn: "
                            + itemClickEvent.getPropertyId().toString());
                }
            }
        });

        this.setContent(container);
    }

    private Component buildFooter() {
        OrderInfoView orderInforView = this;

        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");

        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        // add food button
        btnAddFood = new Button(Language.ADD_FOOD);
        btnAddFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddFood.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnAddFood.addStyleName("margin-bottom1");
        btnAddFood.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                AddFoodView addFoodView = new AddFoodView();
                addFoodView.setParentView(orderInforView);
                addFoodView.setCurrentTable(getCurrentTable());
                addFoodView.createView();
                getUI().addWindow(addFoodView);
            }
        });

        // confirm button
        btnConfirmPaid = new Button(Language.CONFIRM_PAID);
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_HUGE);

        btnConfirmOrder = new Button(Language.CONFIRM_ORDER);
        btnConfirmOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirmOrder.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfirmOrder.addStyleName("margin-left1");

        // Set state of buttons depend on current order detail list
        setControlButtonsState();

        footer.addComponents(btnAddFood, btnConfirmOrder, btnConfirmPaid);
        footer.setComponentAlignment(btnAddFood, Alignment.MIDDLE_RIGHT);
        footer.setComponentAlignment(btnConfirmOrder, Alignment.MIDDLE_LEFT);
        footer.setComponentAlignment(btnConfirmPaid, Alignment.MIDDLE_CENTER);
        footer.setSpacing(true);

        // Confirm order
        btnConfirmOrder.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // update order details infomation
                if( !saveOrderDetailsData() ){
                    // set table state to map to current order
                    setTableState();
                    close();
                    return;
                }

                ////////////////////////////////////////////////////////////////////////
                // Update order
                ////////////////////////////////////////////////////////////////////////
                if (isNewOrder) {
                    if (Adapter.addNewOrder(currentOrder)) {
                        System.out.println("Add new Order: " + currentOrder.toString());
                        // Broadcast order change event
                        Broadcaster.broadcast(CoffeeshopUI.NEW_ORDER_MESSAGE + "::"
                                + getCurrentTable().getNumber());

                        // Waiting time thread
                        NewOrderManager waitingTimeThread = new NewOrderManager(
                                currentOrder);
                        waitingTimeThread.start();

                        // set table state to map to current order
                        setTableState();
                    } else {
                        System.out.println("Fail to add orderId: "
                                + currentOrder.getId());
                    }
                } else {
                    if (Adapter.updateOrder(currentOrder.getId(), currentOrder)) {
                        System.out.println("Update Order: " + currentOrder.toString());
                        if( isOrderDetailListChanged || !previousTextValue.equals(txtNote.getValue()) ){
                            // Broadcast order change event
                            Broadcaster.broadcast(CoffeeshopUI.ORDER_UPDATED_MESSAGE + "::"
                                    + getCurrentTable().getNumber());
                        }

                        // In case add new food, add new waiting time thread if there's no thread exist for this order
                        if( isNewFoodAdded ){
                            boolean isThreadTimeExist = false;
                            for(NewOrderManager orderMgr : NewOrderManager.listWaitingOrderThreads){
                                if( orderMgr.getCurrentOrder().getId().equals(currentOrder.getId()) ){
                                    isThreadTimeExist = true;
                                    break;
                                }
                            }
                            if( !isThreadTimeExist ){
                                NewOrderManager waitingTimeThread = new NewOrderManager(
                                        currentOrder);
                                waitingTimeThread.start();
                            }
                        }

                        // set table state to map to current order
                        setTableState();
                    } else {
                        System.out.println("Fail to update orderId: ");
                    }
                }

                // Clear data
                currentOrder = null;
                orderDetailRecordList = null;
                close();
            }
        });

        // Confirm paid
        btnConfirmPaid.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if( currentOrder != null ) {
                    // Save note
                    if( !currentOrder.getNote().equals(txtNote.getValue()) ) {
                    currentOrder.setNote(txtNote.getValue());
                    }

                    // Update order if staffName is different
                    if( getSession() != null &&
                            getSession().getAttribute("user") != null ){
                        String staffName = getSession().getAttribute("user").toString();
                        if (!staffName.equals(currentOrder.getStaffNameConfirmPaid())) {
                            currentOrder.setStaffNameConfirmPaid(staffName);
                        }
                    }

                    currentOrder.setPaidMoney(Types.getDoubleValueFromFormattedStr(textFieldpaidAmount.getValue()));
                    currentOrder.setPaidTime(new Date());
                    currentOrder.setStatus(Types.State.PAID);

                    boolean ret = false;
                    if( isNewOrder ) {
                        if( Adapter.addNewOrder(currentOrder) ) {
                            ret = true;
                        }
                    }
                    else {
                        if( Adapter.updateOrder(currentOrder.getId(),
                                currentOrder) ) {
                            ret = true;
                        }
                    }

                    if( ret == true ) {
                        // Change table status to PAID
                        Adapter.changeTableState(currentOrder.getTableId(),
                                Types.State.PAID);
                    }
                    close();// close the window
                    Broadcaster.broadcast(CoffeeshopUI.ORDER_WAS_PAID+"::"+getCurrentTable().getNumber());
                }
            }
        });

        return footer;
    }
    
    /**
     * Set table state depend on state of current order
     */
    private void setTableState()
    {
        Types.State tblState = State.UNDEFINED;
        switch(currentOrder.getStatus()){
        case CANCELED:
            tblState = State.EMPTY;
            break;
        default:
            tblState = currentOrder.getStatus();
            break;
        }
        Adapter.changeTableState(currentOrder.getTableId(), tblState);
    }

    /**
     * Set control button state depend on current mode view
     */
    private void setControlButtonsState(){
        btnAddFood.setVisible(false);
        btnConfirmOrder.setVisible(false);
        btnConfirmPaid.setVisible(false);
        switch(currentMode){
        case ORDER_SUMMRY:
            btnConfirmPaid.setVisible(true);
            break;
        case ORDER_DETAIL_VIEW:
            btnAddFood.setVisible(true);
            btnConfirmOrder.setVisible(true);
            break;
         default:
             break;
        }

        boolean hasWaitingOrderDetail = false;
        boolean hasCompleltedOrderDetail = false;
        for( OrderDetailRecord record : orderDetailRecordList ){
            if( record.getStatus() == Types.State.WAITING ){
                hasWaitingOrderDetail = true;
                break;
            }
            
            if( record.getStatus() == Types.State.COMPLETED){
                hasCompleltedOrderDetail = true;
            }
        }

        if( !orderDetailRecordList.isEmpty() ) {
            btnConfirmOrder.setEnabled(true);
            if( hasWaitingOrderDetail ){
                // there's an waiting order detail
                btnConfirmPaid.setEnabled(false);
                textFieldpaidAmount.setEnabled(false);
            }
            else if( hasCompleltedOrderDetail ){
                // No waiting one, all are canceled or completed
                btnConfirmPaid.setEnabled(true);
                textFieldpaidAmount.setEnabled(true);
            }
            else{
                // all are canceled
                btnConfirmPaid.setEnabled(false);
                textFieldpaidAmount.setEnabled(false);
            }
        }
        else{
            btnConfirmOrder.setEnabled(false);
            btnConfirmPaid.setEnabled(false);
            textFieldpaidAmount.setEnabled(false);
        }
    }
    /**
     * This method save/update order details information
     * Call when confirm order or confirm paid
     * @return
     */
    private boolean saveOrderDetailsData(){
        ////////////////////////////////////////////////////////////////////////
        // Save note
        ///////////////////////////////////////////////////////////////////////
        if( !currentOrder.getNote().equals(txtNote.getValue()) ) {
            currentOrder.setNote(txtNote.getValue());
        }

        // Update order if staffName is different
        if( getSession() != null &&
                getSession().getAttribute("user") != null ){
            String staffName = getSession().getAttribute("user").toString();
            if (!staffName.equals(currentOrder.getStaffNameConfirmOrderFinish())) {
                currentOrder.setStaffNameConfirmOrderFinish(staffName);
            }
        }

        ////////////////////////////////////////////////////////////////////////
        // Update order details
        ///////////////////////////////////////////////////////////////////////
        isOrderDetailListChanged = false;
        List<String> orderDetailIdList = new ArrayList<String>();
        orderDetailIdList.clear();
        allOrderDetailCanceled = true;
        isNewFoodAdded = false;
        boolean hasWaitingOrderDetail = false;
        for (OrderDetailRecord record : orderDetailRecordList) {
            OrderDetail odDetail = new OrderDetail();
            String id = record.getOrderDetailId();
            if (id.equals("")) {
                id = new ObjectId().toString();
            }
            orderDetailIdList.add(id);
            odDetail.setId(id);
            odDetail.setFoodId(record.getFoodId());
            odDetail.setQuantity(record.getQuantity());
            odDetail.setState(record.getStatus());

            if(record.getTempStatusChangeFlag() == ChangedFlag.MODIFIED || record.getTempQuantityChangeFlag() == ChangedFlag.MODIFIED) {
                if(record.getChangeFlag().equals(ChangedFlag.UNMODIFIED)) {
                    record.setChangeFlag(ChangedFlag.MODIFIED);
                }
            }
            // check if all record is CANCELED
            if (record.getStatus() != Types.State.CANCELED) {
                allOrderDetailCanceled = false;
            }

            // check if there's a waiting order detail
            if (record.getStatus() == Types.State.WAITING) {
                hasWaitingOrderDetail = true;
            }

            System.out.println("record.getChangeFlag() "
                    + record.getChangeFlag());

            switch (record.getChangeFlag()) {
            case MODIFIED: {
                // update MODIFIED records
                System.out.println("Modified");
                if (Adapter.updateOrderDetail(odDetail.getId(), odDetail)) {
                    isOrderDetailListChanged = true;
                }
                else{
                    System.out.println("Fail to update order detail, Id: "
                            + odDetail.getId());
                }
                break;
            }
            case ADDNEW: {
                System.out.println("Add New");
                // create ADDNEW records
                if (Adapter.addNewOrderDetail(odDetail)) {
                    isOrderDetailListChanged = true;

                    // Consider only if the new order detail has state waiting
                    if( odDetail.getState() == Types.State.WAITING ){
                        isNewFoodAdded = true;
                    }
                }
                else{
                    System.out.println("Fail to add new order detail, Id: "
                        + odDetail.getId());
                }

                break;
            }
            case DELETED: {
                System.out.println("Delete");
                // delete DELETED records
                if (Adapter.removeOrderDetail(odDetail.getId())) {
                    orderDetailIdList.remove(orderDetailIdList.size() - 1);
                    isOrderDetailListChanged = true;
                }
                else{
                    System.out.println("Fail to delete order detail, Id: "
                            + odDetail.getId());
                }

                break;
            }
            default:
                break;
            }
        }

        if (isOrderDetailListChanged) {
            currentOrder.setOrderDetailIdList(orderDetailIdList);
            // If all order details are canceled or deleted, mark order as CANCELED
            if (allOrderDetailCanceled || currentOrder.getOrderDetailIdList().isEmpty()) {
                if( isNewOrder ){
                    // Ignore this order
                    currentOrder.setStatus(Types.State.CANCELED);
                    return false;
                }
                currentOrder.setStatus(Types.State.CANCELED);
            }

            if( isNewFoodAdded ){
                // reset creating time when user add new food to exist order
                if( currentOrder.getStatus() != State.WAITING ){
                    currentOrder.setCreatingTime(new Date());
                }
                currentOrder.setStatus(Types.State.WAITING);
                currentOrder.setWaitingTime(0); // reset waiting time
            }

            if( !hasWaitingOrderDetail ){
                currentOrder.setStatus(Types.State.UNPAID);
            }
        }

        return true;
    }

    /*
     * Get order from db return NULL if no order for this table
     */
    private Order retrieveOrder() {
        System.out.println("loadOrder...");
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.PAID);
        states.add(Types.State.CANCELED);
        List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
        System.out.println(orderList.toString());
        for (Order order : orderList) {
            if( order.getTableId().equals(this.getCurrentTable().getId()) ) {
                System.out.println("OrderId: " + order.getId());
                return order;
            }
        }
        System.out.println("No order for tableId: "
                + this.getCurrentTable().getId());
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

    public List<OrderDetailRecord> getOrderDetailRecordList() {
        return orderDetailRecordList;
    }

    public void setOrderDetailRecordList(
            List<OrderDetailRecord> orderDetailRecordList) {
        this.orderDetailRecordList = orderDetailRecordList;
    }

    public boolean isOrderDetailListChanged() {
        return isOrderDetailListChanged;
    }

    public void setOrderDetailListChanged(boolean isOrderDetailListChanged) {
        this.isOrderDetailListChanged = isOrderDetailListChanged;
    }
}