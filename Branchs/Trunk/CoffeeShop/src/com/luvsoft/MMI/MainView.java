package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.MenuButtonListener;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Table;
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
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainView extends VerticalLayout implements View {
    public static MainView instance;
    private List<Order> orderList; // Current order list
    private Table currentTable; // Current selected table (use for OrderInfoView)

    /*
     * UIs
     */
    private VerticalLayout mainLayout;
    private HorizontalLayout horzTitleContainer;
    private Button btnMenu;
    private Label lblStaffName;
    private Button btnEditName;

    private PopupView popLeftMenu;
    private PopupView popEditName;
    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;
    
    private PopupView orderInfoPopup;
    public MainView() {
        super();
        initView();

        orderList = new ArrayList<Order>();
        currentTable = new Table();
    }

    public static MainView getInstance(){
        if( instance == null ){
            instance = new MainView();
        }
        return instance;
    }

    /*
     * Initialize main view which includes title bar and left menu
     */
    private void initView() {
        this.setSizeFull();

        createTitleComponent("Lê Thị Kim Chi");

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        // The first screen should displays table list
        mainLayout.addComponent(new TableListView());

        createLeftMenuPopup();
        createEditNamePopup();

        // Add all layouts to the container
        this.addComponents(horzTitleContainer, mainLayout, popLeftMenu, popEditName);
        this.setExpandRatio(horzTitleContainer, 1.2f);
        this.setExpandRatio(mainLayout, 10.0f);
        //this.setComponentAlignment(orderInfoPopup, Alignment.TOP_CENTER);

        // Add event listener
        this.addClickListener();
    }

    /*
     * Create a title bar
     */
    private void createTitleComponent(String title)
    {
        // Title container
        horzTitleContainer = new HorizontalLayout();
        horzTitleContainer.setResponsive(true);
        horzTitleContainer.setSizeFull();

        btnMenu = new Button();
        btnMenu.setIcon(FontAwesome.ALIGN_JUSTIFY);
        btnMenu.setStyleName("icon-only huge");
        btnMenu.setResponsive(true);
        btnMenu.setSizeFull();

        lblStaffName = new Label(title);
        lblStaffName.setResponsive(true);
        lblStaffName.setStyleName("bold TEXT_BLUE FONT_OVER_OVERSIZE FONT_TAHOMA TEXT_CENTER");
        lblStaffName.setSizeUndefined();

        btnEditName = new Button();
        btnEditName.setIcon(FontAwesome.PENCIL);
        btnEditName.setStyleName("icon-only huge");
        btnEditName.setResponsive(true);
        btnEditName.setSizeFull();

        horzTitleContainer.addComponent(btnMenu);
        horzTitleContainer.setExpandRatio(btnMenu, 0.7f);

        horzTitleContainer.addComponent(lblStaffName);
        horzTitleContainer.setExpandRatio(lblStaffName, 2.6f);
        horzTitleContainer.setComponentAlignment(lblStaffName, Alignment.MIDDLE_CENTER);

        horzTitleContainer.addComponent(btnEditName);
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
        if(event.getParameters() == null || event.getParameters().isEmpty()) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new TableListView());
            popLeftMenu.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.TABLE_LIST_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new TableListView());
            popLeftMenu.setPopupVisible(false);
        } else if(event.getParameters().equals(CoffeeshopUI.ORDER_LIST_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new OrderListView());
            popLeftMenu.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.MANAGEMENT_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new ManagementView());
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

        btnEditName.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // Show edit name popup
                if(popEditName.isPopupVisible()) {
                    popEditName.setPopupVisible(false);
                } else {
                    popEditName.setPopupVisible(true);
                }
            }
        });

        btnWaiterView.addClickListener(new MenuButtonListener(CoffeeshopUI.TABLE_LIST_VIEW));
        btnBartenderView.addClickListener(new MenuButtonListener(CoffeeshopUI.ORDER_LIST_VIEW));
        btnManagementView.addClickListener(new MenuButtonListener(CoffeeshopUI.MANAGEMENT_VIEW));
    }

    private void createEditNamePopup() {
        VerticalLayout popupContent = new VerticalLayout();
        popupContent.setHeightUndefined();

        TextField txtName = new TextField();
        txtName.setValue(lblStaffName.getValue());
        txtName.setWidth("100%");
        txtName.setStyleName("huge");

        Button btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.setStyleName("huge customizationButton");

        popupContent.addComponents(txtName, btnConfirm);
        popupContent.setComponentAlignment(btnConfirm, Alignment.MIDDLE_CENTER);

        popEditName = new PopupView(null, popupContent);
        popEditName.setStyleName("popupStyle");
        popEditName.setPopupVisible(false);
        popEditName.setHideOnMouseOut(false);

        popEditName.addPopupVisibilityListener(new PopupVisibilityListener() {
            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if(popEditName.isPopupVisible()) {
                    mainLayout.setEnabled(false);
                } else {
                    mainLayout.setEnabled(true);
                }
                
            }
        });

        btnConfirm.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                lblStaffName.setValue(txtName.getValue());
                popEditName.setPopupVisible(false);
            }
        });
    }

    /*
     * At a particular moment, there's only 1 Order corresponding to a Table
     * In DB, we should have two types of Orders:
     *  1. Saved orders - could not be modified anymore, that's all complete orders
     *     - Order state must be "COMPLETE"
     *  2. Current orders - user are working with these orders, add food, confirm paid,...
     *     - Order state < "COMPLETE"
     * - When a table changes state from EMPTY-->WAITING, a temporary Order will be created in MMI layer
     * - When user add food to a temporary order, it will be saved to DB as a new "current orders" for a table
     *      After that, we consider temporary as a "current orders" --> set state ...
     * - When a table changes state from other states to EMPTY, the associated Order in MMI layer will be deleted,
     *      "current orders" now is considered as a "saved orders" --> set state "COMPLETE"
     * 
     * This function loads all "current orders"
     */
    public void loadOrderList(){
        orderList = Adapter.getCurrentOrderList();
    }
    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Table getCurrentTable() {
        return currentTable;
    }

    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }

    public VerticalLayout getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(VerticalLayout mainLayout) {
        this.mainLayout = mainLayout;
    }
}
