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
    private Component addComponent;
    private Component content;

    public CustomizationTreeElement(Component content, String title, Component additionalComponent) {
        super();
        initView(content, title, additionalComponent);
    }

    public void initView(Component content, String title, Component additionalComponent) {
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

        if( additionalComponent == null ){
            addComponent = new Label("");
            addComponent.setHeight("100%");
        }
        else{
            addComponent = additionalComponent;
        }

        // Add label and button to element container
        this.horzFloorTitleContainer.addComponents(this.btnExpandElement, this.lblFloorName, addComponent);
        this.horzFloorTitleContainer.setExpandRatio(this.btnExpandElement, 1.0f);
        this.horzFloorTitleContainer.setExpandRatio(this.lblFloorName, 4.0f);
        this.horzFloorTitleContainer.setExpandRatio(addComponent, 1.0f);
        this.horzFloorTitleContainer.setComponentAlignment(addComponent, Alignment.MIDDLE_RIGHT);

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
