package com.luvsoft.MMI.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.order.OrderDetailRecord.ChangedFlag;
import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.threads.NewOrderManager;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.OrderDetail;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author cuongvm
 *
 * This class handle transform data between two table (src --> dest)
 *  - Split table
 *  - Merge two table
 */
public class ChangeTable extends Window implements ViewInterface{
    /**
     * 
     */
    private static final long serialVersionUID = 3832560424462749922L;
    private Order srcOrder; // Source order
    private Order destOrder; // destination order

    private List<OrderDetailRecordExtension> odDetailRecordExtList; // Order detail record extension list
    private List<CheckBox> cbList;
    private NativeSelect nsDestTables;
    private List<Table> tblList;
    private Table destTable;
    private Table srcTable;
    
    private boolean isMoveWholeTable;
    private boolean hasRecordTransfer;

    public ChangeTable(Order order){
        srcOrder = order;
        destOrder = null;

        tblList = new ArrayList<Table>();
        isMoveWholeTable = false;
        hasRecordTransfer = false;
        
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();
        addStyleName("close-style");

        createView();
    }

    @SuppressWarnings("serial")
    @Override
    public void createView() {
        OrderInfo orderInfo = Adapter.retrieveOrderInfo(srcOrder);
        odDetailRecordExtList = new ArrayList<OrderDetailRecordExtension>();
        // Table name
        Label lblTblName = new Label(orderInfo.getTableName());
        lblTblName.addStyleName("FONT_TAHOMA TEXT_CENTER BACKGROUND_BLUE TEXT_WHITE");
        lblTblName.addStyleName(ValoTheme.LABEL_BOLD);
        lblTblName.addStyleName(ValoTheme.LABEL_HUGE);
        lblTblName.setWidth("100%");

        // Checkbox to select all order detail
        CheckBox cbSelectAll = new CheckBox("Chọn hết"); 
        cbSelectAll.addStyleName(ValoTheme.CHECKBOX_LARGE);
        cbSelectAll.setSizeFull();
        cbSelectAll.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean isChecked = (boolean)event.getProperty().getValue();
                // Check all the check boxs if this checkbox is checked
                // Otherwise, uncheck all checkbox
                for(CheckBox checkBox : cbList){
                    checkBox.setValue(isChecked);
                }
                
            }
        });

        // Combobox destination table name
        nsDestTables = new NativeSelect(Language.MOVE_TO);
        nsDestTables.setNullSelectionAllowed(false);
        nsDestTables.setInvalidAllowed(false);
        nsDestTables.setRequired(true);
        nsDestTables.addStyleName("FONT_X_LARGE");
        List<Floor> floorList = Adapter.retrieveFloorList();
        if( floorList == null || floorList.isEmpty() ){
            System.out.println("No floor exist!");
            return;
        }
        for( Floor floor : floorList ){
            List<Table> list = Adapter.retrieveTableList(floor.getTableIdList());
            tblList.addAll(list); // save table list
            if(list != null){
                for( Table table : list ){
                    nsDestTables.addItem(Language.TABLE + " " + table.getNumber());
                }
            }
        }
        nsDestTables.removeItem(orderInfo.getTableName()); // Should not change to itself
        // set default selected dest table is the first one
        if( !tblList.isEmpty() ){
            nsDestTables.setValue(Language.TABLE + " " + tblList.get(0).getNumber());
        }


        HorizontalLayout hzLayout = new HorizontalLayout();
        hzLayout.setWidth("100%");
        hzLayout.addComponents(cbSelectAll, nsDestTables);
        hzLayout.setComponentAlignment(cbSelectAll, Alignment.MIDDLE_LEFT);
        hzLayout.setComponentAlignment(nsDestTables, Alignment.TOP_RIGHT);

        cbList = new ArrayList<CheckBox>();

        // Grid to hold the order detail content
        GridLayout gridContent = new GridLayout();
        gridContent.addStyleName("large-checkbox grid-layout-custom");
        gridContent.setColumns(3);
        gridContent.setWidth("100%");
        gridContent.setSpacing(true);

        // build grid
        for( OrderDetailRecord record : orderInfo.getOrderDetailRecordList() ){
            // build grid line
            buildGridLine(record, gridContent);
        }
        
        // Create panel to contain grid content
        Panel panelContent = new Panel(gridContent);
        panelContent.setSizeFull();

        // Footer
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.setHeightUndefined();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

        // Confirm button
        Button btnConfirm = new Button(Language.CONFIRM);
        btnConfirm.addStyleName(ValoTheme.BUTTON_HUGE);
        btnConfirm.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnConfirm.addStyleName("margin-bottom1");
        btnConfirm.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialog.show( getUI(), Language.CONFIRM_CHANGE_TABLE, Language.CONFIRM_CHANGE_TABLE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    // do change table
                                    doChangeTable();

                                    // Close window
                                    ChangeTable.this.close();
                                }
                            }
                });
            }
        });
        footer.addComponent(btnConfirm);
        footer.setComponentAlignment(btnConfirm, Alignment.MIDDLE_CENTER);

        VerticalLayout cotainerLayout = new VerticalLayout();
        cotainerLayout.setSizeFull();
        cotainerLayout.addComponents(lblTblName, hzLayout, panelContent, footer);
        cotainerLayout.setExpandRatio(lblTblName, 1.0f);
        cotainerLayout.setExpandRatio(hzLayout, 1.0f);
        cotainerLayout.setExpandRatio(panelContent, 6.0f);
        cotainerLayout.setExpandRatio(footer, 1.0f);

        this.setContent(cotainerLayout);
    }

    @SuppressWarnings("serial")
    private void buildGridLine(OrderDetailRecord record, GridLayout grid){
        OrderDetailRecordExtension rc = new OrderDetailRecordExtension(record, false);

        Label foodName = new Label();
        foodName.addStyleName(ValoTheme.LABEL_HUGE + " " + ValoTheme.LABEL_BOLD + " FONT_TAHOMA TEXT_BLUE");
        foodName.setValue(record.getFoodName());

        CheckBox checkBox = new CheckBox();
        checkBox.addStyleName(ValoTheme.CHECKBOX_LARGE);
        checkBox.setSizeFull();
        cbList.add(checkBox);

        NativeSelect cbNumberList = new NativeSelect();
        cbNumberList.setNullSelectionAllowed(false);
        cbNumberList.setResponsive(true);
        cbNumberList.setHeight("40px");
        cbNumberList.addStyleName("FONT_X_LARGE");

        for(int i = 1; i <= record.getQuantity(); i++) {
            cbNumberList.addItem(i);
        }
        cbNumberList.setValue(record.getQuantity());
        cbNumberList.setEnabled(false);

        cbNumberList.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                rc.getOrderDetailRecord().setQuantity( (int)event.getProperty().getValue() );
            }
        });

        checkBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                cbNumberList.setEnabled((boolean)event.getProperty().getValue());
                rc.setEnable((boolean)event.getProperty().getValue());
            }
        });

        grid.addComponents(checkBox, foodName, cbNumberList);
        grid.setColumnExpandRatio(0, 0.5f);
        grid.setColumnExpandRatio(1, 5.0f);
        grid.setColumnExpandRatio(2, 0.5f);
        grid.setComponentAlignment(cbNumberList, Alignment.MIDDLE_CENTER);

        odDetailRecordExtList.add(rc);
    }

    public void doChangeTable(){
        if( srcOrder == null ){
            System.out.println("Unexpected case, Src order is null...");
            return;
        }
        
        // Get order of dest table
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.PAID);
        states.add(Types.State.CANCELED);
        List<Order> orderList = Adapter.getOrderListIgnoreStates(states, null, null);
        destOrder = null;
        destTable = null;
        for( Table table : tblList ){
            if( nsDestTables.getValue().equals(Language.TABLE + " " + table.getNumber()) ){
                destTable = table;
                break;
            }
        }

        // sanity check
        if( destTable == null ){
            System.out.println("Unexpected case, dest Table not exist!");
            return;
        }

        for (Order order : orderList) {
            if( order.getTableId().equals(destTable.getId()) ) {
                destOrder = order;
                break;
            }
        }

        // Try to split tempOrder (source order)
        OrderInfo orderInfo = Adapter.retrieveOrderInfo(srcOrder);
        List<OrderDetailRecord> srcRecordList = orderInfo.getOrderDetailRecordList();
        // back up the order detail list
        List<String> orderDetailsBackup = new ArrayList<String>();
        orderDetailsBackup.addAll(srcOrder.getOrderDetailIdList());

        // split from source
        splitOrder(srcOrder, srcRecordList, odDetailRecordExtList);

        // No change
        if( !hasRecordTransfer ){
            System.out.println("No order detail was transfered...");
            return;
        }

        // Move whole table
        if( isMoveWholeTable ){
            // Move whole table to an empty table
            if( destOrder == null){
                destOrder = new Order(srcOrder);
                System.out.println("Move to table " + destTable.getNumber());

                // Move dest tableId to src order
                destOrder.setTableId(destTable.getId());
                
                System.out.println("Src table: " + srcOrder.getTableId());
                System.out.println("Des table: " + destOrder.getTableId());
                Adapter.updateFieldValueOfOrder(destOrder.getId(), Order.DB_FIELD_NAME_TABLE_ID, destTable.getId());

                // Broadcast order change event
                Broadcaster.broadcast(CoffeeshopUI.PERFORM_SWITCH_TABLE + "::" + srcTable.getId() + "::"
                        + destTable.getId() + "::" + srcOrder.getId() + "::" + srcOrder.getId());
            }
            else{
                // Move whole table to a table that already has an order
                // Append the order detail id
                destOrder.getOrderDetailIdList().addAll(orderDetailsBackup);

                // move WAITING order to dest order
                // if dest order state is differ from WAITING, set it to WAITING
                if( !destOrder.getStatus().equals(Types.State.WAITING) &&
                        srcOrder.getStatus().equals(Types.State.WAITING) ){
                        destOrder.setStatus(Types.State.WAITING);
                }

                // save to db
                if( Adapter.updateOrder(destOrder) ){
                    // Remove source order
                    Adapter.removeOrder(srcOrder.getId());
                }

                // Broadcast order change event
                Broadcaster.broadcast(CoffeeshopUI.PERFORM_SWITCH_TABLE + "::" + srcTable.getId() + "::"
                        + destTable.getId() + "::" + srcOrder.getId() + "::" + destOrder.getId());
                srcOrder.setStatus(Types.State.CANCELED);
                NewOrderManager.interruptWaitingOrderThread(srcOrder);
            }
            // set table state to map to current order
            srcOrder.setStatus(Types.State.CANCELED);
            CoffeeTableElement.updateTableState(srcTable, srcOrder);
            CoffeeTableElement.updateTableState(destTable, destOrder);
            NewOrderManager.onOrderStateChange(destOrder);
            return;
        }

        // Move part of table
        // Update source order
        if( Adapter.updateOrder(srcOrder) ){
            // Update order details
            // Change quantity
            if( srcRecordList != null ){
                for( OrderDetailRecord record : srcRecordList ){
                    if( record.getChangeFlag().equals(ChangedFlag.MODIFIED) ){
                        Adapter.updateFieldValueOfOrderDetail(record.getOrderDetailId(),
                                    OrderDetail.DB_FIELD_NAME_QUANTITY,
                                    record.getQuantity());
                    }
                }
            }
        }

        // If there's no order is dest table, create a new one
        if( destOrder == null ){
            destOrder = new Order();
            destOrder.setId((new ObjectId()).toString());
            destOrder.setCreatingTime(new Date());
            destOrder.setTableId(destTable.getId());
            destOrder.setStatus(srcOrder.getStatus());
            if( !Adapter.addNewOrder(destOrder) ){
                System.out.println("Fail to add new order!");
            }
        }

        // append order detail list to dest order
        appendOrder(srcOrder, destOrder, odDetailRecordExtList);

        // set table state to map to current order
        CoffeeTableElement.updateTableState(srcTable, srcOrder);
        CoffeeTableElement.updateTableState(destTable, destOrder);

        // Thread waiting time
        NewOrderManager.onOrderStateChange(srcOrder);
        NewOrderManager.onOrderStateChange(destOrder);

        // Broadcast order change event
        Broadcaster.broadcast(CoffeeshopUI.PERFORM_SWITCH_TABLE + "::" + srcTable.getId() + "::"
                + destTable.getId() + "::" + srcOrder.getId() + "::" + destOrder.getId());
    }
    
    /**
     * Split recordExt
     * @param inOrder - order to be split
     * @param srcRecordList record list of inOrder
     * @param odDetailRcdExtList - list of order detail record extension use to split
     * @return
     */
    public boolean splitOrder(Order inOrder, List<OrderDetailRecord> srcRecordList, List<OrderDetailRecordExtension> odDetailRcdExtList){
        for( OrderDetailRecordExtension recordExt : odDetailRcdExtList ){
            for( int index = 0; index < srcRecordList.size(); index++ ){
                // Check if OrderdetailId exist and is selected by user
                OrderDetailRecord srcRecord = srcRecordList.get(index);
                if( srcRecord.getOrderDetailId().equals(recordExt.getOrderDetailRecord().getOrderDetailId())
                   && recordExt.isEnable()){
                    // Split quantity
                    int remainQuantity = srcRecord.getQuantity() - recordExt.getOrderDetailRecord().getQuantity();
                    if(remainQuantity == 0){
                        // Move all quantity
                        // Update record list
                        srcRecordList.remove(srcRecord);

                        // Update order detail list
                        inOrder.getOrderDetailIdList().remove(srcRecord.getOrderDetailId());
                    }
                    else{
                        // Move a part of source record
                        srcRecord.setQuantity(remainQuantity);
                        srcRecord.setChangeFlag(ChangedFlag.MODIFIED);

                        // Tell other object that this recordExt is a part of other record (in other order)
                        recordExt.getOrderDetailRecord().setChangeFlag(ChangedFlag.MODIFIED);
                    }
                    hasRecordTransfer = true;
                    break;
                }
            }
        }

        boolean isAllOrderDetailsCanceled = true;
        boolean hasWaitingOrderDetail = false;
        for( int i=0;i<srcRecordList.size(); i++ ){
            if( srcRecordList.get(i).getStatus().equals(Types.State.WAITING) ){
                hasWaitingOrderDetail = true;
            }

            if( !srcRecordList.get(i).getStatus().equals(Types.State.CANCELED) ){
                isAllOrderDetailsCanceled = false;
            }
        }

        // No order detail left
        // If all the remaining order detail are canceled
        if( srcRecordList.isEmpty() || isAllOrderDetailsCanceled){
            // ignore this order
            //inOrder.setStatus(Types.State.CANCELED);
            isMoveWholeTable = true;
        }
        else if( !hasWaitingOrderDetail ){
            // If there's no waiting order detail
            // Mark order as unpaid
            inOrder.setStatus(Types.State.UNPAID);
        }

        return true;
    }

    /**
     * Append odDetailRcdExtList to source Order
     * Expect all orderdetail in subOrder have state sifferent from CANCELED
     * @param desOder - order to be merged
     * @param odDetailRcdExtList - list of order detail record extension use to append
     * @return
     */
    public boolean appendOrder(Order srcOrder, Order desOder, List<OrderDetailRecordExtension> odDetailRcdExtList){
        for( OrderDetailRecordExtension recordExt : odDetailRcdExtList ){
            if( recordExt.isEnable() && recordExt.getOrderDetailRecord().getQuantity() > 0 ){
                // This record is a part of an other order detail
                // Create new order detail and save to db
                if( recordExt.getOrderDetailRecord().getChangeFlag().equals(ChangedFlag.MODIFIED) ){
                    ObjectId odId = new ObjectId();
                    OrderDetail newOd = new OrderDetail();
                    newOd.setId(odId.toString());
                    newOd.setFoodId(recordExt.getOrderDetailRecord().getFoodId());
                    newOd.setQuantity(recordExt.getOrderDetailRecord().getQuantity());
                    newOd.setState(recordExt.getOrderDetailRecord().getStatus());

                    if( Adapter.addNewOrderDetail(newOd) ){
                        desOder.getOrderDetailIdList().add(odId.toString());
                        Adapter.updateFieldValueOfOrder(desOder.getId(),
                                Order.DB_FIELD_NAME_ORDER_DETAIL_LIST,
                                Types.formatListToString(desOder.getOrderDetailIdList()));
                    }
                }
                else{
                    // Just append the order detail to this order
                    desOder.getOrderDetailIdList().add(recordExt.getOrderDetailRecord().getOrderDetailId());
                    Adapter.updateFieldValueOfOrder(desOder.getId(),
                            Order.DB_FIELD_NAME_ORDER_DETAIL_LIST,
                            Types.formatListToString(desOder.getOrderDetailIdList()));
                }
            }
        }

        boolean hasWaitingOrderDetail = false;
        for( int i=0;i<odDetailRcdExtList.size(); i++ ){
            if( odDetailRcdExtList.get(i).isEnable() ){
                if( odDetailRcdExtList.get(i).getOrderDetailRecord().getStatus().equals(Types.State.WAITING) ){
                    hasWaitingOrderDetail = true;
                }
            }
        }

        // Current order not in waiting state and new waiting order detail was added
        // Change order state to WAITING
        if( hasWaitingOrderDetail && !desOder.getStatus().equals(Types.State.WAITING)){
            desOder.setStatus(Types.State.WAITING);
            if( Adapter.changeOrderState(desOder.getId(), Types.State.WAITING) ){
                System.out.println(" Change ORDER state to WAITING");
            }
        }

        return true;
    }

    @Override
    public void reloadView() {
        // TODO Auto-generated method stub
        
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

    public Table getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(Table srcTable) {
        this.srcTable = srcTable;
    }
}
