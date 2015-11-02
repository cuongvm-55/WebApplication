package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.MenuButtonListener;
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
/*
 *  @author cuongvm-55
 */

@SuppressWarnings("serial")
public class OrderInfoView extends VerticalLayout{
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

    private TextField textFieldpaidAmount;
    public OrderInfoView(){
        super();
        totalAmount = 0.00f;
        paidAmount = 0.00f;
        init();
        //populate();
    }

    public void init(){
        lbTableName = new Label();
        lbTableName.setStyleName("bold FONT_OVERSIZE FONT_TAHOMA TEXT_CENTER customizationButton");
        lbTableName.setWidth("100%");
        // Set table properties
        tbOrderDetails = new Table("");
        tbOrderDetails.addContainerProperty(Language.SEQUENCE, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.FOOD_NAME, String.class, null);
        tbOrderDetails.addContainerProperty(Language.STATUS, Component.class, null);
        tbOrderDetails.addContainerProperty(Language.QUANTITY, Integer.class, null);
        tbOrderDetails.addContainerProperty(Language.PRICE, Float.class, null);
        tbOrderDetails.addContainerProperty(Language.NOTE, String.class, null);
        //tbOrderDetails.setHeight("100%");
        tbOrderDetails.setPageLength(TABLE_NUMBER_OF_ROWS);
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
            if( Adapter.addNewOrder(order) ){
                orderList.add(order);
            }
        }

        List<OrderInfo> orderInfoList = Adapter.retrieveOrderInfoList(orderList);
        if( orderInfoList.isEmpty() ){
            System.out.println("Invalid orderId ");
            //return;
        }else{
            lbTableName.setValue(orderInfoList.get(0).getTableName());
            // It's should be the first element
            List<OrderDetailRecord> recordList = orderInfoList.get(0).getOrderDetailList();
            //tbOrderDetails.setContainerDataSource(new BeanItemContainer<OrderInfo>(orderInfoList));
            //tbOrderDetails.setVisibleColumns(new Object[] { "name", "description" });
            for(int i = 0; i < recordList.size(); i++){
                Integer itemId = new Integer(i);
                OrderDetailRecord record = recordList.get(i);
                //OrderDetailRecord orderInfo = new OrderDetailRecord();//orderInfoList.get(i);
                //orderInfo.setFoodName("Cà phê đen đádffffffffffffffd<br/>sdsdsdsd");
                //orderInfo.setQuantity(5);
                //orderInfo.setPrice(50.00f);
    
                /*Button detailsField = new Button("show details");
                detailsField.setData(itemId);
                detailsField.addClickListener(new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        // Get the item identifier from the user-defined data.
                        Integer iid = (Integer)event.getButton().getData();
                        Notification.show("Link " +
                                          iid.intValue() + " clicked.");
                    } 
                });
                detailsField.addStyleName("link");
                
                ComboBox select = new ComboBox();
                select.addItem("DONE");
                select.addItem("Out of stock");
                */
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

        // add food button
        Button btnAddFood = new Button(Language.ADD_FOOD);
        btnAddFood.setStyleName("customizationButton");
        btnAddFood.addClickListener(new MenuButtonListener(CoffeeshopUI.ADD_FOOD_VIEW));

        // total amount label
        lbTotalAmount = new Label();
        lbTotalAmount.setValue(Language.TOTAL_AMOUNT + totalAmount + " " + Language.CURRENCY_SYMBOL);
        lbTotalAmount.setStyleName("bold");

        // paid amount label
        HorizontalLayout layout = new HorizontalLayout();
        lbPaidAmount = new Label();
        lbPaidAmount.setValue(Language.PAID_AMOUNT);
        lbPaidAmount.setStyleName("bold");
        paidAmount = totalAmount; // default value of paid amount is equal total amount
        Label lbCurrencySymbol = new Label( " " + Language.CURRENCY_SYMBOL);

        // editable paid amount textfield
        textFieldpaidAmount = new TextField();
        textFieldpaidAmount.setHeight("25");
        textFieldpaidAmount.setWidth("100");
        textFieldpaidAmount.setMaxLength(15);
        textFieldpaidAmount.setValue(paidAmount+"");
        textFieldpaidAmount.setStyleName("v-textfield-dashing");
        layout.addComponents(lbPaidAmount, textFieldpaidAmount, lbCurrencySymbol);
        layout.setSpacing(true);
        // confirm button
        Button btnConfirmPaid = new Button(Language.CONFIRM_PAID);
        btnConfirmPaid.setStyleName("customizationButton");
        
        Button btnConfirmOrder = new Button(Language.CONFIRM_ORDER);
        btnConfirmOrder.setStyleName("customizationButton");
        
        HorizontalLayout confirmLayout = new HorizontalLayout();
        confirmLayout.addComponents(btnConfirmPaid, btnConfirmOrder);
        confirmLayout.setSpacing(true);
        layoutOrderHandling = new VerticalLayout();
        layoutOrderHandling.addComponents(btnAddFood, lbTotalAmount, layout, confirmLayout);
        layoutOrderHandling.setComponentAlignment(btnAddFood, Alignment.MIDDLE_CENTER);
        layoutOrderHandling.setComponentAlignment(confirmLayout, Alignment.MIDDLE_CENTER);
        this.addComponents(lbTableName, tbOrderDetails, layoutOrderHandling);
        this.setExpandRatio(lbTableName, 1.0f);
        this.setExpandRatio(tbOrderDetails, 6.0f);
        this.setExpandRatio(layoutOrderHandling, 3.0f);
        this.setSpacing(true);
        this.setResponsive(true);
        this.setSizeFull();
        this.setComponentAlignment(lbTableName, Alignment.TOP_CENTER);
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
    }

    public static OrderInfoView getInstance(){
        if( instance == null ){
            instance = new OrderInfoView();
        }
        return instance;
    }
}