package com.luvsoft.MMI.components;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.TableListView;
import com.luvsoft.MMI.order.ChangeTableStateView;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author datnq.55
 * This class is used to create a coffee table element
 *
 */
@SuppressWarnings("serial")
public class CoffeeTableElement extends CssLayout implements ClickListener {
    private TableListView tableListView;
    private Image btnTableState;
    private Label lblWaitingTime;
    private Label lblTableNumber;
    private ChangeTableStateView changeTableStateView;

    // data
    private Table table; // table info
    private Order order; // the current Order of this table

    public CoffeeTableElement(Table table, TableListView tableListView) {
        super();
        this.table = table;
        this.tableListView = tableListView;
        System.out.println("Created CoffeeTableElement for tblId: " + table.getId());
    }

    /*
     * This function is used to initialize a coffee table element
     * @param: Table state, waiting time and table number
     */
    public void initCoffeeTableElement() {
        Types.State state = table.getState();

        this.addStyleName("card TEXT_ALIGN_CENTER");
        this.setSizeFull();
        this.removeAllComponents();

        this.btnTableState = new Image();
        this.btnTableState.setWidth("70%");
        this.btnTableState.setHeight("70%");
        this.btnTableState.addStyleName("hoverImage TEXT_ALIGN_CENTER");
        this.setButtonResource(state);
        
        this.lblWaitingTime = new Label();
        this.lblWaitingTime.addStyleName("TEXT_CENTER FONT_OVER_OVERSIZE " + ValoTheme.LABEL_BOLD);
        if(state == Types.State.PAID) {
            setPaymentState(Types.State.PAID);
        } else if (state == Types.State.UNPAID) {
            setPaymentState(Types.State.UNPAID);
        } else if (state == Types.State.WAITING){
            if( order == null ){
                setWaitingTimeLabel(-1);
            }
            else{
                this.setWaitingTimeLabel(table.getWaitingTime());
            }
        } else {
            this.setWaitingTimeLabel(0);
        }

        this.lblTableNumber = new Label(Language.TABLE + " " + table.getNumber());
        this.lblTableNumber.addStyleName("TEXT_BLUE TEXT_CENTER FONT_OVER_OVERSIZE " + ValoTheme.LABEL_BOLD);

        this.addComponents(lblTableNumber, btnTableState, lblWaitingTime);
        // this.setComponentAlignment(btnTableState, Alignment.MIDDLE_CENTER);

        // Add click listener
        this.btnTableState.addClickListener(this);
    }

    /*
     * This function is used to set waiting time for a table
     * @param waiting time
     */
    public  void setWaitingTimeLabel(int waitingTime) {
        if (0 < waitingTime) {
            this.lblWaitingTime.addStyleName("TEXT_RED FONT_OVER_OVERSIZE");
            this.lblWaitingTime.setValue(Language.WAITING + " " + waitingTime + " " + Language.MINUTE);
        }
        else if( waitingTime == -1){
            // no order for this table
            this.lblWaitingTime.addStyleName("TEXT_RED FONT_OVER_OVERSIZE");
            this.lblWaitingTime.setValue(Language.NO_ORDER_IN_THIS_TABLE);
        }
        else {
            this.lblWaitingTime.addStyleName("TEXT_WHITE FONT_OVER_OVERSIZE");
            this.lblWaitingTime.setValue(".");
        }
    }

    private void setPaymentState(Types.State state) {
        if(state == Types.State.PAID) {
            this.lblWaitingTime.addStyleName("TEXT_RED FONT_OVER_OVERSIZE");
            this.lblWaitingTime.setValue(Language.PAID);
        } else if(state == Types.State.UNPAID) {
            this.lblWaitingTime.addStyleName("TEXT_RED FONT_OVER_OVERSIZE");
            this.lblWaitingTime.setValue(Language.UNPAID);
        }
        else{
            setWaitingTimeLabel(0);
        }
    }
    /*
     * This function is used to change button state
     * @param state of table
     */
    private void setButtonResource(Types.State tableState) {
        switch (tableState) {
            case PAID: {
                this.btnTableState
                        .setSource(new ThemeResource("images/red.png"));
                break;
            }
            case UNPAID: {
                this.btnTableState
                        .setSource(new ThemeResource("images/red.png"));
                break;
            }
            case WAITING: {
                this.btnTableState.setSource(new ThemeResource(
                        "images/yellow.png"));
                break;
            }
            case EMPTY: {
                this.btnTableState.setSource(new ThemeResource(
                        "images/blue.png"));
                break;
            }
            default:
                break;
        }
    }

    public void changeTableState(Types.State tableState, int waitingTime) {
        setButtonResource(tableState);
        if(tableState == Types.State.PAID) {
            setPaymentState(Types.State.PAID);
        } else if (tableState == Types.State.UNPAID) {
            setPaymentState(Types.State.UNPAID);
        } else if (tableState == Types.State.WAITING){
            if( order == null ){
                this.setWaitingTimeLabel(-1);
            }
            else{
                this.setWaitingTimeLabel(waitingTime);
            }
        } else {
            this.setWaitingTimeLabel(0);
        }
    }
    /*
     * Navigate to changeTableStateView
     */
    @Override
    public void click(ClickEvent event) {
        if (event.getComponent() == btnTableState) {
            System.out.println("curTblId: " + table.getId());
            changeTableStateView = new ChangeTableStateView();
            changeTableStateView.setParentView(tableListView);
            changeTableStateView.setCurrentTable(table);
            if(order != null) {
                changeTableStateView.setOrder(order);
            }
            changeTableStateView.createView();
            UI.getCurrent().addWindow(changeTableStateView);
            changeTableStateView.setImmediate(true);
        }
    }

    public static Types.State StringToTableState(String str){
        Types.State ret = Types.State.EMPTY;
        switch(str){
            case "WAITING":
                ret = Types.State.WAITING;
                break;
            case "PAID":
                ret = Types.State.PAID;
                break;
            case "UNPAID":
                ret = Types.State.UNPAID;
                break;
            default:
                break;
        }
        return ret;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    /**
     * Make the Table state to be compliant with its Order state
     * @param table
     * @param curOrder current Order of this table
     */
    public static void updateTableState(Table table, Order curOrder){
        Types.State tblState = State.UNDEFINED;
        if( curOrder == null ){
            tblState = State.EMPTY;
        }
        else{
            switch(curOrder.getStatus()){
                case CANCELED:
                    tblState = State.EMPTY;
                    break;
                default:
                    tblState = curOrder.getStatus();
                    break;
            }
        }
        // Save state to db
        if( !Adapter.changeTableState(table.getId(), tblState) ){
            System.out.println("Fail to update table " + table.getNumber() + " to state " + tblState.toString());
        }
        
    }
}
