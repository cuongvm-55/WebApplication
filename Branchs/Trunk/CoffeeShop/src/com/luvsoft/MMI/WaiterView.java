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
import com.vaadin.ui.VerticalLayout;

/*
 * We consider that WaiterView is the main view
 */
public class WaiterView extends VerticalLayout implements View {

    private VerticalLayout mainLayout;
    private HorizontalLayout horzTitleContainer;
    private Panel panelContentContainer;
    private Button btnMenu;
    private Label lblStaffName;
    private Button btnEditName;

    private PopupView leftMenuPopup;
    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;
    
    public WaiterView() {
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

        // Add all layouts to the container
        this.addComponents(horzTitleContainer, mainLayout);
        this.addComponent(leftMenuPopup);
        this.setExpandRatio(horzTitleContainer, 1.0f);
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
        horzTitleContainer.setWidth("100%");
        horzTitleContainer.setHeight("50px");

        btnMenu = new Button();
        btnMenu.setIcon(FontAwesome.ALIGN_JUSTIFY);
        btnMenu.setStyleName("icon-only huge");
        btnMenu.setResponsive(true);
        btnMenu.setWidth("40%");
        btnMenu.setHeight("100%");

        lblStaffName = new Label(title);
        lblStaffName.setResponsive(true);
        lblStaffName.setStyleName("bold TEXT_BLUE huge FONT_TAHOMA TEXT_CENTER");
        lblStaffName.setSizeFull();

        btnEditName = new Button(FontAwesome.PENCIL);
        btnMenu.setStyleName("icon-only huge");
        btnMenu.setResponsive(true);
        btnMenu.setWidth("40%");
        btnMenu.setHeight("100%");

        horzTitleContainer.addComponent(btnMenu);
        horzTitleContainer.setExpandRatio(btnMenu, 1.0f);

        horzTitleContainer.addComponent(lblStaffName);
        horzTitleContainer.setExpandRatio(lblStaffName, 2.0f);

        horzTitleContainer.addComponent(btnEditName);
        horzTitleContainer.setExpandRatio(btnEditName, 1.0f);
        horzTitleContainer.setComponentAlignment(btnEditName, Alignment.MIDDLE_RIGHT);
        
        this.addComponents(horzTitleContainer);
    }

    /*
     * Create left menu popup
     */
    private void createLeftMenuPopup() {
        VerticalLayout vtcPopupContent = new VerticalLayout();
        btnWaiterView = new Button(Language.WAITER);
        btnWaiterView.setStyleName("customizationButton");
        btnWaiterView.setWidth("100%");
        
        btnBartenderView = new Button(Language.BARTENDER);
        btnBartenderView.setStyleName("customizationButton");
        btnBartenderView.setWidth("100%");
        
        btnManagementView = new Button(Language.MANAGEMENT);
        btnManagementView.setStyleName("customizationButton");
        btnManagementView.setWidth("100%");

        vtcPopupContent.addComponents(btnWaiterView, btnBartenderView, btnManagementView);

        leftMenuPopup = new PopupView(null, vtcPopupContent);
        leftMenuPopup.setStyleName("leftMenuStyle");
        leftMenuPopup.setPopupVisible(false);
        leftMenuPopup.setHideOnMouseOut(false);

        leftMenuPopup.addPopupVisibilityListener(new PopupVisibilityListener() {
            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if(leftMenuPopup.isPopupVisible()) {
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
            leftMenuPopup.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.WAITER_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(panelContentContainer);
            leftMenuPopup.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.BARTENDER_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new BartenderView());
            leftMenuPopup.setPopupVisible(false);

        } else if(event.getParameters().equals(CoffeeshopUI.MANAGEMENT_VIEW)) {
            mainLayout.removeAllComponents();
            mainLayout.addComponent(new ManagementView());
            leftMenuPopup.setPopupVisible(false);
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
                if(leftMenuPopup.isPopupVisible()) {
                    leftMenuPopup.setPopupVisible(false);
                } else {
                    leftMenuPopup.setPopupVisible(true);
                }
            }
        });

        btnWaiterView.addClickListener(new MenuButtonListener(CoffeeshopUI.WAITER_VIEW));
        btnBartenderView.addClickListener(new MenuButtonListener(CoffeeshopUI.BARTENDER_VIEW));
        btnManagementView.addClickListener(new MenuButtonListener(CoffeeshopUI.MANAGEMENT_VIEW));
    }
}
