package com.luvsoft.MMI.order;

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
import com.vaadin.ui.CssLayout;
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
    private Button btnChangeTable; // to move (part/or whole) order to other table
    private OrderInfoView orderInforView;
    private Types.State newState;
    private Order currentOrder;

    public ChangeTableStateView() {
        currentOrder = null;
    }

    @Override
    public void createView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        addStyleName("close-style radio-style");

        
        Label lblTableNumber = new Label(Language.TABLE + " "+ getCurrentTable().getNumber());
        lblTableNumber.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE FONT_OVERSIZE ");
        lblTableNumber.addStyleName(ValoTheme.LABEL_BOLD);
        lblTableNumber.setWidth("100%");

        VerticalLayout vtcPopupContainer = new VerticalLayout();
        vtcPopupContainer.setSizeFull();

        optionState = new OptionGroup();
        optionState.addItems(Language.EMPTY, Language.WAITING,
                Language.CANCEL_ORDER, Language.PAID, Language.UNPAID);
        optionState.setItemEnabled(Language.PAID, false);
        optionState.setItemEnabled(Language.UNPAID, false);
        optionState.addStyleName("bold FONT_OVER_OVERSIZE");
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
        btnChangeTable.addClickListener(this);
    }

    @Override
    public void reloadView() {
        // Nothing to do
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if( event.getComponent() == btnAddOrder ) {
            orderInforView = new OrderInfoView(currentOrder, ViewMode.ORDER_DETAIL_VIEW);
            orderInforView.setParentView(this);
            orderInforView.setCurrentTable(getCurrentTable());
            orderInforView.setCurrentOrder(currentOrder);
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
                                        Adapter.changeOrderState(currentOrder.getId(), State.CANCELED);
                                        for (String orderDetailId : currentOrder.getOrderDetailIdList()) {
                                            Adapter.changeOrderDetailState(orderDetailId, State.CANCELED);
                                        }

                                        // save staff name who confirm paid (cancel in this case)
                                        if( getSession() != null &&
                                                getSession().getAttribute("user") != null ){
                                            String staffName = getSession().getAttribute("user").toString();
                                            if (!staffName.equals(currentOrder)) {
                                                currentOrder.setStaffNameConfirmPaid(staffName);
                                                Adapter.updateFieldValueOfOrder(currentOrder.getId(), Order.DB_FIELD_NAME_STAFF_NAME_CONFIRM_PAID, currentOrder.getStaffNameConfirmPaid());
                                            }
                                        }

                                        Adapter.changeTableState(getCurrentTable().getId(), newState);
                                        NewOrderManager.interruptWaitingOrderThread(currentOrder);
                                        Broadcaster.broadcast(CoffeeshopUI.CANCELED_ORDER+"::"+getCurrentTable().getId()+"::"+currentOrder.getId());
                                        close();
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
                Broadcaster.broadcast(CoffeeshopUI.CHANGE_TABLE_STATE+"::"+getCurrentTable().getId());
                close();
            }
        }
        else if(event.getComponent() == btnConfirmPaid){
            orderInforView = new OrderInfoView(currentOrder, ViewMode.ORDER_SUMMRY);
            orderInforView.setParentView(this);
            orderInforView.setCurrentTable(getCurrentTable());
            orderInforView.setCurrentOrder(currentOrder);
            orderInforView.createView();
            getUI().addWindow(orderInforView);
            close();
        }
        else if(event.getComponent() == btnChangeTable){
            ChangeTable changeTblWindow = new ChangeTable(currentOrder);
            changeTblWindow.setParentView(this);
            changeTblWindow.setSrcTable(getCurrentTable());
            getUI().addWindow(changeTblWindow);
            close();
         }
    }

    private Component buildFooter() {
        CssLayout footer = new CssLayout();
        footer.setWidth("100%");
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR + " TEXT_CENTER");

        btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_FRIENDLY + " margin-right1 margin-left1 margin-bottom1 BUTTON_GIGANTIC");
        btnConfirm.setClickShortcut(KeyCode.ENTER, null);

        btnConfirmPaid = new Button(Language.PAY_BILL);
        btnConfirmPaid.addStyleName(ValoTheme.BUTTON_PRIMARY + " margin-right1 margin-left1 margin-bottom1 BUTTON_GIGANTIC");

        btnAddOrder = new Button(Language.ADD_ORDER);
        btnAddOrder.addStyleName(ValoTheme.BUTTON_PRIMARY + " margin-right1 margin-left1 margin-bottom1 BUTTON_GIGANTIC");

        btnChangeTable = new Button(Language.CHANGE_TABLE);
        btnChangeTable.addStyleName(ValoTheme.BUTTON_PRIMARY + " margin-right1 margin-left1 margin-bottom1 BUTTON_GIGANTIC");

        // set control buttons state
        setControlBtnState();

        footer.addComponents(btnAddOrder, btnConfirm, btnConfirmPaid, btnChangeTable);
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
        case UNPAID:
            optionState.setItemEnabled(Language.CANCEL_ORDER, true);
            break;
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
        btnChangeTable.setVisible(false);

        // No order in this table
        if(currentOrder == null){
            btnConfirm.setVisible(true);
            btnAddOrder.setVisible(true);
            return;
        }
        btnChangeTable.setVisible(true);
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
            btnConfirm.setVisible(true);
            break;
        default:
            break;
        }
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
    }

    public Order getOrder(Order order) {
        return this.currentOrder;
    }
}
