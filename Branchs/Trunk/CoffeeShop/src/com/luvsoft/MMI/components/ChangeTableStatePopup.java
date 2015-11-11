package com.luvsoft.MMI.components;

import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.Order.OrderInfoView;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ChangeTableStatePopup extends Window implements ClickListener{
    private CoffeeTableElement coffeTableContainer;
    private Table table;
    
    // private PopupView popupChangeTableState;
    private OptionGroup optionState;
    private Button btnAddOrder;
    private Button btnConfirm;
    private OrderInfoView orderInforView;

    public ChangeTableStatePopup(CoffeeTableElement coffeTableContainer, Table table) {
        this.coffeTableContainer = coffeTableContainer;
        this.table = table;
        initView();
    }

    private void initView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        Label lblTableNumber = new Label(Language.TABLE + " " + table.getNumber());
        lblTableNumber.addStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblTableNumber.setWidth("100%");
        
        VerticalLayout vtcPopupContainer = new VerticalLayout();
        vtcPopupContainer.setSizeFull();

        optionState = new OptionGroup();
        optionState.addItems(Language.PAID, Language.UNPAID, Language.EMPTY, Language.WAITING);
        selectOptionState(table.getState());

        vtcPopupContainer.addComponents(lblTableNumber, optionState, buildFooter());

        setContent(vtcPopupContainer);
        // Add click listeners
        btnConfirm.addClickListener(this);
        btnAddOrder.addClickListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getComponent() == btnAddOrder) {
            orderInforView = new OrderInfoView(table);
            orderInforView.populate();
            coffeTableContainer.getUI().addWindow(orderInforView);
            close();
        } else if(event.getComponent() == btnConfirm){
            // Save to db, change the displayed state upon success
            Types.State newState = Types.StringToState(optionState.getValue().toString());

            List<Order> orderList = Adapter.getOrderListIgnoreState(Types.State.COMPLETED);
            Order currentOrder = null;
            for( Order order : orderList ){
                if( order.getTableId().equals(table.getId()) ){
                    currentOrder = order;
                    break;
                }
            }

         // Cannot change from UNPAID to EMPTY if current order is not PAID
            if( table.getState() == Types.State.UNPAID && newState == Types.State.EMPTY 
                    && currentOrder != null && currentOrder.getStatus() != Types.State.PAID){
                System.out.println("Cannot change from UNPAID to EMPTY when current order's not PAID");
                // notify message
                Notification notify = new Notification("<b>Error</b>",
                        "<i>" + Language.CANNOT_CHANGE_TABLE_STATE+"</i>",
                        Notification.Type.TRAY_NOTIFICATION  , true);
                notify.setPosition(Position.BOTTOM_RIGHT);
                notify.show(Page.getCurrent());
            }
            else if( Adapter.changeTableState(table.getId(), newState) ){
                coffeTableContainer.changeTableState(newState, 0);
                // if table is change to empty, set currentOrder to COMPLETED if it exist
                if( newState == Types.State.EMPTY && currentOrder != null ){
                    Adapter.changeOrderState(currentOrder.getId(), Types.State.COMPLETED);
                }
            }
            close();
        }
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirm.addStyleName("customizationButton");
        btnConfirm.setClickShortcut(KeyCode.ENTER, null);

        btnAddOrder = new Button(Language.ADD_ORDER);
        btnAddOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddOrder.addStyleName("customizationButton");

        footer.addComponents(btnAddOrder, btnConfirm);
        footer.setComponentAlignment(btnAddOrder, Alignment.MIDDLE_CENTER);
        return footer;
    }

    private void selectOptionState(Types.State tableState) {
        switch (tableState) {
            case PAID:
                optionState.select(Language.PAID);
                break;
            case UNPAID:
                optionState.select(Language.UNPAID);
                break;
            case EMPTY:
                optionState.select(Language.EMPTY);
                break;
            case WAITING:
                optionState.select(Language.WAITING);
                break;
            default:
                break;
        }
    }
}
