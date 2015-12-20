package com.luvsoft.MMI.management;

import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
/**
 * 
 * @author cuongvm-55
 *
 * This class is used to implement category form
 */
@SuppressWarnings("serial")
public class FoodForm extends Window implements ViewInterface{
    private PropertysetItem foodItem;
    private ViewInterface parentView;

    static public enum STATE{UPDATE, ADDNEW};
    private STATE state;
    private Food food;

    public FoodForm(Food _food, STATE _state){
        state = _state;
        food = _food;
        String caption = ( state == STATE.ADDNEW) ? Language.NEW_FOOD : Language.UPDATE_FOOD;
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("340px");
        this.setHeight("330px");
        this.center();
        
        foodItem = new PropertysetItem();
        foodItem.addItemProperty("name", new ObjectProperty<String>(food.getName()));
        foodItem.addItemProperty("price", new ObjectProperty<Double>(food.getPrice()));

        TextField nameField = new TextField(Language.NAME);
        nameField.setRequired(true);
        nameField.focus();

        TextField priceField = new TextField(Language.PRICE);
        priceField.setRequired(true);

        ComboBox cbType = new ComboBox(Language.CATEGORY);
        List<Category> cateList = Adapter.retrieveCategoryList();
        if( cateList != null ){
            for( Category cate : cateList ){
                cbType.addItem(cate.getName());
            }
        }
        cbType.setScrollToSelectedItem(true);
        cbType.setNullSelectionAllowed(false);
        cbType.setTextInputAllowed(false);
        cbType.setRequired(true);
        cbType.setResponsive(true);

        Category cate = Adapter.getCategoryOfFood(food.getId());
        if( cate != null ){
            cbType.setValue(cate.getName());
        }

        // Now create the binder and bind the fields
        FieldGroup fieldGroup = new FieldGroup(foodItem);
        fieldGroup.bind(nameField, "name");
        fieldGroup.bind(priceField, "price");
        fieldGroup.setBuffered(true);
        fieldGroup.setFieldFactory(new FieldGroupFieldFactory() {
            @SuppressWarnings("rawtypes")
            @Override
            public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
                // TODO Auto-generated method stub
                return createField(dataType, fieldType);
            }
        });
        fieldGroup.addCommitHandler(new CommitHandler() {
            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                // validate data
                if( nameField.getValue().equals("") ){
                    throw new CommitException("Name cannot be empty!");
                }
                try{
                   Double.parseDouble(priceField.getValue());
                }catch(Exception e){
                    throw new CommitException("You must enter number value for price");
                }
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                // save data to database
                // refresh parent view
                PropertysetItem item = (PropertysetItem)commitEvent.getFieldBinder().getItemDataSource();
                food.setName(item.getItemProperty("name").getValue().toString());
                food.setPrice(Double.parseDouble(item.getItemProperty("price").getValue().toString()));

                // save data
                switch(state){
                case ADDNEW:
                    food.setId((new ObjectId()).toString());
                    if( !Adapter.addNewFood(food)){
                        System.out.println("Fail to add food, id: " + food.getId());
                    }

                    break;
                case UPDATE:
                    if( !Adapter.updateFood(food) ){
                        System.out.println("Fail to update food: " + food.getId() );
                    }

                    break;
                default:
                    // do nothing
                    break;
                }

                // Remove food id from previous category
                Category preCate = Adapter.getCategoryOfFood(food.getId());
                if( !preCate.getName().equals(cbType.getValue().toString()) ){
                    if(preCate != null){
                        List<String> foodIds = preCate.getFoodIdList();
                        foodIds.remove(food.getId());
                        if( !Adapter.updateCategory(preCate) ){
                            System.out.println("Fail to update category id: " + preCate.getId());
                        }
                    }

                    // Add to selected category
                    Category newCate = Adapter.getCategoryByName(cbType.getValue().toString());
                    if( newCate != null ){
                        List<String> foodIds = newCate.getFoodIdList();
                        foodIds.add(food.getId());
                        if( !Adapter.updateCategory(newCate) ){
                            System.out.println("Fail to update category id: " + newCate.getId());
                        }
                    }
                    else{
                        System.out.println("Cannot find category name: " + cbType.getValue().toString());
                    }
                }

                parentView.reloadView();
                close();
            }
        });
        // Control buttons
        Button btnSave = new Button(Language.SAVE);
        btnSave.addStyleName(ValoTheme.BUTTON_HUGE);
        btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSave.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                } catch (CommitException e) {
                    // e.printStackTrace();
                }
            }
        });

        Button btnCancel = new Button(Language.CANCEL);
        btnCancel.addStyleName(ValoTheme.BUTTON_HUGE);
        btnCancel.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnCancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                fieldGroup.discard();
                parentView.reloadView();
                close();
            }
        });
        HorizontalLayout hzLayout = new HorizontalLayout();
        //hzLayout.setSizeFull();
        hzLayout.addComponents(btnSave, btnCancel);
        hzLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        hzLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
        hzLayout.setSpacing(true);

        Label lblCaption = new Label(caption);
        lblCaption.addStyleName(ValoTheme.LABEL_HUGE);
        lblCaption.addStyleName(ValoTheme.LABEL_BOLD);
        lblCaption.addStyleName("TEXT_CENTER");

        VerticalLayout vtcLayout = new VerticalLayout();
        vtcLayout.setSizeFull();
        vtcLayout.addComponents(lblCaption, nameField, priceField, cbType, hzLayout);
        vtcLayout.setComponentAlignment(nameField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(priceField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(cbType, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);

        vtcLayout.setSpacing(true);
        this.setContent(vtcLayout);
    }

    @Override
    public void createView() {
    }

    @Override
    public void reloadView() {
    }

    @Override
    public ViewInterface getParentView() {
        return parentView;
    }
    @Override
    public void setParentView(ViewInterface parentView) {
        this.parentView = parentView;
    }
}
