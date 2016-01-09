package com.luvsoft.MMI;

import com.luvsoft.MMI.components.ValoMenuLayout;
import com.luvsoft.MMI.management.LoginForm;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.MenuButtonListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MainView extends ValoMenuLayout implements View, ViewInterface {
    /*
     * UIs
     */
    private String strStaffName;

    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;
    private OrderListView orderListView;
    private TableListView tableListView;
    private MenuBar mnbSettings;

    private CssLayout menu;
    private Label title;
    private boolean isViewChangedByClickingButton;

    public MainView() {
        super();
        orderListView = new OrderListView();
        tableListView = new TableListView();
        tableListView.setParentView(this);
        setViewChangedByClickingButton(true);

        // init cached data
        Adapter.initCachedData();
    }

    public void setMenu(CssLayout menu) {
        this.menu = menu;
        if(this.menu == null) {
            this.menu = new CssLayout();
        }
    }

    /*
     * Initialize main view which includes title bar and left menu
     */
    @Override
    public void createView() {
        this.setWidth("100%");

        addMenu(buildMenu());

        // Add event listener
        this.addClickListener();
    }

    private CssLayout buildMenu() {
        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName("valo-menu-title");
        menu.addComponent(top);

        final Button showMenu = new Button("Menu", new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                if (menu.getStyleName().contains("valo-menu-visible")) {
                    menu.removeStyleName("valo-menu-visible");
                } else {
                    menu.addStyleName("valo-menu-visible");
                }
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        menu.addComponent(showMenu);

        title = new Label("<h3>Huyền Coffee</h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        mnbSettings = new MenuBar();
        mnbSettings.addStyleName("user-menu");
        menu.addComponent(mnbSettings);

        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        btnWaiterView = new Button(Language.WAITER);
        btnWaiterView.setPrimaryStyleName("valo-menu-item");
        btnWaiterView.setIcon(FontAwesome.USERS);
        btnWaiterView.addStyleName(ValoTheme.LABEL_HUGE);

        btnBartenderView = new Button(Language.BARTENDER);
        btnBartenderView.setPrimaryStyleName("valo-menu-item");
        btnBartenderView.setIcon(FontAwesome.CUTLERY);

        btnManagementView = new Button(Language.MANAGEMENT);
        btnManagementView.setPrimaryStyleName("valo-menu-item");
        btnManagementView.setIcon(FontAwesome.KEY);

        menuItemsLayout.addComponents(btnWaiterView, btnBartenderView, btnManagementView);

        return menu;
    }

    @Override
    public void reloadView() {
        // Nothing to do
    }

    /*
     * This event is used to listen navigation event and set content content layout as expected 
     * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
     */
    @Override
    public void enter(ViewChangeEvent event) {
        // Check login session here
        if( getSession().getAttribute("user") == null ){
            UI.getCurrent().getNavigator().navigateTo(CoffeeshopUI.LOGIN_VIEW);
            return;
        }

        // we're in Operator mode
        Adapter.setSUPMode(false);
        if(strStaffName == null) {
            strStaffName = getSession().getAttribute("user") != null ? getSession().getAttribute("user").toString() : "";
            System.out.println("STAFF NAME .......... " + strStaffName);
            mnbSettings.addItem(strStaffName, new ThemeResource("../tests-valo/img/profile-pic-300px.jpg"), null);
        }

        if(isViewChangedByClickingButton == true) {
            if((event.getParameters() == null || event.getParameters().isEmpty())) {
                getContentContainer().removeAllComponents();
                tableListView.createView();
                getContentContainer().addComponent(tableListView);
                menu.removeStyleName("valo-menu-visible");
                isViewChangedByClickingButton = false;
            } else if(event.getParameters().equals(CoffeeshopUI.TABLE_LIST_VIEW)) {
                getContentContainer().removeAllComponents();
                tableListView.setParentView(this);
                tableListView.createView();
                getContentContainer().addComponent(tableListView);
                menu.removeStyleName("valo-menu-visible");
                isViewChangedByClickingButton = false;
            } else if(event.getParameters().equals(CoffeeshopUI.ORDER_LIST_VIEW)) {
                getContentContainer().removeAllComponents();
                orderListView.createView();
                getContentContainer().addComponent(orderListView);
                menu.removeStyleName("valo-menu-visible");
                isViewChangedByClickingButton = false;
            } else if(event.getParameters().equals(CoffeeshopUI.MANAGEMENT_VIEW)) {
                getContentContainer().removeAllComponents();
                getUI().addWindow(new LoginForm(this));
                menu.removeStyleName("valo-menu-visible");
                isViewChangedByClickingButton = false;
            }
        }
    }

    /*
     * Handle all button click events
     */
    private void addClickListener() {
        btnWaiterView.addClickListener(new MenuButtonListener(CoffeeshopUI.TABLE_LIST_VIEW, this));
        btnBartenderView.addClickListener(new MenuButtonListener(CoffeeshopUI.ORDER_LIST_VIEW, this));
        btnManagementView.addClickListener(new MenuButtonListener(CoffeeshopUI.MANAGEMENT_VIEW, this));
    }

    public String getStaffName() {
        return strStaffName;
    }

    public void setStaffName(String staffName) {
        this.strStaffName = staffName;
    }

    @Override
    public ViewInterface getParentView() {
        // Nothing to do
        return null;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        // Nothing to do
    }

    public OrderListView getOrderListView() {
        return orderListView;
    }

    public void setOrderListView(OrderListView orderListView) {
        this.orderListView = orderListView;
    }

    public TableListView getTableListView() {
        return tableListView;
    }

    public void setTableListView(TableListView tableListView) {
        this.tableListView = tableListView;
    }

    public boolean isViewChangedByClickingButton() {
        return isViewChangedByClickingButton;
    }

    public void setViewChangedByClickingButton(boolean isViewChangedByClickingButton) {
        this.isViewChangedByClickingButton = isViewChangedByClickingButton;
    }
}
