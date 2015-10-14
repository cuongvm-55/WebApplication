package com.luvsoft.MMI;

import javax.servlet.annotation.WebServlet;

import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.components.CoffeeTableElement.TABLE_STATE;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.Language.LANGUAGE;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("coffeeshop")
public class CoffeeshopUI extends UI implements ClickListener{

    final VerticalLayout layout = new VerticalLayout();
    
    private HorizontalLayout horzTitleContainer;
    private Button btnMenu;
    private Label lblStaffName;
    private Button btnEditName;

    private PopupView leftMenuPopup;
    private Button btnWaiterView;
    private Button btnBartenderView;
    private Button btnManagementView;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CoffeeshopUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        // Set Language is VietNamese
        Language lang = new Language();
        lang.setLanguage(LANGUAGE.VIETNAMESE);

        layout.setSizeFull();
        setContent(layout);

        createTitleComponent("Lê Thị Kim Chi");

        // Content Panel
        Panel panelContentContainer = new Panel();
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

        createLeftMenuPopup();

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

        // Add all layouts to the container
        layout.addComponents(horzTitleContainer, panelContentContainer);
        layout.addComponent(leftMenuPopup);
        layout.setExpandRatio(horzTitleContainer, 1.0f);
        layout.setExpandRatio(panelContentContainer, 10.0f);

        // Add event listener
        btnMenu.addClickListener(this);
    }

    void createTitleComponent(String title)
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
        
        layout.addComponents(horzTitleContainer);
    }

    void createLeftMenuPopup() {
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
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton() == btnMenu) {
            // Show left menu popup
            if(leftMenuPopup.isPopupVisible()) {
                leftMenuPopup.setPopupVisible(false);
            } else {
                leftMenuPopup.setPopupVisible(true);
            }
        } else if(event.getButton() == btnWaiterView) {
            // TODO
        } else if(event.getButton() == btnBartenderView) {
            // TODO
        } else if(event.getButton() == btnManagementView) {
            // TODO
        }
        
    }
}