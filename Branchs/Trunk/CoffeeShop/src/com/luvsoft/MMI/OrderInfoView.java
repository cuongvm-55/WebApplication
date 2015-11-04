package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
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
    public static OrderInfoView instance; //singleton
    private Label lbTableName;
    private Label lbTotalAmount; // label of amount caculate automatically, the amount can not be modified
    private Label lbPaidAmount; // label real amount that customer pay for the bill, the amount can be modified

    //private Panel panelContentContainer;
    private Table tbOrderDetails;
    private VerticalLayout layoutOrderHandling; // contains some control buttons

    private float totalAmount;
    private float paidAmount;
    private VerticalLayout container;

    private TextField textFieldpaidAmount;
    public OrderInfoView(){
        super();
        totalAmount = 0.00f;
        paidAmount = 0.00f;
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
        lbTableName.setValue(Language.TABLE + " " + MainView.getInstance().getCurrentTable().getNumber());
        // Set table properties
        tbOrderDetails = new Table("");
        tbOrderDetails.addStyleName("bold large FONT_TAHOMA");
        tbOrderDetails.setSizeFull();
        tbOrderDetails.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderDetails.addContainerProperty(Language.STATUS, Component.class, null);
        tbOrderDetails.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.PRICE, Float.class, null);
        // tbOrderDetails.addContainerProperty(Language.NOTE, String.class, null);
        //tbOrderDetails.setHeight("100%");
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);

        setupUI();
    }

    public void populate() {
        // Fill data
        List<Order> orderList = new ArrayList<Order >();
        // find order correspond to current selected table
        for( Order order: MainView.getInstance().getOrderList() ){
            if( order.getTableId() == MainView.getInstance().getCurrentTable().getId() ){
                orderList.add(order);
            }
        }

        if( orderList.isEmpty() ){
            System.out.println("No order's created for current table, create a new order");
            Order order = new Order();
            order.setTableId(MainView.getInstance().getCurrentTable().getId());
            ///if( Adapter.addNewOrder(order) ){
            orderList.add(order);
            //}
            return;
        }

        List<OrderInfo> orderInfoList = Adapter.retrieveOrderInfoList(orderList);
        if( orderInfoList.isEmpty() ){
            System.out.println("orderId is not exist!");
            //return;
        }else{
            lbTableName.setValue(orderInfoList.get(0).getTableName());
            // It's should be the first element
            List<OrderDetailRecord> recordList = orderInfoList.get(0).getOrderDetailList();
            for(int i = 0; i < recordList.size(); i++){
                Integer itemId = new Integer(i);
                OrderDetailRecord record = recordList.get(i);
                // Create the table row.
                tbOrderDetails.addItem(new Object[] {new Integer(i), record.getFoodName(),
                        record.getIconFromStatus(record.getStatus()), record.getQuantity(), record.getPrice(), null},
                              itemId);
                // calculate totalAmount
                totalAmount+= record.getPrice();
            }
        }
        /*tbOrderDetails.addItem(new Object[] {new Integer(1), "Food name 1",
                new Label("edit"), 3, 50.0f, null},
                      new Integer(0)); */
        paidAmount = totalAmount; // default value of paid amount is equal total amount
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " " + Language.CURRENCY_SYMBOL);
        textFieldpaidAmount.setValue(paidAmount+"");
    }
    private void setupUI(){
     // Note text field
        TextField txtNote = new TextField(Language.NOTE);
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
        // TODO btnAddFood.addClickListener(new MenuButtonListener(CoffeeshopUI.ADD_FOOD_VIEW));

        HorizontalLayout confirmButtonsContainer = new HorizontalLayout();
        // confirm button
        Button btnConfirmPaid = new Button(Language.CONFIRM_PAID);
        btnConfirmPaid.addStyleName("customizationButton");
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_HUGE);

        Button btnConfirmOrder = new Button(Language.CONFIRM_ORDER);
        btnConfirmOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirmOrder.addStyleName("customizationButton");

        confirmButtonsContainer.addComponents(btnConfirmPaid, btnConfirmOrder);

        footer.addComponents(btnAddFood, confirmButtonsContainer);
        footer.setComponentAlignment(btnAddFood, Alignment.MIDDLE_CENTER);
        footer.setComponentAlignment(confirmButtonsContainer, Alignment.MIDDLE_CENTER);
        return footer;
    }
}