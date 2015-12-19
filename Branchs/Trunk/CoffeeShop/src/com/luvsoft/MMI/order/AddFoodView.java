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
import com.luvsoft.entities.Types.State;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
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

    private class OrderDetailRecordExtension {
        private OrderDetailRecord orderDetailRecord;
        private boolean enable;

        public OrderDetailRecordExtension(OrderDetailRecord orderDetailRecord, boolean enable) {
            this.orderDetailRecord = orderDetailRecord;
            this.enable = enable;
        }

        public OrderDetailRecord getOrderDetailRecord() {
            return orderDetailRecord;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    private List<Category> listOfCategory = new ArrayList<Category>();
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
        lblTableNumber.addStyleName("FONT_TAHOMA TEXT_CENTER BACKGROUND_BLUE TEXT_WHITE");
        lblTableNumber.addStyleName(ValoTheme.LABEL_BOLD);
        lblTableNumber.addStyleName(ValoTheme.LABEL_HUGE);
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
                    buildContentElement(category), category.getName(), null);
            treeElement.setContentCollapse();
            treeElement.getBtnExpandElement().addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    System.out.println("cateId: " + category.getId());
                    treeElement.setContent(buildContentElement(category));
                    treeElement.setContentExpand();
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
        gridElementContent.addStyleName("large-checkbox");
        gridElementContent.setColumns(5);
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
        OrderDetailRecord orderDetail = new OrderDetailRecord();
        orderDetail.setFoodId(food.getId());
        orderDetail.setFoodName(food.getName());
        orderDetail.setPrice(food.getPrice());
        orderDetail.setChangeFlag(ChangedFlag.ADDNEW);
        orderDetail.setStatus(State.WAITING);

        OrderDetailRecordExtension orderDetailExtension = new OrderDetailRecordExtension(
                orderDetail, false);

        Label foodName = new Label();
        foodName.addStyleName(ValoTheme.LABEL_HUGE + " " + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        foodName.setValue(food.getName());

        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setSizeFull();

        Button btnMinus = new Button();
        btnMinus.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnMinus.addStyleName(ValoTheme.BUTTON_LARGE);
        btnMinus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnMinus.setIcon(FontAwesome.MINUS_CIRCLE);

        Button btnPlus = new Button();
        btnPlus.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnPlus.addStyleName(ValoTheme.BUTTON_LARGE);
        btnPlus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnPlus.setIcon(FontAwesome.PLUS_CIRCLE);

        // Enable or disable minus and plus button depend on check box status
        btnMinus.setEnabled(checkBox.getValue());
        btnPlus.setEnabled(checkBox.getValue());

        Label foodNumber = new Label();
        foodNumber.addStyleName("bold TEXT_CENTER FONT_TAHOMA TEXT_BLUE " + ValoTheme.LABEL_HUGE);

        grid.addComponents(foodName, checkBox, btnMinus, foodNumber, btnPlus);
        grid.setColumnExpandRatio(0, 5.0f);
        grid.setColumnExpandRatio(1, 0.5f);
        grid.setColumnExpandRatio(3, 0.5f);

        // Handle events
        checkBox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (boolean) event.getProperty().getValue();
                btnMinus.setEnabled(value);
                btnPlus.setEnabled(value);
                orderDetailExtension.setEnable(value);
                if (value == true && foodNumber.getValue().equals("")) {
                    foodNumber.setValue(1 + "");
                    foodNumber.setImmediate(true);
                    orderDetailExtension.getOrderDetailRecord().setQuantity(1);
                }
            }
        });

        btnMinus.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (!foodNumber.getValue().equals("")) {
                    Integer number = Integer.parseInt(foodNumber.getValue());
                    if (number > 1) {
                        foodNumber.setValue(number - 1 + "");
                        foodNumber.setImmediate(true);
                        orderDetailExtension.getOrderDetailRecord().setQuantity(
                                number - 1);
                    }
                }
            }
        });

        btnPlus.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (!foodNumber.getValue().equals("")) {
                    Integer number = Integer.parseInt(foodNumber.getValue());
                    foodNumber.setValue(1 + number + "");
                    foodNumber.setImmediate(true);
                    orderDetailExtension.getOrderDetailRecord().setQuantity(
                            number + 1);
                } else {
                    foodNumber.setValue(1 + "");
                    foodNumber.setImmediate(true);
                    orderDetailExtension.getOrderDetailRecord().setQuantity(1);
                }
            }
        });

        orderDetailExtensionList.add(orderDetailExtension);
    }

    private Component buildFooter() {
        CssLayout footer = new CssLayout();
        footer.setWidth("100%");

        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR + " TEXT_CENTER");

        Button btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_HUGE);
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

                        // If id of records are matched and change flag different from READONLY
                        for (OrderDetailRecord record : orderDetailRecordList) {
                            if (orderDetailExtension.getOrderDetailRecord().getFoodId().equals(record.getFoodId())
                                    && record.getChangeFlag() != ChangedFlag.READONLY) {
                                shouldCreateNewOne = false;
                                ChangedFlag currentFlag = record.getChangeFlag();
                                if( currentFlag == ChangedFlag.UNMODIFIED ) {
                                    record.setChangeFlag(ChangedFlag.MODIFIED);
                                    record.setQuantity(record.getQuantity() + orderDetailExtension.getOrderDetailRecord().getQuantity());
                                    record.setStatus(State.WAITING);
                                } else if( currentFlag == ChangedFlag.ADDNEW ) {
                                    record.setQuantity(record.getQuantity() + orderDetailExtension.getOrderDetailRecord().getQuantity());
                                    record.setStatus(State.WAITING);
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
