package com.luvsoft.MMI.management;

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
    static public enum STATE{UPDATE, ADDNEW};
    private STATE state;
    private Category category;
    public CategoryForm(Category _category, STATE _state){
        category = _category;
        state = _state;
        String caption = ( state == STATE.ADDNEW) ? Language.NEW_CATEGORY : Language.UPDATE_CATEGORY;
        // this.setCaption(caption);
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("340px");
        this.setHeight("180px");
        this.center();

        categoryItem = new PropertysetItem();
        categoryItem.addItemProperty("name", new ObjectProperty<String>(category.getName()));

        TextField nameField = new TextField(Language.NAME);
        nameField.setRequired(true);
        nameField.focus();

        // Now create the binder and bind the fields
        FieldGroup fieldGroup = new FieldGroup(categoryItem);
        fieldGroup.bind(nameField, "name");
        fieldGroup.setBuffered(true);
        fieldGroup.addCommitHandler(new CommitHandler() {
            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                // validate data
                if( nameField.getValue().equals("") ){
                    throw new CommitException("Name cannot be empty!");
                }
                else if( state == STATE.ADDNEW && Adapter.isCategoryNameExist(nameField.getValue()) ){
                    throw new CommitException("Category is already exist!");
                }
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                // save data to database
                // refresh parent view
                PropertysetItem item = (PropertysetItem)commitEvent.getFieldBinder().getItemDataSource();
                category.setName(item.getItemProperty("name").getValue().toString());
                switch(state){
                case ADDNEW:
                    category.setId((new ObjectId()).toString());
                    if( Adapter.addNewCategory(category)){
                        parentView.reloadView();
                        close();
                    }
                    break;
                case UPDATE:
                    if( Adapter.updateCategory(category) ){
                        parentView.reloadView();
                        close();
                    }
                    break;
                default:
                    break;
                }
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
                    lblMsg.setValue("All field are required!");
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
        hzLayout.addComponents(btnSave, btnCancel);
        hzLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        hzLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
        hzLayout.setSpacing(true);

        Label lblCaption = new Label(caption);
        lblCaption.addStyleName(ValoTheme.LABEL_BOLD);
        lblCaption.addStyleName(ValoTheme.LABEL_HUGE);
        lblCaption.addStyleName("TEXT_CENTER");

        lblMsg = new Label("");
        lblMsg.setSizeFull();
        VerticalLayout vtcLayout = new VerticalLayout();
        vtcLayout.setSizeFull();
        vtcLayout.addComponents(lblCaption, nameField, hzLayout);
        vtcLayout.setComponentAlignment(nameField, Alignment.MIDDLE_CENTER);
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
