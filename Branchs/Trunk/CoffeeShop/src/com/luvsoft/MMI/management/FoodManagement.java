package com.luvsoft.MMI.management;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.management.FoodForm.STATE;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class FoodManagement extends Window implements ViewInterface{
    List<Category> toBeDeletedCategories;
    List<String> toBeDeletedFoods;

    VerticalLayout layout;
    Label lblWindowName;
    Component panel;
    Component footer;
    public FoodManagement(){
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        createView();

        toBeDeletedCategories = new ArrayList<Category>();
        toBeDeletedFoods = new ArrayList<String>();
    }

    @Override
    public void createView(){
        layout = new VerticalLayout();
        layout.setSizeFull();

        // window name
        lblWindowName = new Label(Language.FOOD_MANAGEMENT);
        lblWindowName.addStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblWindowName.setWidth("100%");

        // Content
        buildContent();

        // control buttons
        footer = buildFooter();
        layout.addComponents(lblWindowName, panel, footer);
        layout.setExpandRatio(lblWindowName, 1.2f);
        layout.setExpandRatio(panel, 10.0f);
        this.setContent(layout);
    }

    public void buildContent(){
        // food list
        List<Category> categories = Adapter.retrieveCategoryList();
        panel = new Panel();
        if( categories != null ){
            panel = buildTree(categories);
        }
    }

    public Component buildTree(List<Category> categories) {
        Panel wrapPanel = new Panel();
        wrapPanel.setSizeFull();
        VerticalLayout content = new VerticalLayout();

        for (Category category : categories) {
            // Check box "To be deleted category"
            CheckBox cbDel = new CheckBox();
            cbDel.setCaption("Xóa");
            cbDel.addStyleName(ValoTheme.CHECKBOX_LARGE);
            cbDel.setData(category);
            cbDel.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    boolean value = (boolean) event.getProperty().getValue();
                    if( value ){
                        // Check
                        toBeDeletedCategories.add((Category)cbDel.getData());
                    }
                    else{
                        // Unckeck
                        toBeDeletedCategories.remove((Category)cbDel.getData());
                    }
                }
            });
            Button btnModify = new Button("Sửa");
            btnModify.addStyleName(ValoTheme.BUTTON_LARGE);
            btnModify.addStyleName(ValoTheme.BUTTON_LINK);
            btnModify.addStyleName("TEXT_WHITE");
            btnModify.setData(category);
            btnModify.addClickListener(new ClickListener() {
                
                @Override
                public void buttonClick(ClickEvent event) {
                    CategoryForm form = new CategoryForm(category, CategoryForm.STATE.UPDATE);
                    form.setParentView(FoodManagement.this);
                    getUI().addWindow(form);
                }
            });

            HorizontalLayout hzLayout = new HorizontalLayout();
            hzLayout.addComponents(btnModify, cbDel);
            hzLayout.setComponentAlignment(cbDel, Alignment.MIDDLE_CENTER);
            hzLayout.setComponentAlignment(btnModify, Alignment.MIDDLE_CENTER);
            hzLayout.setSpacing(true);
            CustomizationTreeElement treeElement = new CustomizationTreeElement(
                    buildContentElement(category), category.getName(), hzLayout);
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

        // Check box "To be deleted food"
        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setData(food.getId());
        checkBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (boolean) event.getProperty().getValue();
                if( value ){
                    // Check
                    toBeDeletedFoods.add(checkBox.getData().toString());
                }
                else{
                    // Unckeck
                    toBeDeletedFoods.remove(checkBox.getData().toString());
                }
            }
        });

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
        btnEdit.addStyleName(ValoTheme.BUTTON_LARGE);
        btnEdit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnEdit.setIcon(FontAwesome.PENCIL);
        btnEdit.setData(food.getId());
        btnEdit.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                FoodForm form = new FoodForm(food, STATE.UPDATE);
                form.setParentView(FoodManagement.this);
                getUI().addWindow(form);
            }
        });

        hrzChildElementContainer.addComponents(checkBox, lblfoodName, lblprice, btnEdit);
        hrzChildElementContainer.setExpandRatio(checkBox, 1.0f);
        hrzChildElementContainer.setExpandRatio(lblfoodName, 6.0f);
        hrzChildElementContainer.setExpandRatio(lblprice, 2.0f);
        hrzChildElementContainer.setExpandRatio(btnEdit, 1.0f);
        hrzChildElementContainer.setComponentAlignment(checkBox, Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(lblfoodName, Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(lblprice, Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(btnEdit, Alignment.MIDDLE_CENTER);

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
        btnAddFood.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddFood.addStyleName("margin-right1 margin-bottom1");
        btnAddFood.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                FoodForm form = new FoodForm(new Food(), STATE.ADDNEW);
                form.setParentView(FoodManagement.this);
                getUI().addWindow(form);
            }
        });
        Button btnRemoveFood = new Button(Language.DELETE_FOODS);
        btnRemoveFood.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveFood.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnRemoveFood.addStyleName("margin-bottom1");
        btnRemoveFood.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.show( getUI(), Language.CONFIRM_DELETE_TITLE, Language.CONFIRM_DELETE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    for( int i = 0; i < toBeDeletedFoods.size(); i++ ){
                                        String foodId = toBeDeletedFoods.get(i);
                                        Adapter.removeFood(foodId);
                                        
                                        // update corresponding category
                                        Category cate = Adapter.getCategoryOfFood(foodId);
                                        if( cate != null ){
                                            cate.getFoodIdList().remove(foodId);
                                            Adapter.updateCategory(cate.getId(), cate);
                                        }
                                    }

                                    // Refresh
                                    reloadView();
                                } else {
                                    System.out.println("user canceled, do nothing!");
                                }
                            }
                        });
            }
        });
        
        Button btnAddCategory = new Button(Language.ADD_CATEGORY);
        btnAddCategory.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddCategory.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddCategory.addStyleName("margin-right1");
        btnAddCategory.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                        CategoryForm form = new CategoryForm(new Category(), CategoryForm.STATE.ADDNEW);
                        form.setParentView(FoodManagement.this);
                        getUI().addWindow(form);
            }
        });

        Button btnRemoveCategory = new Button(Language.DELETE_CATEGORY);
        btnRemoveCategory.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveCategory.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnRemoveCategory.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.show( getUI(), Language.CONFIRM_DELETE_TITLE, Language.CONFIRM_DELETE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    for( Category category : toBeDeletedCategories){
                                        if(category != null){
                                            // Remove food list first
                                            List<String> foodIdList = category.getFoodIdList();
                                            System.out.println("size: " + foodIdList.size());
                                            for( int i = 0; i < foodIdList.size(); i++ ){
                                                Adapter.removeFood(foodIdList.get(i));
                                            }

                                            Adapter.removeCategory(category.getId());

                                            // Refresh
                                            reloadView();
                                        }
                                    }
                                } else {
                                    System.out.println("user canceled, do nothing!");
                                }
                            }
                        });
            }
        });
        
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

    @Override
    public void reloadView() {
        layout = new VerticalLayout();
        layout.setSizeFull();
        buildContent();
        layout.addComponents(lblWindowName, panel, footer);
        layout.setExpandRatio(lblWindowName, 1.2f);
        layout.setExpandRatio(panel, 10.0f);
        this.setContent(layout);
    }

    @Override
    public ViewInterface getParentView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParentView(ViewInterface parentView) {
        // TODO Auto-generated method stub
        
    }
}
