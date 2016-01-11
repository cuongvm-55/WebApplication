package com.luvsoft.MMI.order;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.order.OrderDetailRecord.ChangedFlag;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Category;
import com.luvsoft.entities.Food;
import com.luvsoft.entities.Types;
import com.luvsoft.entities.Types.State;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author datnq.55
 *
 */

@SuppressWarnings("serial")
public class AddFoodView extends AbstractOrderView {
    private List<Category> listOfCategory;
    private List<OrderDetailRecord> orderDetailRecordList;
    private List<OrderDetailRecordExtension> orderDetailExtensionList;

    public AddFoodView() {
        super();
    }

    @Override
    public void createView() {
        // Get all foods from database
        // Do not display category that has no food
        List<Category> cateList = Adapter.retrieveCategoryList();
        if(cateList != null){
            listOfCategory = new ArrayList<Category>();
            for( Category cate : cateList ){
                if( !cate.getFoodIdList().isEmpty() ){
                    listOfCategory.add(cate);
                }
            }
        }

        this.orderDetailRecordList = ((OrderInfoView) this.getParentView()).getOrderDetailRecordList();
        if (this.orderDetailRecordList == null) {
            this.orderDetailRecordList = new ArrayList<OrderDetailRecord>();
        }
        orderDetailExtensionList = new ArrayList<OrderDetailRecordExtension>();


        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        addStyleName("close-style");

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        Label lblTableNumber = new Label(Language.TABLE + " " + getCurrentTable().getNumber());
        lblTableNumber.addStyleName("FONT_TAHOMA TEXT_CENTER BACKGROUND_BLUE TEXT_WHITE FONT_OVERSIZE");
        lblTableNumber.addStyleName(ValoTheme.LABEL_BOLD);
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
        if( listOfCategory == null ){
            wrapPanel.setContent(content);
            return wrapPanel;
        }
        for (Category category : listOfCategory) {
            VerticalLayout defaultLayout = new VerticalLayout();
            CustomizationTreeElement treeElement = new CustomizationTreeElement(
                    /*buildContentElement(category)*/defaultLayout, category.getName(), null);
            treeElement.setContentCollapse();

            treeElement.getHorzFloorTitleContainer().addLayoutClickListener(new LayoutClickListener() {
                @Override
                public void layoutClick(LayoutClickEvent event) {
                    if( treeElement.getContent().isVisible() ){
                        treeElement.setContent(defaultLayout);
                        treeElement.setContentCollapse();
                    }
                    else{
                        treeElement.setContent(buildContentElement(category));
                        treeElement.setContentExpand();
                    }
                }
            });
            
            treeElement.getBtnExpandElement().addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    if( treeElement.getContent().isVisible() ){
                        treeElement.setContent(defaultLayout);
                        treeElement.setContentCollapse();
                    }
                    else{
                        treeElement.setContent(buildContentElement(category));
                        treeElement.setContentExpand();
                    }
                }
            });

            content.addComponent(treeElement);
            content.setComponentAlignment(treeElement, Alignment.TOP_CENTER);
        }

        wrapPanel.setContent(content);
        return wrapPanel;
    }

    private Component buildContentElement(Category category) {
        GridLayout gridElementContent = new GridLayout();
        gridElementContent.addStyleName("large-checkbox grid-layout-custom");
        gridElementContent.setColumns(3);
        gridElementContent.setSizeFull();
        gridElementContent.setSpacing(true);
        for(int index=0; index < category.getFoodIdList().size();index++){
            buildChildElementContainer(
                    Adapter.getFood(category.getId(), index), gridElementContent);
        }
        return gridElementContent;
    }

    private void buildChildElementContainer(Food food, GridLayout grid) {
        // Create a temporary order detail
        if( food == null ){
            return;
        }
        OrderDetailRecord orderDetail = new OrderDetailRecord();
        orderDetail.setFoodId(food.getId());
        orderDetail.setFoodName(food.getName());
        orderDetail.setPrice(food.getPrice());
        orderDetail.setChangeFlag(ChangedFlag.ADDNEW);
        orderDetail.setStatus(State.WAITING);

        OrderDetailRecordExtension orderDetailExtension = new OrderDetailRecordExtension(
                orderDetail, false);

        Label foodName = new Label();
        foodName.addStyleName(ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE FONT_OVERSIZE");
        foodName.setValue(food.getName());

        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setSizeFull();

        NativeSelect cbNumberList = new NativeSelect();
        cbNumberList.setSizeFull();
        cbNumberList.setValue(1);
        cbNumberList.setNullSelectionAllowed(false);
        cbNumberList.setResponsive(true);
        cbNumberList.addStyleName("FONT_OVERSIZE");

        for(int i=1; i<=20; i++) {
            cbNumberList.addItem(i);
        }
        cbNumberList.setValue(1);

        cbNumberList.setEnabled(true);

        grid.addComponents(foodName, checkBox, cbNumberList);
        grid.setColumnExpandRatio(0, 4.5f);
        grid.setColumnExpandRatio(1, 0.5f);
        grid.setColumnExpandRatio(2, 1.0f);

        grid.setComponentAlignment(cbNumberList, Alignment.MIDDLE_CENTER);
        // Handle events
        checkBox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (boolean) event.getProperty().getValue();
                //cbNumberList.setEnabled(value);
                Integer number = (Integer) cbNumberList.getValue();
                if(number == null) {
                    //cbNumberList.setValue(1);
                    number = 1;
                }

                orderDetailExtension.setEnable(value);
                if (value == true) {
                    orderDetailExtension.getOrderDetailRecord().setQuantity(number);
                }
            }
        });
        cbNumberList.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                String foodNumber = event.getProperty().getValue().toString();
                if (!foodNumber.equals("")) {
                    Integer number = Integer.parseInt(foodNumber);
                    if (number > 1) {
                        orderDetailExtension.getOrderDetailRecord().setQuantity(number);
                    }
                }
            }
        });

        cbNumberList.setImmediate(false);
        checkBox.setImmediate(false);
        orderDetailExtensionList.add(orderDetailExtension);
    }

    private Component buildFooter() {
        CssLayout footer = new CssLayout();
        footer.setWidth("100%");

        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR + " TEXT_CENTER");

        Button btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName("BUTTON_GIGANTIC");
        btnConfirm.addStyleName(ValoTheme.BUTTON_PRIMARY);

        btnConfirm.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                for (OrderDetailRecordExtension orderDetailExtension : orderDetailExtensionList) {
                    System.out.println(orderDetailExtension.isEnable()
                            + " "
                            + orderDetailExtension.getOrderDetailRecord().getFoodId()
                            + " "
                            + orderDetailExtension.getOrderDetailRecord().getOrderDetailId()
                            + " "
                            + orderDetailExtension.getOrderDetailRecord()
                                    .getQuantity() + " ");

                    if (orderDetailExtension.isEnable()) {
                        // Merge new order details list to old order details list
                        boolean shouldCreateNewOne = true;

                        // If id of records are matched
                        for (OrderDetailRecord record : orderDetailRecordList) {
                            if (orderDetailExtension.getOrderDetailRecord().getFoodId().equals(record.getFoodId())){
                                    ChangedFlag currentFlag = record.getChangeFlag();
                                    // we merge the quantity of food if record's in waiting state
                                    if( record.getStatus() == Types.State.WAITING ){
                                        record.setQuantity(record.getQuantity() + orderDetailExtension.getOrderDetailRecord().getQuantity());
                                        shouldCreateNewOne = false;
                                        // record is add new, set it to waiting state
                                        if( currentFlag == ChangedFlag.ADDNEW ){
                                            record.setStatus(State.WAITING);
                                        }
                                        else{
                                            // if record is not new, just mark as modified
                                            record.setChangeFlag(ChangedFlag.MODIFIED);
                                        }
                                    }
                                    else if( currentFlag == ChangedFlag.DELETED ) {
                                        shouldCreateNewOne = true;
                                    }

                                    System.out.println("Merged " + record.getFoodId() + " " + record.getQuantity());
                            }
                        }
                        if ( shouldCreateNewOne ) {
                            System.out.println("ADD NEW");
                            OrderDetailRecord record = orderDetailExtension.getOrderDetailRecord();
                            record.setOrderDetailId(new ObjectId().toString());
                            record.setChangeFlag(ChangedFlag.ADDNEW);
                            record.setStatus(State.WAITING);
                            record.setPreviousStatus(State.WAITING);
                            orderDetailRecordList.add(record);
                        }
                    }
                }

                if(!orderDetailRecordList.isEmpty()) {
                    System.out.println("orderDetailRecordList is not empty");
                    ((OrderInfoView) getParentView()).setOrderDetailRecordList(orderDetailRecordList);
                    ((OrderInfoView) getParentView()).setOrderDetailListChanged(true);
                    getParentView().reloadView();
                }
                close();
            }
        });
        footer.addComponents(btnConfirm);

        return footer;
    }

    @Override
    public void reloadView() {
        // No thing to do
    }
}
