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

    private Button btnConfirmOrder;
    private Button btnConfirmPaid;
    // private Panel panelContentContainer;
    private com.vaadin.ui.Table tbOrderDetails;
    private float totalAmount;
    private float paidAmount;
    private VerticalLayout container;
    private TextField txtNote;
    private TextField textFieldpaidAmount;
    private String staffName;

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

    public OrderInfoView() {
        super();
        totalAmount = 0.00f;
        paidAmount = 0.00f;
        currentOrder = null;
        orderInfo = null;
        orderDetailRecordList = null;
        isOrderDetailListChanged = false;
        isNewOrder = false;
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

        staffName = getCurrentTable().getStaffName();

        currentOrder = retrieveOrder();
        if( currentOrder != null ) {
            orderInfo = Adapter.retrieveOrderInfo(currentOrder);
            if( orderInfo != null ) {
                orderDetailRecordList = orderInfo.getOrderDetailRecordList();
            }
            isNewOrder = false;
        }
        else {
            // If there's no order corresponding to current table, create new
            // one
            currentOrder = new Order();
            currentOrder.setId(new ObjectId().toString());
            currentOrder.setStatus(Types.State.WAITING);
            currentOrder.setNote("");
            currentOrder.setStaffName(staffName);
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
        lbTableName.setValue(orderInfo.getTableName());
        txtNote.setValue(orderInfo.getNote());
        if( orderDetailRecordList == null ) {
            // just return if no record
            return;
        }

        updateTable();

        paidAmount = totalAmount; // default value of paid amount is equal total
                                  // amount
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " "
                + Language.CURRENCY_SYMBOL);
        textFieldpaidAmount.setValue(paidAmount + "");

        if( !orderDetailRecordList.isEmpty() ) {
            btnConfirmOrder.setEnabled(true);
            btnConfirmPaid.setEnabled(true);
        }
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
        tbOrderDetails.addContainerProperty(Language.PRICE, Float.class, null);
        tbOrderDetails
                .addContainerProperty(Language.DELETE, Button.class, null);
        tbOrderDetails
                .addContainerProperty("orderdetailId", String.class, null);

        tbOrderDetails.setColumnCollapsingAllowed(true);
        tbOrderDetails.setColumnCollapsed("orderdetailId", true);
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);

        tbOrderDetails.setColumnExpandRatio(Language.SEQUENCE, 0.5f);
        tbOrderDetails.setColumnExpandRatio(Language.FOOD_NAME, 3.0f);
        tbOrderDetails.setColumnExpandRatio(Language.STATUS, 1.5f);
        tbOrderDetails.setColumnExpandRatio(Language.QUANTITY, 0.5f);
        tbOrderDetails.setColumnExpandRatio(Language.PRICE, 0.5f);
        tbOrderDetails.setColumnExpandRatio(Language.DELETE, 0.5f);
    }

    private void updateTable() {
        totalAmount = 0;
        tbOrderDetails.removeAllItems();

        for (int i = 0; i < orderDetailRecordList.size(); i++) {
            OrderDetailRecord record = orderDetailRecordList.get(i);

            // If item already deleted, nothing to do
            if( record.getChangeFlag() == ChangedFlag.DELETED ) {
                continue;
            }

            Button btnRemove = new Button(Language.DELETE);
            btnRemove.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            btnRemove.setIcon(FontAwesome.BAN);
            btnRemove.setData(i);

            ComboBox cbStatus = new ComboBox();
            cbStatus.addItems(Language.CANCELED, Language.WAITING,
                    Language.COMPLETED);
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
                    cbStatus, txtQuantity, new Float(record.getPrice()),
                    btnRemove, record.getOrderDetailId() }, i) == null ) {
                System.out.println("NULL roai");
            }

            btnRemove.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    record.setChangeFlag(ChangedFlag.DELETED);
                    if( record.getStatus() != State.CANCELED ) {
                        totalAmount -= record.getPrice() * record.getQuantity();
                        paidAmount -= record.getPrice() * record.getQuantity();
                        textFieldpaidAmount.setValue(paidAmount + "");
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT
                                + totalAmount + " " + Language.CURRENCY_SYMBOL);
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

                        textFieldpaidAmount.setValue(paidAmount + "");
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT
                                + totalAmount + " " + Language.CURRENCY_SYMBOL);
                        record.setPreviousStatus(Types.State.CANCELED);

                    }
                    else if( record.getStatus() != Types.State.CANCELED
                            && record.getPreviousStatus() == Types.State.CANCELED ) {
                        totalAmount += record.getPrice() * record.getQuantity();
                        paidAmount += record.getPrice() * record.getQuantity();

                        textFieldpaidAmount.setValue(paidAmount + "");
                        lbTotalAmount.setValue(Language.TOTAL_AMOUNT
                                + totalAmount + " " + Language.CURRENCY_SYMBOL);

                        btnRemove.removeStyleName("TEXT_RED");
                        txtQuantity.removeStyleName("TEXT_RED");
                        record.setPreviousStatus(record.getStatus());
                    }
                    // We do not change flag to Modified if this record is a new
                    // one
                    if( record.getChangeFlag() != ChangedFlag.ADDNEW ) {
                        record.setChangeFlag(ChangedFlag.MODIFIED);
                    }
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
                    record.setQuantity(quantity);
                    // We do not change flag to Modified if this record is a new
                    // one
                    if( record.getChangeFlag() != ChangedFlag.ADDNEW ) {
                        record.setChangeFlag(ChangedFlag.MODIFIED);
                    }
                }
            });
            if( record.getStatus() != Types.State.CANCELED ) {
                totalAmount += record.getPrice() * record.getQuantity();
                paidAmount += record.getPrice() * record.getQuantity();
            }
        }
        tbOrderDetails.setImmediate(true);
    }

    private void setupUI() {
        // Note text field
        txtNote = new TextField(Language.NOTE);
        txtNote.addStyleName("bold large FONT_TAHOMA");
        txtNote.setSizeFull();
        txtNote.setValue(currentOrder.getNote());

        // total amount label
        lbTotalAmount = new Label();
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " "
                + Language.CURRENCY_SYMBOL);
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
        textFieldpaidAmount.setValue(paidAmount + "");
        textFieldpaidAmount
                .addStyleName("v-textfield-dashing bold TEXT_RED FONT_TAHOMA");
        layout.addComponents(lbTotalAmount, lbPaidAmount, textFieldpaidAmount,
                lbCurrencySymbol);
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

        VerticalLayout footer = new VerticalLayout();
        footer.setWidth("100%");

        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        // add food button
        Button btnAddFood = new Button(Language.ADD_FOOD);
        btnAddFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddFood.addStyleName("customizationButton");
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

        HorizontalLayout confirmButtonsContainer = new HorizontalLayout();
        // confirm button
        btnConfirmPaid = new Button(Language.CONFIRM_PAID);
        btnConfirmPaid.addStyleName("customizationButton");
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_HUGE);

        btnConfirmOrder = new Button(Language.CONFIRM_ORDER);
        btnConfirmOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirmOrder.addStyleName("customizationButton");
        // Don't allow some button when no order exist
        if( isNewOrder && orderDetailRecordList.isEmpty()
                && txtNote.getValue().isEmpty() ) {
            btnConfirmOrder.setEnabled(false);
            btnConfirmPaid.setEnabled(false);
        }
        confirmButtonsContainer.addComponents(btnConfirmPaid, btnConfirmOrder);

        footer.addComponents(btnAddFood, confirmButtonsContainer);
        footer.setComponentAlignment(btnAddFood, Alignment.MIDDLE_CENTER);
        footer.setComponentAlignment(confirmButtonsContainer,
                Alignment.MIDDLE_CENTER);

        // Confirm order
        btnConfirmOrder.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // Save the note
                if( currentOrder != null
                        && !currentOrder.getNote().equals(txtNote.getValue()) ) {
                    currentOrder.setNote(txtNote.getValue());
                    Adapter.updateFieldValueOfOrder(currentOrder.getId(),
                            Order.DB_FIELD_NAME_NOTE, txtNote.getValue());
                }

                isOrderDetailListChanged = false;

                List<String> orderDetailIdList = new ArrayList<String>();
                // orderDetailIdList = currentOrder.getOrderDetailIdList();

                for (OrderDetailRecord record : orderDetailRecordList) {
                    OrderDetail odDetail = new OrderDetail();
                    String id = record.getOrderDetailId();
                    if( id.equals("") ) {
                        id = new ObjectId().toString();
                    }
                    orderDetailIdList.add(id);
                    odDetail.setId(id);
                    odDetail.setFoodId(record.getFoodId());
                    odDetail.setQuantity(record.getQuantity());
                    odDetail.setState(record.getStatus());
                    System.out.println("record.getChangeFlag() "
                            + record.getChangeFlag());

                    switch ( record.getChangeFlag() ) {
                        case MODIFIED:
                            // update MODIFIED records
                            System.out.println("Modified");
                            if( !Adapter.updateOrderDetail(odDetail.getId(),
                                    odDetail) ) {
                                System.out
                                        .println("Fail to update order detail, Id: "
                                                + odDetail.getId());
                                isOrderDetailListChanged = false;
                            }
                            else {
                                // set table state to WAITING
                                Adapter.changeTableState(
                                        currentOrder.getTableId(),
                                        Types.State.WAITING);
                                isOrderDetailListChanged = true;
                            }
                            break;
                        case ADDNEW:
                            System.out.println("Add New");
                            // create ADDNEW records
                            if( !Adapter.addNewOrderDetail(odDetail) ) {
                                System.out
                                        .println("Fail to add new order detail, Id: "
                                                + odDetail.getId());
                                isOrderDetailListChanged = false;
                            }
                            else {
                                System.out.println("odDetail.getId() "
                                        + odDetail.getId());
                                // set table state to WAITING
                                Adapter.changeTableState(
                                        currentOrder.getTableId(),
                                        Types.State.WAITING);
                                isOrderDetailListChanged = true;
                            }
                            break;
                        case DELETED:
                            System.out.println("Delete");
                            // delete DELETED records
                            if( !Adapter.removeOrderDetail(odDetail.getId()) ) {
                                System.out
                                        .println("Fail to delete order detail, Id: "
                                                + odDetail.getId());
                                isOrderDetailListChanged = false;
                            }
                            else {
                                orderDetailIdList.remove(orderDetailIdList
                                        .size() - 1);
                                isOrderDetailListChanged = true;
                            }
                            isOrderDetailListChanged = true;
                            break;
                        default:
                            break;
                    }
                }

                // If there's no new food was selected, return
                if( !isOrderDetailListChanged ) {
                    System.out.println("No changes, return");
                    close();
                    return;
                }

                // next, update order
                boolean ret = true;
                currentOrder.setOrderDetailIdList(orderDetailIdList);
                if( isNewOrder ) {
                    System.out.println("is new order");
                    if( Adapter.addNewOrder(currentOrder) ) {
                        System.out.println("Updated orderId: "
                                + currentOrder.getId());
                        System.out.println("\tUpdated orderDetailList: "
                                + currentOrder.getOrderDetailIdList()
                                        .toString());
                    }
                    else {
                        System.out.println("Fail to update orderId: "
                                + currentOrder.getId());
                        ret = false;
                    }
                }
                else {
                    if( Adapter.updateOrderDetailList(currentOrder) ) {
                        // Update order if staffName is different
                        if( !staffName.equals(currentOrder.getStaffName()) ) {
                            currentOrder.setStaffName(staffName);
                            Adapter.updateOrder(currentOrder.getId(),
                                    currentOrder);
                        }
                        System.out.println("Updated orderId: "
                                + currentOrder.getId());
                        System.out.println("\tUpdated orderDetailList: "
                                + currentOrder.getOrderDetailIdList()
                                        .toString());
                    }
                    else {
                        System.out.println("Fail to update orderId: "
                                + currentOrder.getId());
                        ret = false;
                    }
                }

                if( ret == true ) {
                    // Change table state to waiting when we have new order or
                    // change current order
                    Broadcaster.broadcast(CoffeeshopUI.NEW_ORDER_MESSAGE+"::"+getCurrentTable().getNumber());
                    Adapter.changeTableState(getCurrentTable().getId(),
                            State.WAITING);
                    NewOrderManager waitingTimeThread = new NewOrderManager(currentOrder);
                    waitingTimeThread.start();
                }

                // Clear data
                currentOrder = null;
                orderDetailIdList = null;
                orderDetailRecordList = null;

                close();
            }
        });

        // Confirm paid
        btnConfirmPaid.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if( currentOrder != null ) {
                    currentOrder.setPaidMoney(Float
                            .parseFloat(textFieldpaidAmount.getValue()));
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

    /*
     * Get order from db return NULL if no order for this table
     */
    private Order retrieveOrder() {
        System.out.println("loadOrder...");
        List<Order> orderList = Adapter.getOrderListIgnoreStates(
                Types.State.PAID, Types.State.CANCELED, null, null);
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