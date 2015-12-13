package com.luvsoft.MMI.order;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.order.OrderInfoView.ViewMode;
import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.threads.NewOrderManager;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ChangeTableStateView extends AbstractOrderView implements
        ClickListener {

    // private PopupView popupChangeTableState;
    private OptionGroup optionState;
    private Button btnAddOrder;
    private Button btnConfirm;
    private Button btnConfirmPaid;
    private OrderInfoView orderInforView;
    private Types.State newState;
    private Order currentOrder;

    public ChangeTableStateView() {
    }

    @Override
    public void createView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        // Get order of current table
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.PAID);
        states.add(Types.State.CANCELED);
        List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
        currentOrder = null;
        for (Order order : orderList) {
            if( order.getTableId().equals(getCurrentTable().getId()) ) {
                currentOrder = order;
                break;
            }
        }
        
        Label lblTableNumber = new Label(Language.TABLE + " "+ getCurrentTable().getNumber());
        lblTableNumber.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblTableNumber.addStyleName(ValoTheme.LABEL_BOLD);
        lblTableNumber.addStyleName(ValoTheme.LABEL_HUGE);
        lblTableNumber.setWidth("100%");

        VerticalLayout vtcPopupContainer = new VerticalLayout();
        vtcPopupContainer.setSizeFull();

        optionState = new OptionGroup();
        optionState.addItems(Language.EMPTY, Language.WAITING,
                Language.CANCEL_ORDER, Language.PAID, Language.UNPAID);
        optionState.setItemEnabled(Language.PAID, false);
        optionState.setItemEnabled(Language.UNPAID, false);
        optionState.addStyleName("bold FONT_LARGE FONT_TAHOMA TEXT_BLUE");
        optionState.addStyleName(ValoTheme.OPTIONGROUP_LARGE);

        selectOptionState(getCurrentTable().getState()); //@todo: should depend on Order state
        setOptionState();

        vtcPopupContainer.addComponents(lblTableNumber, optionState,
                buildFooter());
        vtcPopupContainer.setExpandRatio(lblTableNumber, 1.0f);
        vtcPopupContainer.setExpandRatio(optionState, 5.0f);
        vtcPopupContainer.setComponentAlignment(optionState, Alignment.MIDDLE_CENTER);

        setContent(vtcPopupContainer);
        // Add click listeners
        btnConfirm.addClickListener(this);
        btnAddOrder.addClickListener(this);
        btnConfirmPaid.addClickListener(this);
    }

    @Override
    public void reloadView() {
        // Nothing to do
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if( event.getComponent() == btnAddOrder ) {
            orderInforView = new OrderInfoView(ViewMode.ORDER_DETAIL_VIEW);
            orderInforView.setParentView(this);
            orderInforView.setCurrentTable(getCurrentTable());
            orderInforView.createView();
            getUI().addWindow(orderInforView);
            close();
        }
        else if( event.getComponent() == btnConfirm ) {
            // Save to db, change the displayed state upon success
            newState = Types.StringToState(optionState.getValue().toString());
            if( newState == State.CANCELED ) {
                ConfirmDialog.show(getUI(), Language.CONFIRM_DELETE_TITLE,
                        Language.CONIFRM_CANCEL_ORDER,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED,
                        new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if( dialog.isConfirmed() ) {
                                    newState = State.EMPTY;
                                    // Reset all current order
                                    if( currentOrder != null ) {
                                        Adapter.changeOrderState(
                                                currentOrder.getId(),
                                                State.CANCELED);
                                        for (String orderDetailId : currentOrder.getOrderDetailIdList()) {
                                            Adapter.changeOrderDetailState(orderDetailId, State.CANCELED);
                                        }
                                        Adapter.changeTableState(
                                                getCurrentTable().getId(),
                                                newState);
                                        NewOrderManager.interruptWaitingOrderThread(currentOrder);
                                        close();
                                        Broadcaster.broadcast(CoffeeshopUI.CANCELED_ORDER+"::"+getCurrentTable().getNumber());
                                    }
                                }
                                else {
                                    System.out
                                            .println("user canceled, do nothing!");
                                }
                            }
                        });
            }
            else {
                Adapter.changeTableState(getCurrentTable().getId(), newState);
                close();
                Broadcaster.broadcast(CoffeeshopUI.CHANGE_TABLE_STATE);
            }
        }
        else if(event.getComponent() == btnConfirmPaid){
            orderInforView = new OrderInfoView(ViewMode.ORDER_SUMMRY);
            orderInforView.setParentView(this);
            orderInforView.setCurrentTable(getCurrentTable());
            orderInforView.createView();
            getUI().addWindow(orderInforView);
            close();
        }
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfirm.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirm.addStyleName("margin-left1");
        btnConfirm.setHeightUndefined();
        btnConfirm.setClickShortcut(KeyCode.ENTER, null);

        btnConfirmPaid = new Button(Language.PAY_BILL);
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirmPaid.addStyleName("margin-right1");
        btnConfirmPaid.setHeightUndefined();

        btnAddOrder = new Button(Language.ADD_ORDER);
        btnAddOrder.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddOrder.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddOrder.addStyleName("margin-right1");
        btnAddOrder.setHeightUndefined();

        // set control buttons state
        setControlBtnState();

        footer.addComponents(btnAddOrder, btnConfirm, btnConfirmPaid);
        footer.setComponentAlignment(btnAddOrder, Alignment.MIDDLE_RIGHT);
        footer.setComponentAlignment(btnConfirm, Alignment.MIDDLE_LEFT);
        footer.setComponentAlignment(btnConfirmPaid, Alignment.MIDDLE_LEFT);
        footer.setSpacing(true);
        return footer;
    }

    private void selectOptionState(Types.State tableState) {
        switch ( tableState ) {
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
            case CANCELED:
                optionState.select(Language.CANCEL_ORDER);
            default:
                break;
        }
    }
    
    /**
     * Set status of element of optionState depend on current order state
     */
    private void setOptionState(){
        optionState.setItemEnabled(Language.EMPTY, false);
        optionState.setItemEnabled(Language.WAITING, false);
        optionState.setItemEnabled(Language.CANCEL_ORDER, false);
        optionState.setItemEnabled(Language.UNPAID, false);

        if(currentOrder == null){
            // No order for this table
            // Enable only WAITING and EMPTY state
            optionState.setItemEnabled(Language.EMPTY, true);
            optionState.setItemEnabled(Language.WAITING, true);
            return;
        }

        switch(currentOrder.getStatus()){
        case PAID:
        case CANCELED:
            optionState.setItemEnabled(Language.EMPTY, true);
            break;
        case WAITING:
            optionState.setItemEnabled(Language.CANCEL_ORDER, true);
            break;
        case UNPAID:
        default:
            break;
        }
    }
    
    /**
     * Set control buttons state
     */
    private void setControlBtnState(){
        btnConfirm.setVisible(false);
        btnAddOrder.setVisible(false);
        btnConfirmPaid.setVisible(false);
        if(currentOrder == null){
            btnConfirm.setVisible(true);
            btnAddOrder.setVisible(true);
            return;
        }
        switch(currentOrder.getStatus()){
        case CANCELED:
        case PAID:
        case WAITING:
            btnAddOrder.setVisible(true);
            btnConfirm.setVisible(true);
            break;
        case UNPAID:
            btnAddOrder.setVisible(true);
            btnConfirmPaid.setVisible(true);
            break;
        default:
            break;
        }
    }
}
