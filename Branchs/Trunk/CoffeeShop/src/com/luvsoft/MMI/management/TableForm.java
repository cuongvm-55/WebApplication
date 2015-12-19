package com.luvsoft.MMI.management;

import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Table;
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
public class TableForm extends Window implements ViewInterface{
    private PropertysetItem tableItem;
    private ViewInterface parentView;

    static public enum STATE{UPDATE, ADDNEW};
    private STATE state;
    private Table table;

    public TableForm(Table _table, STATE _state){
        state = _state;
        table = _table;
        String caption = ( state == STATE.ADDNEW) ? Language.NEW_TABLE : Language.UPDATE_TABLE;
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("340px");
        this.setHeight("240px");
        this.center();

        tableItem = new PropertysetItem();
        tableItem.addItemProperty("number", new ObjectProperty<String>(table.getNumber()+""));

        TextField numberField = new TextField(Language.NUMBER);
        numberField.setRequired(true);
        numberField.focus();

        ComboBox cbType = new ComboBox(Language.FLOOR);
        List<Floor> floorList = Adapter.retrieveFloorList();
        if( floorList != null ){
            for( Floor floor : floorList ){
                cbType.addItem(Language.FLOOR + " " + floor.getNumber());
            }
        }
        cbType.setScrollToSelectedItem(true);
        cbType.setNullSelectionAllowed(false);
        cbType.setTextInputAllowed(false);
        cbType.setRequired(true);
        cbType.setResponsive(true);

        Floor floor = Adapter.getFloorOfTable(table.getId());
        if( floor != null ){
            cbType.setValue(Language.FLOOR + " " + floor.getNumber());
        }

        // Now create the binder and bind the fields
        FieldGroup fieldGroup = new FieldGroup(tableItem);
        fieldGroup.bind(numberField, "number");
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
                if( numberField.getValue().equals("") ){
                    throw new CommitException("Name cannot be empty!");
                }
                try{
                    Integer.parseInt(numberField.getValue().toString());
                }catch(Exception e){
                    throw new CommitException("Only number is accepted for table number");
                }
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                // save data to database
                // refresh parent view
                System.out.println("Post commit...");
                PropertysetItem item = (PropertysetItem)commitEvent.getFieldBinder().getItemDataSource();
                table.setNumber(item.getItemProperty("number").getValue().toString());

                // save data
                switch(state){
                case ADDNEW:
                    table.setId((new ObjectId()).toString());
                    if( !Adapter.addNewTable(table) ){
                        System.out.println("Fail to add food, id: " + table.getId());
                    }

                    break;
                case UPDATE:
                    if( !Adapter.updateTable(table.getId(), _table) ){
                        System.out.println("Fail to update food: " + table.getId() );
                    }

                    break;
                default:
                    // do nothing
                    break;
                }

                // Remove table id from previous floor
                Floor prefloor = Adapter.getFloorOfTable(table.getId());
                if(prefloor != null){
                    List<String> tableIds = prefloor.getTableIdList();
                    tableIds.remove(table.getId());
                    if( !Adapter.updateFloor(prefloor) ){
                        System.out.println("Fail to update category id: " + prefloor.getId());
                    }
                }

                // Add to selected category
                Floor newfloor = Adapter.getFloorByName(cbType.getValue().toString());
                if( newfloor != null ){
                    List<String> tableIds = newfloor.getTableIdList();
                    tableIds.add(table.getId());
                    if( !Adapter.updateFloor(newfloor) ){
                        System.out.println("Fail to update category id: " + newfloor.getId());
                    }
                }
                else{
                    System.out.println("Cannot find category name: " + cbType.getValue().toString());
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

        Label lblCaption = new Label(caption);
        lblCaption.addStyleName(ValoTheme.LABEL_HUGE);
        lblCaption.addStyleName(ValoTheme.LABEL_BOLD);
        lblCaption.addStyleName("TEXT_CENTER");

        VerticalLayout vtcLayout = new VerticalLayout();
        vtcLayout.setSizeFull();
        vtcLayout.addComponents(lblCaption, numberField, cbType, hzLayout);
        vtcLayout.setComponentAlignment(numberField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(cbType, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);
        //this.setComponentAlignment(lblMsg, Alignment.MIDDLE_CENTER);
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
