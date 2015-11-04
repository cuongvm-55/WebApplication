package com.luvsoft.MMI.components;

import com.luvsoft.MMI.OrderInfoView;
import com.luvsoft.MMI.TableListView;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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
    private TableListView parentView;
    private OrderInfoView orderInforView;

    public ChangeTableStatePopup(CoffeeTableElement coffeTableContainer, TableListView tableListView, Table table) {
        parentView = tableListView;
        this.coffeTableContainer = coffeTableContainer;
        this.table = table;
        initView();
    }

    private void initView() {
        setCaption(Language.TABLE + " " + table.getNumber());
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setHeight("45%");
        setWidth("100%");

        orderInforView = new OrderInfoView(table);

        VerticalLayout vtcPopupContainer = new VerticalLayout();
        vtcPopupContainer.setSizeFull();

        optionState = new OptionGroup();
        optionState.addItems(Language.PAID, Language.UNPAID, Language.EMPTY, Language.WAITING);
        selectOptionState(table.getState());

        vtcPopupContainer.addComponents(optionState, buildFooter());

        setContent(vtcPopupContainer);
        // Add click listeners
        btnConfirm.addClickListener(this);
        btnAddOrder.addClickListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getComponent() == btnAddOrder) {
            close();
            // CoffeeshopUI.navigator.navigateTo(CoffeeshopUI.MAIN_VIEW + "/" + CoffeeshopUI.ORDER_INFO_VIEW);
            parentView.getUI().addWindow(orderInforView);
        } else if(event.getComponent() == btnConfirm){
            close();
            Types.State tableState = StringToTableState(optionState.getValue().toString());
            coffeTableContainer.changeTableState(tableState, 0);
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

    private Types.State StringToTableState(String str) {
        Types.State state = State.EMPTY;

        if(str.equals(Language.PAID)) {
            state = State.PAID;
        } else if(str.equals(Language.UNPAID)) {
            state = State.UNPAID;
        } else if(str.equals(Language.WAITING)) {
            state = State.WAITING;
        }

        return state;
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
