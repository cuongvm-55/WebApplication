package com.luvsoft.MMI.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CustomizationTreeElement extends VerticalLayout implements ClickListener{

    /**
     * @author datnq.55
     * Function is used to create an element for a tree
     * 
     * Tip: CustomizationTreeElement yourElement 
     *        = new CustomizationTreeElement(yourGridContentLayout, yourTitleOfThisElement)
     */

    private Button btnExpandElement;
    private HorizontalLayout horzFloorTitleContainer;
    private Label lblFloorName;
    private Component btnAddTable;
    private Component content;

    public CustomizationTreeElement(Component content, String title, boolean hasAddTableBtn) {
        super();
        initView(content, title, hasAddTableBtn);
    }

    public CustomizationTreeElement(Component content, String title) {
        super();
        initView(content, title, true);
    }

    public void initView(Component content, String title, boolean hasAddTableBtn) {
        this.content = content;
        this.setStyleName("elementContainerStyle");

        this.horzFloorTitleContainer = new HorizontalLayout();
        this.horzFloorTitleContainer.setStyleName("card");
        this.horzFloorTitleContainer.setResponsive(true);
        this.horzFloorTitleContainer.setWidth("100%");
        this.horzFloorTitleContainer.setStyleName("BACKGROUND_BLUE TEXT_WHITE");

        this.btnExpandElement = new Button();
        this.btnExpandElement.setStyleName("icon-only primary TEXT_WHITE");
        this.btnExpandElement.setIcon(FontAwesome.CHEVRON_DOWN);
        this.btnExpandElement.setHeight("100%");

        this.lblFloorName = new Label(title);
        this.lblFloorName.setStyleName("bold FONT_OVERSIZE FONT_TAHOMA TEXT_CENTER");
        this.lblFloorName.setWidth("100%");

        if(hasAddTableBtn == true) {
            this.btnAddTable = new Button();
            this.btnAddTable.setHeight("100%");
            btnAddTable.setIcon(FontAwesome.PLUS_CIRCLE);
            btnAddTable.setStyleName("icon-only primary TEXT_WHITE");
        } else {
            this.btnAddTable = new Label("");
            this.btnAddTable.setHeight("100%");
        }

        // Add label and button to element container
        this.horzFloorTitleContainer.addComponents(this.btnExpandElement, this.lblFloorName, this.btnAddTable);
        this.horzFloorTitleContainer.setExpandRatio(this.btnExpandElement, 1.0f);
        this.horzFloorTitleContainer.setExpandRatio(this.lblFloorName, 4.0f);
        this.horzFloorTitleContainer.setExpandRatio(this.btnAddTable, 1.0f);
        this.horzFloorTitleContainer.setComponentAlignment(btnAddTable, Alignment.MIDDLE_RIGHT);

        this.addComponents(this.horzFloorTitleContainer, content);

        // Add event listener
        this.btnExpandElement.addClickListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton() == this.btnExpandElement) {
            if(this.content.isVisible()) {
                this.btnExpandElement.setIcon(FontAwesome.CHEVRON_RIGHT);
                this.content.setVisible(false);
            } else {
                this.btnExpandElement.setIcon(FontAwesome.CHEVRON_DOWN);
                this.content.setVisible(true);
            }
        }
        
    }

}
