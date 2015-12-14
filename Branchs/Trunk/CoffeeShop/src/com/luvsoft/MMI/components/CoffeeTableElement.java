package com.luvsoft.MMI.components;

import com.luvsoft.MMI.TableListView;
import com.luvsoft.MMI.order.ChangeTableStateView;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
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
public class CoffeeTableElement extends VerticalLayout implements ClickListener {
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

        this.addStyleName("card");
        this.setSizeFull();

        this.btnTableState = new Image();
        this.btnTableState.setWidth("70%");
        this.btnTableState.setHeight("70%");
        this.btnTableState.addStyleName("hoverImage");
        this.setButtonResource(state);
        
        this.lblWaitingTime = new Label();
        this.lblWaitingTime.setSizeUndefined();
        if(state == Types.State.PAID) {
            setPaymentState(Types.State.PAID);
        } else if (state == Types.State.UNPAID) {
            setPaymentState(Types.State.UNPAID);
        } else if (state == Types.State.WAITING){
            if( order == null ){
                setWaitingTime(-1);
            }
            else{
                this.setWaitingTime(table.getWaitingTime());
            }
        } else {
            this.setWaitingTime(0);
        }

        this.lblTableNumber = new Label(Language.TABLE + " " + table.getNumber());
        this.lblTableNumber.addStyleName("TEXT_BLUE");
        this.lblTableNumber.addStyleName(ValoTheme.LABEL_HUGE);
        this.lblTableNumber.addStyleName(ValoTheme.LABEL_BOLD);
        this.lblTableNumber.setSizeUndefined();

        this.addComponents(btnTableState, lblWaitingTime, lblTableNumber);
        this.setComponentAlignment(btnTableState, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblWaitingTime, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblTableNumber, Alignment.MIDDLE_CENTER);

        // Add click listener
        this.btnTableState.addClickListener(this);
    }

    /*
     * This function is used to set waiting time for a table
     * @param waiting time
     */
    private void setWaitingTime(int waitingTime) {
        if (0 < waitingTime) {
            this.lblWaitingTime.addStyleName("TEXT_RED");
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_BOLD);
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_LARGE);
            this.lblWaitingTime.setValue(Language.WAITING + " " + waitingTime
                    + " " + Language.MINUTE);
        }
        else if( waitingTime == -1){
            // no order for this table
            this.lblWaitingTime.addStyleName("TEXT_RED");
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_BOLD);
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_LARGE);
            this.lblWaitingTime.setValue(Language.NO_ORDER_IN_THIS_TABLE);
        }
        else {
            this.lblWaitingTime.addStyleName("TEXT_WHITE");
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_BOLD);
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_LARGE);
            this.lblWaitingTime.setValue("NO TIME");
        }
    }

    private void setPaymentState(Types.State state) {
        if(state == Types.State.PAID) {
            this.lblWaitingTime.addStyleName("TEXT_RED");
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_BOLD);
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_LARGE);
            this.lblWaitingTime.setValue(Language.PAID);
        } else if(state == Types.State.UNPAID) {
            this.lblWaitingTime.addStyleName("TEXT_RED");
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_BOLD);
            this.lblWaitingTime.addStyleName(ValoTheme.LABEL_LARGE);
            this.lblWaitingTime.setValue(Language.UNPAID);
        }
        else{
            setWaitingTime(0);
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
                this.setWaitingTime(-1);
            }
            else{
                this.setWaitingTime(waitingTime);
            }
        } else {
            this.setWaitingTime(0);
        }
    }
    /*
     * Navigate to OrderInfoView
     */
    @Override
    public void click(ClickEvent event) {
        if (event.getComponent() == btnTableState) {
            System.out.println("curTblId: " + table.getId());
            changeTableStateView = new ChangeTableStateView();
            changeTableStateView.setParentView(tableListView);
            changeTableStateView.setCurrentTable(table);
            changeTableStateView.createView();
            UI.getCurrent().addWindow(changeTableStateView);
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
}
