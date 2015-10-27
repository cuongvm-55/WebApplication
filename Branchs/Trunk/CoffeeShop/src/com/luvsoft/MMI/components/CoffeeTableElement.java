package com.luvsoft.MMI.components;

import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.utils.Language;
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
    private Image btnTableState;
    private Label lblWaitingTime;
    private Label lblTableNumber;
    private String tableId;
    
    public enum TABLE_STATE {
        STATE_FULL, STATE_WAITING, STATE_EMPTY
    }
    
    public CoffeeTableElement(TABLE_STATE tableState, int waitingTime,
            String tableName) {
        super();
        initCoffeeTableElement(tableState, waitingTime, tableName);
    }

    /*
     * This function is used to initialize a coffee table element
     * @param: Table state, waiting time and table number
     */
    private void initCoffeeTableElement(TABLE_STATE tableState, int waitingTime,
            String tableName) {
        this.setStyleName("card");
        this.setSizeFull();

        this.btnTableState = new Image();
        this.btnTableState.setWidth("50%");
        this.btnTableState.setHeight("50%");
        this.setButtonResource(tableState);
        
        this.lblWaitingTime = new Label();
        this.lblWaitingTime.setSizeUndefined();
        this.setWaitingTime(waitingTime);
        
        this.lblTableNumber = new Label(tableName);
        this.lblTableNumber.setStyleName("huge bold TEXT_BLUE");
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
    public void setWaitingTime(int waitingTime) {
        if (0 < waitingTime) {
            this.lblWaitingTime.setStyleName("huge bold TEXT_RED");
            this.lblWaitingTime.setValue(Language.WAITING + " " + waitingTime
                    + " " + Language.MINUTE);
        } else {
            this.lblWaitingTime.setStyleName("huge bold TEXT_WHITE");
            this.lblWaitingTime.setValue("NO TIME");
        }
    }

    /*
     * This function is used to change button state
     * @param state of table
     */
    public void setButtonResource(TABLE_STATE tableState) {
        switch (tableState) {
            case STATE_FULL: {
                this.btnTableState
                        .setSource(new ThemeResource("images/red.png"));
                break;
            }
            case STATE_WAITING: {
                this.btnTableState.setSource(new ThemeResource(
                        "images/yellow.png"));
                break;
            }
            case STATE_EMPTY: {
                this.btnTableState.setSource(new ThemeResource(
                        "images/blue.png"));
                break;
            }
            default:
                break;
        }
    }
    
    /*
     * Navigate to OrderInfoView
     */
    @Override
    public void click(ClickEvent event) {
        if (event.getComponent() == btnTableState) {
            // System.out.println("Image was clicked");
            CoffeeshopUI.navigator.navigateTo(CoffeeshopUI.MAIN_VIEW + "/" + CoffeeshopUI.ORDER_INFO_VIEW);
        }
    }

    public static TABLE_STATE StringToTableState(String str){
        TABLE_STATE ret = TABLE_STATE.STATE_EMPTY;
        switch(str){
        case "WAITING":
            ret = TABLE_STATE.STATE_WAITING;
            break;
        case "FULL":
            ret = TABLE_STATE.STATE_FULL;
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
