package com.luvsoft.MMI.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CustomizationTreeElement extends VerticalLayout{

    /**
     * @author datnq.55
     * Function is used to create an element for a tree
     * 
     * Tip: CustomizationTreeElement yourElement 
     *        = new CustomizationTreeElement(yourGridContentLayout, yourTitleOfThisElement)
     */

    private Button btnExpandElement;
    private CssLayout horzFloorTitleContainer;
    private Label lblFloorName;
    private Component addComponent;
    private Component content;

    public CustomizationTreeElement(Component content, String title, Component additionalComponent) {
        super();
        initView(content, title, additionalComponent);
    }

    public void initView(Component content, String title, Component additionalComponent) {
        this.content = content;
        this.addStyleName("elementContainerStyle");

        this.horzFloorTitleContainer = new CssLayout();
        this.horzFloorTitleContainer.setWidth("100%");
        this.horzFloorTitleContainer.addStyleName("BACKGROUND_BLUE TEXT_WHITE " + ValoTheme.LAYOUT_COMPONENT_GROUP);

        this.btnExpandElement = new Button();
        this.btnExpandElement.addStyleName("icon-only primary TEXT_WHITE " + ValoTheme.BUTTON_LARGE);
        this.btnExpandElement.setIcon(FontAwesome.CHEVRON_DOWN);
        this.btnExpandElement.setWidth("15%");

        this.lblFloorName = new Label(title);
        this.lblFloorName.addStyleName("FONT_TAHOMA TEXT_CENTER TEXT_WHITE " +ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_HUGE);
        this.lblFloorName.setWidth("70%");

        if( additionalComponent == null ){
            addComponent = new Label("");
            addComponent.setWidth("15%");
        }
        else{
            addComponent = additionalComponent;
            addComponent.setWidth("15%");
        }

        // Add label and button to element container
        this.horzFloorTitleContainer.addComponents(btnExpandElement, lblFloorName, addComponent);
        this.addComponents(this.horzFloorTitleContainer, content);
    }

    public void setContentExpand(){
        this.btnExpandElement.setIcon(FontAwesome.CHEVRON_DOWN);
        this.content.setVisible(true);
        this.btnExpandElement.setImmediate(true);
    }

    public void setContentCollapse(){
        this.btnExpandElement.setIcon(FontAwesome.CHEVRON_RIGHT);
        this.content.setVisible(false);
        this.btnExpandElement.setImmediate(true);
    }

    public Button getBtnExpandElement() {
        return btnExpandElement;
    }

    public void setBtnExpandElement(Button btnExpandElement) {
        this.btnExpandElement = btnExpandElement;
    }

    public Component getContent() {
        return content;
    }

    public void setContent(Component content) {
        removeComponent(this.content);
        this.content = content;
        addComponent(this.content);
    }

}
