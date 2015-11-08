package com.luvsoft.MMI.Order;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.data.Property.ValueChangeListener;

/**
 * 
 * @author datnq.55
 *
 */

@SuppressWarnings("serial")
public class AddFood extends Window {

    private List<Category> listOfCategory = new ArrayList<Category>();

    public AddFood() {
        super();
        // Get all foods from database
        listOfCategory = Adapter.retrieveCategoryList();
        initView();
    }

    private void initView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        Label lblTableNumber = new Label(Language.TABLE + " ");
        lblTableNumber
                .addStyleName("bold FONT_OVERSIZE FONT_TAHOMA TEXT_CENTER BACKGROUND_BLUE TEXT_WHITE");
        lblTableNumber.setWidth("100%");

        Component content = buildContent();
        container.addComponents(lblTableNumber, content, buildFooter());
        container.setExpandRatio(lblTableNumber, 1.2f);
        container.setExpandRatio(content, 10.0f);

        setContent(container);
    }

    public Component buildContent() {
        Panel wrapPanel = new Panel();
        wrapPanel.setSizeFull();

        VerticalLayout content = new VerticalLayout();

        for (Category category : listOfCategory) {
            CustomizationTreeElement treeElement = new CustomizationTreeElement(
                    buildContentElement(category), category.getName(), false);
            content.addComponent(treeElement);
            content.setComponentAlignment(treeElement, Alignment.TOP_CENTER);
        }

        wrapPanel.setContent(content);
        return wrapPanel;
    }

    private Component buildContentElement(Category category) {
        VerticalLayout vtcElementContainer = new VerticalLayout();

        for (Food food : category.getListOfFoodByCategory()) {
            vtcElementContainer.addComponents(buildChildElementContainer(food));
        }

        return vtcElementContainer;
    }

    private Component buildChildElementContainer(Food food) {
        HorizontalLayout hrzChildElementContainer = new HorizontalLayout();
        hrzChildElementContainer.setSizeFull();
        hrzChildElementContainer.addStyleName(ValoTheme.LAYOUT_CARD);

        Label foodName = new Label();
        foodName.addStyleName(ValoTheme.LABEL_LARGE + " "
                + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        foodName.setValue(food.getName());

        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setSizeFull();

        Button btnMinus = new Button();
        btnMinus.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnMinus.addStyleName(ValoTheme.BUTTON_SMALL);
        btnMinus.setIcon(FontAwesome.MINUS_CIRCLE);

        Button btnPlus = new Button();
        btnPlus.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnPlus.addStyleName(ValoTheme.BUTTON_SMALL);
        btnPlus.setIcon(FontAwesome.PLUS_CIRCLE);

        // Enable or disable minus and plus button depend on check box status
        btnMinus.setEnabled(checkBox.getValue());
        btnPlus.setEnabled(checkBox.getValue());

        Label foodNumber = new Label();
        foodNumber.addStyleName("TEXT_CENTER FONT_TAHOMA TEXT_BLUE");
        foodNumber.setValue("4");

        hrzChildElementContainer.addComponents(foodName, checkBox, btnMinus,
                foodNumber, btnPlus);
        hrzChildElementContainer.setComponentAlignment(foodName,
                Alignment.MIDDLE_LEFT);
        hrzChildElementContainer.setComponentAlignment(checkBox,
                Alignment.MIDDLE_RIGHT);
        hrzChildElementContainer.setComponentAlignment(btnPlus,
                Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(btnMinus,
                Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(foodNumber,
                Alignment.MIDDLE_CENTER);

        hrzChildElementContainer.setExpandRatio(foodName, 6.0f);
        hrzChildElementContainer.setExpandRatio(checkBox, 1.0f);
        hrzChildElementContainer.setExpandRatio(btnMinus, 1.0f);
        hrzChildElementContainer.setExpandRatio(foodNumber, 1.0f);
        hrzChildElementContainer.setExpandRatio(btnPlus, 1.0f);

        // Handle events
        checkBox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (boolean) event.getProperty().getValue();
                btnMinus.setEnabled(value);
                btnPlus.setEnabled(value);
            }
        });

        btnMinus.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Integer number = Integer.parseInt(foodNumber.getValue());
                if (number > 0) {
                    foodNumber.setValue(number - 1 + "");
                }
            }
        });

        btnPlus.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                foodNumber.setValue(Integer.parseInt(foodNumber.getValue()) + 1
                        + "");
            }
        });

        return hrzChildElementContainer;
    }

    private Component buildFooter() {
        VerticalLayout footer = new VerticalLayout();
        footer.setWidth("100%");
        ;
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        Button btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirm.addStyleName("customizationButton");

        footer.addComponents(btnConfirm);
        footer.setComponentAlignment(btnConfirm, Alignment.MIDDLE_CENTER);

        return footer;
    }
}
