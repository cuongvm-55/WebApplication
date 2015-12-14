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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TableListView extends VerticalLayout implements ViewInterface {
    private List<Floor> floorList;
    private List<Table> tableList;
    private ViewInterface parentView;
    public TableListView(){
        super();
    }

    @Override
    public void createView() {
        removeAllComponents();
        this.setWidth("100%");
        this.setHeightUndefined();
        this.addStyleName("table-list-view");
        floorList = Adapter.retrieveFloorList();
        if( floorList == null ){
            System.out.println("There's no floor!");
            return;
        }

        String staffName = ((MainView) getParentView()).getStaffName();

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
                List<Types.State> states = new ArrayList<Types.State>();
                states.add(Types.State.PAID);
                states.add(Types.State.CANCELED);
                List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
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
                CoffeeTableElement tableElement = new CoffeeTableElement(table, this);
                gridElementContent.addComponent(tableElement);
            }

            // Add table button
            Button btnAddTbl = new Button();
            btnAddTbl.setHeight("100%");
            btnAddTbl.setIcon(FontAwesome.PLUS_CIRCLE);
            btnAddTbl.setStyleName("icon-only primary TEXT_WHITE");
            btnAddTbl.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    TableForm form = new TableForm(new Table(), TableForm.STATE.ADDNEW);
                    form.setParentView(TableListView.this);
                    getUI().addWindow(form);
                }
            });

            CustomizationTreeElement treeElement = new CustomizationTreeElement(gridElementContent, Language.FLOOR + " " + floorList.get(j).getNumber(), btnAddTbl);
            this.addComponent(treeElement);
        }
    }

    @Override
    public void reloadView() {
        createView();
    }

    public void haveNewOrderCompleted(String tableNumber) {
        // notify message
        LuvsoftNotification notify = new LuvsoftNotification("<b>"+Language.PAY_ATTENTION+"</b>",
                "<i>" + Language.ORDER + " " + tableNumber + " " + Language.COMPLETED+"</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();

        reloadView();
    }

    public void orderWasPaid(String tableNumber) {
        // notify message
        LuvsoftNotification notify = new LuvsoftNotification("<b>"+Language.PAY_ATTENTION+"</b>",
                "<i>Bàn " + tableNumber +" đã thanh toán</i>",
                Notification.Type.WARNING_MESSAGE);
        notify.show();

        reloadView();
    }

    public void haveCanceledOrder() {
        reloadView();
    }

    public void updateWaitingTime() {
        reloadView();
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
