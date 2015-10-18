package com.luvsoft.MMI;

import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.components.CoffeeTableElement.TABLE_STATE;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.MenuButtonListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/*
 * We consider that WaiterView is the main view
 */
public class TableListView extends VerticalLayout implements View {

    private VerticalLayout mainLayout;
    private HorizontalLayout horzTitleContainer;
    private Panel panelContentContainer;
    private Button btnMenu;
    private Label lblStaffName;
    private Button btnEditName;

    private PopupView popLeftMenu;
    private PopupView popEditName;
    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;
    
    public TableListView() {
        super();
        initView();
    }

    /*
     * Initialize main view which includes title bar and left menu
     */
    private void initView() {
        this.setSizeFull();

        createTitleComponent("Lê Thị Kim Chi");

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        // Content Panel
        panelContentContainer = new Panel();
        panelContentContainer.setSizeFull();

        VerticalLayout vtcContentContainer = new VerticalLayout();

        for (int j = 0; j < 5; j++) {
            GridLayout gridElementContent = new GridLayout();
            gridElementContent.setRows(3);
            gridElementContent.setColumns(3);
            gridElementContent.setWidth("100%");

            // Add components to grid layout
            for (int i = 0; i < 6; i++) {
                CoffeeTableElement tableElement = new CoffeeTableElement(TABLE_STATE.STATE_WAITING, i, i);

                gridElementContent.addComponent(tableElement);
            }

            CustomizationTreeElement treeElement = new CustomizationTreeElement(gridElementContent, Language.FLOOR + " " + (j+1));
            vtcContentContainer.addComponent(treeElement);
            panelContentContainer.setContent(vtcContentContainer);
        }

        mainLayout.addComponent(panelContentContainer);

        createLeftMenuPopup();
        createEditNamePopup();

        // Add all layouts to the container
        this.addComponents(horzTitleContainer, mainLayout, popLeftMenu, popEditName);
        this.setExpandRatio(horzTitleContainer, 1.2f);
        this.setExpandRatio(mainLayout, 10.0f);

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
                    panelContentContainer.setEnabled(false);
                } else {
                    panelContentContainer.setEnabled(true);
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
            mainLayout.addComponent(panelContentContainer);
            popLeftMenu.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.TABLE_LIST_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(panelContentContainer);
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

        Button btnConfirm = new Button("Xác Nhận");
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
                    panelContentContainer.setEnabled(false);
                } else {
                    panelContentContainer.setEnabled(true);
                }
                
            }
        });

        btnConfirm.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                lblStaffName.setValue(txtName.getValue());
            }
        });
    }
}
