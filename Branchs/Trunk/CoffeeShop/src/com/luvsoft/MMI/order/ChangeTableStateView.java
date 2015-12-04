package com.luvsoft.MMI.order;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.threads.NewOrderManager;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
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
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ChangeTableStateView extends AbstractOrderView implements
        ClickListener {

    // private PopupView popupChangeTableState;
    private OptionGroup optionState;
    private Button btnAddOrder;
    private Button btnConfirm;
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

        Label lblTableNumber = new Label(Language.TABLE + " "
                + getCurrentTable().getNumber());
        lblTableNumber
                .addStyleName("bold FONT_OVER_OVERSIZE FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblTableNumber.setWidth("100%");

        VerticalLayout vtcPopupContainer = new VerticalLayout();
        vtcPopupContainer.setSizeFull();

        optionState = new OptionGroup();
        optionState.addItems(Language.EMPTY, Language.WAITING,
                Language.CANCEL_ORDER, Language.PAID, Language.UNPAID);
        optionState.setItemEnabled(Language.PAID, false);
        optionState.setItemEnabled(Language.UNPAID, false);
        optionState.addStyleName("bold FONT_OVERSIZE FONT_TAHOMA TEXT_BLUE");
        optionState.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        optionState.addStyleName(ValoTheme.OPTIONGROUP_LARGE);

        selectOptionState(getCurrentTable().getState());

        vtcPopupContainer.addComponents(lblTableNumber, optionState,
                buildFooter());
        vtcPopupContainer.setExpandRatio(lblTableNumber, 2.0f);
        vtcPopupContainer.setExpandRatio(optionState, 5.0f);
        vtcPopupContainer.setComponentAlignment(optionState, Alignment.MIDDLE_CENTER);

        setContent(vtcPopupContainer);
        // Add click listeners
        btnConfirm.addClickListener(this);
        btnAddOrder.addClickListener(this);
    }

    @Override
    public void reloadView() {
        // Nothing to do
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if( event.getComponent() == btnAddOrder ) {
            orderInforView = new OrderInfoView();
            orderInforView.setParentView(this);
            orderInforView.setCurrentTable(getCurrentTable());
            orderInforView.createView();
            getUI().addWindow(orderInforView);
            close();
        }
        else if( event.getComponent() == btnConfirm ) {
            // Save to db, change the displayed state upon success
            newState = Types.StringToState(optionState.getValue().toString());

            List<Order> orderList = Adapter.getOrderListIgnoreStates(
                    Types.State.PAID, Types.State.CANCELED, null, null);
            currentOrder = null;
            for (Order order : orderList) {
                if( order.getTableId().equals(getCurrentTable().getId()) ) {
                    currentOrder = order;
                    break;
                }
            }

            // Cannot change from UNPAID to EMPTY if current order is not PAID
            // Cannot change from WAITING to EMPTY if current order is not PAID
            if( (getCurrentTable().getState() == Types.State.UNPAID || getCurrentTable()
                    .getState() == Types.State.WAITING)
                    && newState == Types.State.EMPTY
                    && currentOrder != null
                    && currentOrder.getStatus() != Types.State.PAID ) {
                System.out
                        .println("Cannot change from UNPAID or WAITING to EMPTY when current order's not PAID");
                // notify message
                Notification notify = new Notification("<b>Error</b>", "<i>"
                        + Language.CANNOT_CHANGE_TABLE_STATE + "</i>",
                        Notification.Type.TRAY_NOTIFICATION, true);
                notify.setPosition(Position.BOTTOM_RIGHT);
                notify.show(Page.getCurrent());
            }
            else if( newState == State.CANCELED ) {
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
                                    } else if( getCurrentTable().getState() == Types.State.PAID ) {
                                        Notification notify = new Notification(
                                                "<b>Error</b>",
                                                "<i>"
                                                        + Language.CANNOT_CANCEL_ORDER
                                                        + "</i>",
                                                Notification.Type.TRAY_NOTIFICATION,
                                                true);
                                        notify.setPosition(Position.BOTTOM_RIGHT);
                                        notify.show(Page.getCurrent());
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
                Broadcaster.broadcast(CoffeeshopUI.CHANGE_TABLE_STATE);
                close();
            }
        }
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName("BUTTON_GIGANTIC");
        btnConfirm.addStyleName("customizationButton");
        btnConfirm.setHeightUndefined();
        btnConfirm.setClickShortcut(KeyCode.ENTER, null);

        btnAddOrder = new Button(Language.ADD_ORDER);
        btnAddOrder.addStyleName("BUTTON_GIGANTIC");
        btnAddOrder.addStyleName("customizationButton");
        btnAddOrder.setHeightUndefined();

        footer.addComponents(btnAddOrder, btnConfirm);
        footer.setComponentAlignment(btnAddOrder, Alignment.MIDDLE_RIGHT);
        footer.setComponentAlignment(btnConfirm, Alignment.MIDDLE_LEFT);
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
}
