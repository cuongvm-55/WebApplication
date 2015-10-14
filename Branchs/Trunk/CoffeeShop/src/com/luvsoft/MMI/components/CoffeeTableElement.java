package com.luvsoft.MMI.components;

import com.luvsoft.MMI.utils.Language;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CoffeeTableElement extends VerticalLayout implements ClickListener {
    
    private Image btnTableState;
    private Label lblWaitingTime;
    private Label lblTableNumber;
    private String tableId;
    
    public enum TABLE_STATE {
        STATE_FULL, STATE_WAITING, STATE_EMPTY
    }
    
    public CoffeeTableElement(TABLE_STATE tableState, int waitingTime,
            int tableNumber) {
        super();
        
        this.setStyleName("card");
        this.setSizeFull();
        
        this.btnTableState = new Image();
        this.btnTableState.setWidth("50%");
        this.btnTableState.setHeight("50%");
        this.setButtonResource(tableState);
        
        this.lblWaitingTime = new Label();
        this.lblWaitingTime.setStyleName("small bold TEXT_RED");
        this.lblWaitingTime.setSizeUndefined();
        this.setWaitingTime(waitingTime);
        
        this.lblTableNumber = new Label(Language.TABLE + " " + tableNumber);
        this.lblTableNumber.setStyleName("bold TEXT_BLUE");
        this.lblTableNumber.setSizeUndefined();
        
        this.addComponents(btnTableState, lblWaitingTime, lblTableNumber);
        this.setComponentAlignment(btnTableState, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblWaitingTime, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(lblTableNumber, Alignment.MIDDLE_CENTER);
        
        // Add click listener
        this.btnTableState.addClickListener(this);
    }
    
    public void setWaitingTime(int waitingTime) {
        if (0 < waitingTime) {
            this.lblWaitingTime.setValue(Language.WAITING + " " + waitingTime
                    + " " + Language.MINUTE);
        }
    }
    
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
    
    @Override
    public void click(ClickEvent event) {
        if (event.getComponent() == btnTableState) {
            System.out.println("Image was clicked");
        }
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
