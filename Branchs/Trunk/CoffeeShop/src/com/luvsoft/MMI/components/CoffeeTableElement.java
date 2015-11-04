package com.luvsoft.MMI.components;

import com.luvsoft.MMI.TableListView;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author datnq.55
 * This class is used to create a coffee table element
 *
 */
@SuppressWarnings("serial")
public class CoffeeTableElement extends VerticalLayout implements ClickListener {
    private TableListView tableListView;
    private Table table;
    private Image btnTableState;
    private Label lblWaitingTime;
    private Label lblTableNumber;
    private String tableId;
    private ChangeTableStatePopup changeTableStatePopup;

    public CoffeeTableElement(Table table, TableListView tableListView) {
        super();
        this.table = table;
        this.tableListView = tableListView;
        initCoffeeTableElement();
    }

    /*
     * This function is used to initialize a coffee table element
     * @param: Table state, waiting time and table number
     */
    private void initCoffeeTableElement() {
        Types.State state = table.getState();

        this.setStyleName("card");
        this.setSizeFull();

        this.btnTableState = new Image();
        this.btnTableState.setWidth("50%");
        this.btnTableState.setHeight("50%");
        this.btnTableState.setStyleName("hoverImage");
        this.setButtonResource(state);
        
        this.lblWaitingTime = new Label();
        this.lblWaitingTime.setSizeUndefined();
        if(state == Types.State.PAID) {
            setPaymentState(Types.State.PAID);
        } else if (state == Types.State.UNPAID) {
            setPaymentState(Types.State.UNPAID);
        } else if (state == Types.State.WAITING){
            this.setWaitingTime(table.getWaitingTime());
        } else {
            this.setWaitingTime(0);
        }

        this.lblTableNumber = new Label(Language.TABLE + " " + table.getNumber());
        this.lblTableNumber.setStyleName("huge bold TEXT_BLUE");
        this.lblTableNumber.setSizeUndefined();

        changeTableStatePopup = new ChangeTableStatePopup(this, tableListView, table);

        this.addComponents(btnTableState, lblWaitingTime, lblTableNumber);
        this.setComponentAlignment(btnTableState, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblWaitingTime, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblTableNumber, Alignment.MIDDLE_CENTER);

//        changeTableStatePopup.getPopup().addPopupVisibilityListener(new PopupVisibilityListener() {
//            @Override
//            public void popupVisibilityChange(PopupVisibilityEvent event) {
//                if(event.isPopupVisible()) {
//                    MainView.getInstance().getMainLayout().setEnabled(false);
//                } else {
//                    MainView.getInstance().getMainLayout().setEnabled(true);
//                }
//            }
//        });
        // Add click listener
        this.btnTableState.addClickListener(this);
    }

    /*
     * This function is used to set waiting time for a table
     * @param waiting time
     */
    private void setWaitingTime(int waitingTime) {
        if (0 < waitingTime) {
            this.lblWaitingTime.setStyleName("huge bold TEXT_RED");
            this.lblWaitingTime.setValue(Language.WAITING + " " + waitingTime
                    + " " + Language.MINUTE);
        } else {
            this.lblWaitingTime.setStyleName("huge bold TEXT_WHITE");
            this.lblWaitingTime.setValue("NO TIME");
        }
    }

    private void setPaymentState(Types.State state) {
        if(state == Types.State.PAID) {
            this.lblWaitingTime.setStyleName("huge bold TEXT_RED");
            this.lblWaitingTime.setValue(Language.PAID);
        } else if(state == Types.State.UNPAID) {
            this.lblWaitingTime.setStyleName("huge bold TEXT_RED");
            this.lblWaitingTime.setValue(Language.UNPAID);
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
            this.setWaitingTime(waitingTime);
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
            // CoffeeshopUI.navigator.navigateTo(CoffeeshopUI.MAIN_VIEW + "/" + CoffeeshopUI.ORDER_INFO_VIEW);
            // TODO changeTableStatePopup.setPopup
            tableListView.getUI().addWindow(changeTableStatePopup);
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
    
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
