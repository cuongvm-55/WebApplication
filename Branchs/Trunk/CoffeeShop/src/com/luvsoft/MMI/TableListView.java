package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.components.LuvsoftNotification;
import com.luvsoft.MMI.management.TableForm;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TableListView extends VerticalLayout implements ViewInterface {
    private List<Floor> floorList;
    private List<Table> tableList;
    private ViewInterface parentView;
    private List<CoffeeTableElement> listTableElement;  

    public TableListView(){
        super();
        listTableElement = new ArrayList<CoffeeTableElement>();
    }

    @Override
    public void createView() {
        this.setWidth("100%");
        this.setHeightUndefined();
        this.addStyleName("table-list-view");
        removeAllComponents();
        listTableElement.clear();

        floorList = Adapter.retrieveFloorList();
        if( floorList == null ){
            System.out.println("There's no floor!");
            return;
        }

        String staffName = ((MainView) getParentView()).getStaffName();
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.PAID);
        states.add(Types.State.CANCELED);
        List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
        for (int j = 0; j < floorList.size(); j++) {
            GridLayout gridElementContent = new GridLayout();
            gridElementContent.setColumns(3);
            gridElementContent.setWidth("100%");
            System.out.println("FloorId: " + floorList.get(j).getId());
            tableList = Adapter.retrieveTableList(floorList.get(j).getTableIdList());
            // Add components to grid layout
            for (int i = 0; i < tableList.size(); i++) {
                Table table = tableList.get(i);
                System.out.println("TableId: " + table.getId());
                Order currentOrder = null;
                for (Order order : orderList) {
                    if( order.getTableId().equals(table.getId()) ) {
                        currentOrder = order;
                        break;
                    }
                }

                if(currentOrder != null) {
                    table.setWaitingTime(currentOrder.getWaitingTime());
                } else {
                    table.setWaitingTime(0);
                }

                table.setStaffName(staffName);

                // Set order for table and init the view
                CoffeeTableElement tableElement = new CoffeeTableElement(table, this);
                tableElement.setOrder(currentOrder);
                tableElement.initCoffeeTableElement();

                gridElementContent.addComponent(tableElement);
                listTableElement.add(tableElement);
            }

            // Add table button
            Button btnAddTbl = new Button();
            btnAddTbl.setIcon(FontAwesome.PLUS_CIRCLE);
            btnAddTbl.setStyleName("icon-only primary TEXT_WHITE " + ValoTheme.BUTTON_LARGE);
            btnAddTbl.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    TableForm form = new TableForm(new Table(), TableForm.STATE.ADDNEW);
                    form.setParentView(TableListView.this);
                    getUI().addWindow(form);
                }
            });

            CustomizationTreeElement treeElement = new CustomizationTreeElement(gridElementContent, Language.FLOOR + " " + floorList.get(j).getNumber(), btnAddTbl);
            treeElement.getBtnExpandElement().addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    if( treeElement.getContent().isVisible() ){
                        treeElement.setContentCollapse();
                    }
                    else{
                        treeElement.setContentExpand();
                    }
                }
            });
            this.addComponent(treeElement);
        }
    }

    @Override
    public void reloadView() {
        createView();
    }

    /**
     * This function is used to update display of any table when it has new order 
     * 
     * @param tableId
     */
    public void haveNewOrder(String messageData) {
        String tableId;
        String str[] = messageData.split("::");
        tableId = str[0];

        Table table = new Table();
        updateCoffeeTableElementByTableId(tableId, table);

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>",
                "<i>" + Language.TABLE + " " + table.getNumber() + Language.ORDERED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * This function is used to update order, table when it performs a change action
     * 
     * @param messageData
     */
    public void doChangeTableAndOrder(String messageData) {
        String srcTableId, desTableId;
        String str[] = messageData.split("::");
        srcTableId = str[0];
        desTableId = str[1];

        Table desTable = new Table();
        Table srcTable = new Table();
        updateCoffeeTableElementByTableId(desTableId, desTable);
        updateCoffeeTableElementByTableId(srcTableId, srcTable);

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + "Đặt món ở bàn " + srcTable.getNumber() + " đã được chuyển sang bàn " + desTable.getNumber() + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * 
     * @param messageData
     */
    public void haveNewOrderUpdated(String messageData) {
        String tableId;
        String str[] = messageData.split("::");
        tableId = str[0];

        Table table = new Table();
        updateCoffeeTableElementByTableId(tableId, table);

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.ORDER_IN_TABLE + table.getNumber() + Language.HAS_BEEN_UPDATED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();

    }

    /**
     * This function is used to update display of any table when it's order was completed
     * @param tableId
     */
    public void haveNewOrderCompleted(String messageData) {
        String tableId;
        String str[] = messageData.split("::");
        tableId = str[0];

        Table table = new Table();
        updateCoffeeTableElementByTableId(tableId, table);

        // notify message
        LuvsoftNotification notify = new LuvsoftNotification("<b>"+Language.PAY_ATTENTION+"</b>",
                "<i>" + Language.ORDER + " " + table.getNumber() + " " + Language.COMPLETED+"</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * This function is used to update display of any table when it's order was paid
     * @param tableId
     */
    public void orderWasPaid(String messageData) {
        String tableId;
        String str[] = messageData.split("::");
        tableId = str[0];

        Table table = new Table();
        updateCoffeeTableElementByTableId(tableId, table);

        // notify message
        LuvsoftNotification notify = new LuvsoftNotification("<b>"+Language.PAY_ATTENTION+"</b>",
                "<i>Bàn " + table.getNumber() +" đã thanh toán</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * This function is used to update display of any table when its order details was completed
     * 
     * @param messageData
     */
    public void foodWasCompleted(String messageData){
        String message;
        String str[] = messageData.split("::");
        message = str[0];

        // notify message
        LuvsoftNotification notify = new LuvsoftNotification("<b>"+Language.PAY_ATTENTION+"</b>",
                message,
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * This function is used to update table display when it's order was canceled
     * @param tableId
     */
    public void haveCanceledOrder(String messageData) {
        String tableId;
        String str[] = messageData.split("::");
        tableId = str[0];

        Table table = new Table();
        updateCoffeeTableElementByTableId(tableId, table);

        LuvsoftNotification notify = new LuvsoftNotification("<b>"+ Language.PAY_ATTENTION +"</b>", "<i>"
                + Language.ORDER_IN_TABLE + table.getNumber() + Language.HAS_BEEN_CANCELED + "</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();
    }

    /**
     * This function is used to update table display when it's order was changed state
     * @param tableId
     */
    public void changeTableState(String tableId) {
        updateCoffeeTableElementByTableId(tableId);
    }

    /**
     * This function is used to update waiting time display of any table
     * 
     * @param messageData
     */
    public void updateWaitingTime(String messageData) {
        String tableId, wattingTime;
        String str[] = messageData.split("::");
        tableId = str[0];
        wattingTime = str[1];

        for (CoffeeTableElement coffeeTableElement : listTableElement) {
            if(tableId.equals(coffeeTableElement.getTable().getId())) {
                coffeeTableElement.setWaitingTimeLabel(Integer.parseInt(wattingTime));
            }
        }
    }

    /**
     * This function is used as an API to update a table element when have a request
     * 
     * @param tableId
     */
    private void updateCoffeeTableElementByTableId(String tableId) {
        Table table = Adapter.getTableById(tableId);
        if(table == null) {
            System.out.println("Cannot get the table");
            return;
        }
        doUpdateCoffeeTableElement(tableId, table);
    }

    /**
     * This function is used as an API to update a table element when have a request
     * 
     * @param tableId, table
     */
    private void updateCoffeeTableElementByTableId(String tableId, Table table) {
        Table tempTable = Adapter.getTableById(tableId);
        if(tempTable == null) {
            return;
        }
        table.setTable(tempTable);
        
        doUpdateCoffeeTableElement(tableId, table);
    }

    private void doUpdateTableElementsList(String srcOrderId, String destOderId) {
        for (CoffeeTableElement coffeeTableElement : listTableElement) {
            if(coffeeTableElement.getOrder().getId().equals(srcOrderId)) {
                Order order = Adapter.getOrder(destOderId);
                coffeeTableElement.setOrder(order);
            }
        }
    }

    private void doUpdateCoffeeTableElement(String tableId, Table table) {
        for (CoffeeTableElement coffeeTableElement : listTableElement) {
            if(tableId.equals(coffeeTableElement.getTable().getId())) {
               // Get order of current table
                List<Types.State> states = new ArrayList<Types.State>();
                states.add(Types.State.PAID);
                states.add(Types.State.CANCELED);
                List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
                Order currentOrder = null;
                for (Order order : orderList) {
                    if( order.getTableId().equals(tableId) ) {
                        currentOrder = order;
                        break;
                    }
                }

                if(currentOrder != null) {
                    table.setWaitingTime(currentOrder.getWaitingTime());
                    coffeeTableElement.setOrder(currentOrder);
                } else {
                    coffeeTableElement.setOrder(null);
                }

                String staffName = ((MainView) getParentView()).getStaffName();
                table.setStaffName(staffName);

                coffeeTableElement.setTable(table);
                coffeeTableElement.initCoffeeTableElement();
            }
        }
    }

    @Override
    public ViewInterface getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        this.parentView = parentView;
    }
}
