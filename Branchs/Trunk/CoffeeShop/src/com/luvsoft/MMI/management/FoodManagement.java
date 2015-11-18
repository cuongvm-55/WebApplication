package com.luvsoft.MMI.management;

import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class FoodManagement extends Window{
    public FoodManagement(){
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        setupUI();
    }

    public void setupUI(){
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        // window name
        Label lblWindowName = new Label(Language.FOOD_MANAGEMENT);
        lblWindowName.addStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblWindowName.setWidth("100%");

        // food list
        List<Category> categories = Adapter.retrieveCategoryList();
        Component panel = new Panel();
        if( categories != null ){
            panel = buildContent(categories);
        }

        // control buttons
        Component footer = buildFooter();
        layout.addComponents(lblWindowName, panel, footer);
        layout.setExpandRatio(panel, 1.0f);
        layout.setExpandRatio(panel, 7.0f);
        layout.setExpandRatio(footer, 2.0f);
        this.setContent(layout);
    }

    public Component buildContent(List<Category> categories) {
        Panel wrapPanel = new Panel();
        wrapPanel.setSizeFull();

        VerticalLayout content = new VerticalLayout();

        for (Category category : categories) {
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
            vtcElementContainer.setSpacing(true);
        }

        return vtcElementContainer;
    }

    private Component buildChildElementContainer(Food food) {
        HorizontalLayout hrzChildElementContainer = new HorizontalLayout();
        hrzChildElementContainer.setSizeFull();
        hrzChildElementContainer.addStyleName(ValoTheme.LAYOUT_CARD);

        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setSizeFull();

        Label lblfoodName = new Label();
        lblfoodName.addStyleName(ValoTheme.LABEL_LARGE + " "
                + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        lblfoodName.setValue(food.getName());

        Label lblprice = new Label();
        lblprice.addStyleName(ValoTheme.LABEL_LARGE + " "
                + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        lblprice.setValue(food.getPrice() +"");

        Button btnEdit = new Button();
        btnEdit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnEdit.addStyleName(ValoTheme.BUTTON_SMALL);
        btnEdit.setIcon(FontAwesome.EDIT);
        btnEdit.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            }
        });

        hrzChildElementContainer.addComponents(checkBox, lblfoodName, lblprice, btnEdit);
        hrzChildElementContainer.setComponentAlignment(checkBox, Alignment.MIDDLE_RIGHT);

        hrzChildElementContainer.setExpandRatio(checkBox, 2.0f);
        hrzChildElementContainer.setExpandRatio(lblfoodName, 5.0f);
        hrzChildElementContainer.setExpandRatio(lblprice, 2.0f);
        hrzChildElementContainer.setExpandRatio(btnEdit, 1.0f);

        return hrzChildElementContainer;
    }

    private Component buildFooter() {
        VerticalLayout footer = new VerticalLayout();
        footer.setWidth("100%");

        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        HorizontalLayout hzLayoutFoodCtrl = new HorizontalLayout();
        HorizontalLayout hzLayoutCategoryCtrl = new HorizontalLayout();

        Button btnAddFood = new Button(Language.ADD_FOOD);
        btnAddFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddFood.addStyleName("customizationButton");

        Button btnRemoveFood = new Button(Language.DELETE_FOODS);
        btnRemoveFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveFood.addStyleName("customizationButton");

        Button btnAddCategory = new Button(Language.ADD_CATEGORY);
        btnAddCategory.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddCategory.addStyleName("customizationButton");

        Button btnRemoveCategory = new Button(Language.DELETE_CATEGORY);
        btnRemoveCategory.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveCategory.addStyleName("customizationButton");

        hzLayoutFoodCtrl.addComponents(btnAddFood, btnRemoveFood);
        hzLayoutFoodCtrl.setComponentAlignment(btnAddFood, Alignment.MIDDLE_CENTER);
        hzLayoutFoodCtrl.setComponentAlignment(btnRemoveFood, Alignment.MIDDLE_CENTER);

        hzLayoutCategoryCtrl.addComponents(btnAddCategory, btnRemoveCategory);
        hzLayoutCategoryCtrl.setComponentAlignment(btnAddCategory, Alignment.MIDDLE_CENTER);
        hzLayoutCategoryCtrl.setComponentAlignment(btnRemoveCategory, Alignment.MIDDLE_CENTER);

        footer.addComponents(hzLayoutFoodCtrl, hzLayoutCategoryCtrl);
        footer.setComponentAlignment(hzLayoutFoodCtrl, Alignment.MIDDLE_CENTER);
        footer.setComponentAlignment(hzLayoutCategoryCtrl, Alignment.MIDDLE_CENTER);
        return footer;
    }
}
