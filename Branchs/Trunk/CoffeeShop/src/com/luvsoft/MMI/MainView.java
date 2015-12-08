package com.luvsoft.MMI;

import com.luvsoft.MMI.management.LoginForm;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.MenuButtonListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MainView extends VerticalLayout implements View, ViewInterface {
    /*
     * UIs
     */
    private VerticalLayout mainLayout;
    private HorizontalLayout horzTitleContainer;
    private Button btnMenu;
    private Label lblStaffName;
    private Button btnEditName;

    private PopupView popLeftMenu;
    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;
    private OrderListView orderListView;
    private TableListView tableListView;

    public MainView() {
        super();
        orderListView = new OrderListView();
        tableListView = new TableListView();
        tableListView.setParentView(this);
        lblStaffName = new Label();
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

    }

    /*
     * Initialize main view which includes title bar and left menu
     */
    @Override
    public void createView() {
        this.setSizeFull();

        createTitleComponent();

        createLeftMenuPopup();

        // add table list to MainView
        buildTableList();

        // Add all layouts to the container
        this.addComponents(horzTitleContainer, mainLayout, popLeftMenu);
        this.setExpandRatio(horzTitleContainer, 2.0f);
        this.setExpandRatio(mainLayout, 10.0f);

        // Add event listener
        this.addClickListener();
    }

    private void buildTableList(){
        // The first screen should displays table list
        TableListView tableListView = new TableListView();
        tableListView.setParentView(this);
        tableListView.createView();
        mainLayout.addComponent(tableListView);
    }
    @Override
    public void reloadView() {
        // Nothing to do
        
    }

    /*
     * Create a title bar
     */
    private void createTitleComponent()
    {
        // Title container
        horzTitleContainer = new HorizontalLayout();
        horzTitleContainer.setResponsive(true);
        horzTitleContainer.setSizeFull();

        btnMenu = new Button();
        btnMenu.setIcon(FontAwesome.LIST);
        btnMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnMenu.addStyleName(ValoTheme.BUTTON_LARGE);
        // btnMenu.addStyleName("valo-menu-toggle");
        btnMenu.setResponsive(true);
        btnMenu.setSizeFull();

        String staffName = getSession().getAttribute("user") != null ? getSession().getAttribute("user").toString() : "";
        lblStaffName.setValue(staffName);
        lblStaffName.setResponsive(true);
        lblStaffName.setStyleName("bold TEXT_BLUE FONT_OVER_OVERSIZE FONT_TAHOMA TEXT_CENTER");
        lblStaffName.setSizeUndefined();

        btnEditName = new Button();
        btnEditName.setIcon(FontAwesome.PENCIL);
        btnEditName.setStyleName("icon-only huge");
        btnEditName.setResponsive(true);
        btnEditName.setSizeFull();

        horzTitleContainer.addComponents(btnMenu, lblStaffName, btnEditName);
        horzTitleContainer.setExpandRatio(btnMenu, 0.7f);

        horzTitleContainer.setExpandRatio(lblStaffName, 2.6f);
        horzTitleContainer.setComponentAlignment(lblStaffName, Alignment.MIDDLE_CENTER);

        horzTitleContainer.setExpandRatio(btnEditName, 0.7f);
        horzTitleContainer.setComponentAlignment(btnEditName, Alignment.MIDDLE_RIGHT);
        
        this.addComponents(horzTitleContainer);
    }

    /*
     * Create left menu popup
     */
    private void createLeftMenuPopup() {
        VerticalLayout vtcPopupContent = new VerticalLayout();
        btnWaiterView = new Button(Language.WAITER);
        btnWaiterView.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");

        btnBartenderView = new Button(Language.BARTENDER);
        btnBartenderView.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        
        btnManagementView = new Button(Language.MANAGEMENT);
        btnManagementView.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");

        vtcPopupContent.addComponents(btnWaiterView, btnBartenderView, btnManagementView);

        popLeftMenu = new PopupView(null, vtcPopupContent);
        popLeftMenu.setStyleName("leftMenuStyle");
        popLeftMenu.setPopupVisible(false);
        popLeftMenu.setHideOnMouseOut(false);

        popLeftMenu.addPopupVisibilityListener(new PopupVisibilityListener() {
            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if(popLeftMenu.isPopupVisible()) {
                    mainLayout.setEnabled(false);
                } else {
                    mainLayout.setEnabled(true);
                }
                
            }
        });
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
        this.removeAllComponents();
        createView();
        if(event.getParameters() == null || event.getParameters().isEmpty()) {
            mainLayout.removeAllComponents();
            TableListView tableListView = new TableListView();
            tableListView.setParentView(this);
            tableListView.createView();
            mainLayout.addComponent(tableListView);
        } else if(event.getParameters().equals(CoffeeshopUI.TABLE_LIST_VIEW)) {
            mainLayout.removeAllComponents();
            tableListView.setParentView(this);
            tableListView.createView();
            mainLayout.addComponent(tableListView);
            popLeftMenu.setPopupVisible(false);
        } else if(event.getParameters().equals(CoffeeshopUI.ORDER_LIST_VIEW)) {
            mainLayout.removeAllComponents();
            orderListView.createView();
            mainLayout.addComponent(orderListView);
            popLeftMenu.setPopupVisible(false);
        } else if(event.getParameters().equals(CoffeeshopUI.MANAGEMENT_VIEW)) {
            mainLayout.removeAllComponents();
            getUI().addWindow(new LoginForm(this));
            popLeftMenu.setPopupVisible(false);
        }
    }

    /*
     * Handle all button click events
     */
    private void addClickListener() {
        btnMenu.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                // Show left menu popup
                if(popLeftMenu.isPopupVisible()) {
                    popLeftMenu.setPopupVisible(false);
                } else {
                    popLeftMenu.setPopupVisible(true);
                }
            }
        });

        btnWaiterView.addClickListener(new MenuButtonListener(CoffeeshopUI.TABLE_LIST_VIEW));
        btnBartenderView.addClickListener(new MenuButtonListener(CoffeeshopUI.ORDER_LIST_VIEW));
        btnManagementView.addClickListener(new MenuButtonListener(CoffeeshopUI.MANAGEMENT_VIEW));
    }

    public VerticalLayout getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(VerticalLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    public String getStaffName() {
        return lblStaffName.getValue();
    }

    public void setStaffName(String staffName) {
        this.lblStaffName.setValue(staffName);
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
}
