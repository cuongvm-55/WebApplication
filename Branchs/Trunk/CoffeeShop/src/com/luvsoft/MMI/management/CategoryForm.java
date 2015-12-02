package com.luvsoft.MMI.management;

import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
public class CategoryForm extends Window implements ViewInterface{
    private PropertysetItem categoryItem;
    private ViewInterface parentView;

    private Label lblMsg;
    public CategoryForm(){
        this.setCaption("New Category");
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("340px");
        this.setHeight("240px");
        this.center();

        categoryItem = new PropertysetItem();
        categoryItem.addItemProperty("code", new ObjectProperty<String>(""));
        categoryItem.addItemProperty("name", new ObjectProperty<String>(""));

        //this.setItemDataSource(item);
        TextField codeField = new TextField("Code");
        codeField.setRequired(true);

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);

        // Now create the binder and bind the fields
        FieldGroup fieldGroup = new FieldGroup(categoryItem);
        fieldGroup.bind(codeField, "code");
        fieldGroup.bind(nameField, "name");
        fieldGroup.setBuffered(true);
        fieldGroup.addCommitHandler(new CommitHandler() {
            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                // validate data
                System.out.println("Pre commit...");
                if( codeField.getValue().equals("") && nameField.getValue().equals("") ){
                    throw new CommitException("Code or name cannot be empty!");
                }
                else if( isCategoryNameExist(nameField.getValue()) ){
                    throw new CommitException("Category is already exist!");
                }
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                // save data to database
                // refresh parent view
                System.out.println("Post commit...");
                PropertysetItem item = (PropertysetItem)commitEvent.getFieldBinder().getItemDataSource();
                Category category = new Category();
                category.setCode(item.getItemProperty("code").getValue().toString());
                category.setName(item.getItemProperty("name").getValue().toString());
                category.setId((new ObjectId()).toString());
                if( Adapter.addNewCategory(category)){
                    parentView.reloadView();
                    close();
                }
            }
        });
        // Control buttons
        Button btnSave = new Button(Language.SAVE);
        btnSave.addStyleName(ValoTheme.BUTTON_HUGE);
        btnSave.addStyleName("customizationButton");
        btnSave.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                } catch (CommitException e) {
                    // e.printStackTrace();
                    lblMsg.setValue("All field are required!");
                }
            }
        });

        Button btnCancel = new Button(Language.CANCEL);
        btnCancel.addStyleName(ValoTheme.BUTTON_HUGE);
        btnCancel.addStyleName("customizationButton");
        btnCancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Discard...");
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

        lblMsg = new Label("");
        lblMsg.setSizeFull();
        
        VerticalLayout vtcLayout = new VerticalLayout();
        vtcLayout.setSizeFull();
        vtcLayout.addComponents(codeField, nameField, hzLayout);
        vtcLayout.setComponentAlignment(codeField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(nameField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);
        vtcLayout.setSpacing(true);
        this.setContent(vtcLayout);
    }

    private boolean isCategoryNameExist(String categoryName){
        List<Category> cateList = Adapter.retrieveCategoryList();
        if( cateList != null ){
            for( Category cate : cateList ){
                if( cate.getName().equals(categoryName) ){
                    return true;
                }
            }
        }
        return false;
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
