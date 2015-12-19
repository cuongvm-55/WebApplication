package com.luvsoft.MMI.management;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Table;
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
public class TableManagement extends Window implements ViewInterface{
    List<Floor> toBeDeletedFloors;
    List<String> toBeDeletedTables;

    VerticalLayout layout;
    Label lblWindowName;
    Component panel;
    Component footer;
    public TableManagement(){
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        createView();

        toBeDeletedFloors = new ArrayList<Floor>();
        toBeDeletedTables = new ArrayList<String>();
    }

    @Override
    public void createView(){
        layout = new VerticalLayout();
        layout.setSizeFull();

        // window name
        lblWindowName = new Label(Language.TABLE_MANAGEMENT);
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
        List<Floor> floors = Adapter.retrieveFloorList();
        panel = new Panel();
        if( floors != null ){
            panel = buildTree(floors);
        }
    }

    public Component buildTree(List<Floor> floors) {
        Panel wrapPanel = new Panel();
        wrapPanel.setSizeFull();

        VerticalLayout content = new VerticalLayout();

        for (Floor floor : floors) {
            Button btnEdit = new Button("Sửa");
            btnEdit.addStyleName(ValoTheme.BUTTON_LARGE);
            btnEdit.addStyleName(ValoTheme.BUTTON_LINK);
            btnEdit.addStyleName("TEXT_WHITE");
            btnEdit.setData(floor);
            btnEdit.addClickListener(new ClickListener() {
                
                @Override
                public void buttonClick(ClickEvent event) {
                    FloorForm form = new FloorForm(floor, FloorForm.STATE.UPDATE);
                    form.setParentView(TableManagement.this);
                    getUI().addWindow(form);
                }
            });

            // Check box "To be deleted category"
            CheckBox cbDel = new CheckBox();
            cbDel.setCaption("Xóa");
            cbDel.addStyleName(ValoTheme.CHECKBOX_LARGE);
            cbDel.setData(floor);
            cbDel.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    boolean value = (boolean) event.getProperty().getValue();
                    if( value ){
                        // Check
                        toBeDeletedFloors.add((Floor)cbDel.getData());
                    }
                    else{
                        // Unckeck
                        toBeDeletedFloors.remove((Floor)cbDel.getData());
                    }
                }
            });
            
            HorizontalLayout hzLayout = new HorizontalLayout();
            hzLayout.addComponents(btnEdit, cbDel);
            hzLayout.setComponentAlignment(btnEdit, Alignment.MIDDLE_CENTER);
            hzLayout.setComponentAlignment(cbDel, Alignment.MIDDLE_CENTER);
            hzLayout.setSpacing(true);
            CustomizationTreeElement treeElement = new CustomizationTreeElement(
                    buildContentElement(floor), Language.FLOOR + " " + floor.getNumber(), hzLayout);
            content.addComponent(treeElement);
            content.setComponentAlignment(treeElement, Alignment.TOP_CENTER);
        }

        wrapPanel.setContent(content);
        return wrapPanel;
    }

    private Component buildContentElement(Floor floor) {
        VerticalLayout vtcElementContainer = new VerticalLayout();
        for (String tableId : floor.getTableIdList()) {
            Table table = Adapter.getTableById(tableId);
            if(table != null){
                vtcElementContainer.addComponents(buildChildElementContainer(table));
            }
        }

        return vtcElementContainer;
    }

    private Component buildChildElementContainer(Table table) {
        HorizontalLayout hrzChildElementContainer = new HorizontalLayout();
        hrzChildElementContainer.setSizeFull();
        hrzChildElementContainer.addStyleName(ValoTheme.LAYOUT_CARD);

        // Check box "To be deleted food"
        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setData(table.getId());
        checkBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (boolean) event.getProperty().getValue();
                if( value ){
                    // Check
                    toBeDeletedTables.add(checkBox.getData().toString());
                }
                else{
                    // Unckeck
                    toBeDeletedTables.remove(checkBox.getData().toString());
                }
            }
        });

        Label lblTableName = new Label();
        lblTableName.addStyleName(ValoTheme.LABEL_LARGE + " "
                + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        lblTableName.setValue(Language.TABLE + " " + table.getNumber());

        Button btnEdit = new Button();
        btnEdit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnEdit.addStyleName(ValoTheme.BUTTON_LARGE);
        btnEdit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnEdit.setIcon(FontAwesome.PENCIL);
        btnEdit.setData(table.getId());
        btnEdit.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                TableForm form = new TableForm(table, TableForm.STATE.UPDATE);
                form.setParentView(TableManagement.this);
                getUI().addWindow(form);
            }
        });

        hrzChildElementContainer.addComponents(checkBox, lblTableName, btnEdit);
        hrzChildElementContainer.setExpandRatio(checkBox, 1.0f);
        hrzChildElementContainer.setExpandRatio(lblTableName, 8.0f);
        hrzChildElementContainer.setExpandRatio(btnEdit, 1.0f);
        hrzChildElementContainer.setComponentAlignment(checkBox, Alignment.MIDDLE_CENTER);
        hrzChildElementContainer.setComponentAlignment(lblTableName, Alignment.MIDDLE_CENTER);
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

        Button btnAddTable = new Button(Language.ADD_TABLE);
        btnAddTable.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddTable.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddTable.addStyleName("margin-right1 margin-bottom1");
        btnAddTable.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                TableForm form = new TableForm(new Table(), TableForm.STATE.ADDNEW);
                form.setParentView(TableManagement.this);
                getUI().addWindow(form);
            }
        });
        Button btnRemoveTable = new Button(Language.DELETE_TABLES);
        btnRemoveTable.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveTable.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnRemoveTable.addStyleName("margin-bottom1");
        btnRemoveTable.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.show( getUI(), Language.CONFIRM_DELETE_TITLE, Language.CONFIRM_DELETE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    for( int i = 0; i < toBeDeletedTables.size(); i++ ){
                                        if( Adapter.removeTable(toBeDeletedTables.get(i)) ){
                                            // update corresponding floor
                                            Floor floor = Adapter.getFloorOfTable(toBeDeletedTables.get(i));
                                            if(floor != null){
                                                floor.getTableIdList().remove(toBeDeletedTables.get(i));
                                                Adapter.updateFloor(floor);
                                            }
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
        
        Button btnAddFloor = new Button(Language.ADD_FLOOR);
        btnAddFloor.addStyleName(ValoTheme.BUTTON_HUGE);
        btnAddFloor.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddFloor.addStyleName("margin-right1");
        btnAddFloor.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                        FloorForm form = new FloorForm(new Floor(), FloorForm.STATE.ADDNEW);
                        form.setParentView(TableManagement.this);
                        getUI().addWindow(form);
            }
        });

        Button btnRemoveFloor = new Button(Language.DELETE_FLOOR);
        btnRemoveFloor.addStyleName(ValoTheme.BUTTON_HUGE);
        btnRemoveFloor.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnRemoveFloor.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.show( getUI(), Language.CONFIRM_DELETE_TITLE, Language.CONFIRM_DELETE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    for( Floor floor : toBeDeletedFloors){
                                        if(floor != null){
                                            // Remove table list first
                                            List<String> tableIdList = floor.getTableIdList();
                                            System.out.println("size: " + tableIdList.size());
                                            for( int i = 0; i < tableIdList.size(); i++ ){
                                                Adapter.removeTable(tableIdList.get(i));
                                            }

                                            Adapter.removeFloor(floor.getId());

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

        hzLayoutFoodCtrl.addComponents(btnAddTable, btnRemoveTable);
        hzLayoutFoodCtrl.setComponentAlignment(btnAddTable, Alignment.MIDDLE_CENTER);
        hzLayoutFoodCtrl.setComponentAlignment(btnRemoveTable, Alignment.MIDDLE_CENTER);

        hzLayoutCategoryCtrl.addComponents(btnAddFloor, btnRemoveFloor);
        hzLayoutCategoryCtrl.setComponentAlignment(btnAddFloor, Alignment.MIDDLE_CENTER);
        hzLayoutCategoryCtrl.setComponentAlignment(btnRemoveFloor, Alignment.MIDDLE_CENTER);
        
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
