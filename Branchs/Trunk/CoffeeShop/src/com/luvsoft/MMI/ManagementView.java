package com.luvsoft.MMI;

import java.util.Date;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.Order.AddFood;
import com.luvsoft.MMI.management.FoodManagement;
import com.luvsoft.MMI.report.OrderInfoProducer;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.PopupView.PopupVisibilityListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author datnq.55
 *
 * This class is used to implement Management View
 */
@SuppressWarnings("serial")
public class ManagementView extends VerticalLayout{
    private Button btnStatistic;
    private Button btnFoodManagement;
    private Button btnConfiguration;
    private PopupView  datePickerPopup;

    public ManagementView() {
        // TODO Add your components at here
        btnStatistic = new Button();
        btnFoodManagement = new Button();
        btnConfiguration = new Button();

        setupUI();
    }
    
    private void setupUI()
    {
        
        btnStatistic.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        btnStatistic.setCaption(Language.STATISTIC);
        btnStatistic.setWidth("50%");
        btnStatistic.addStyleName("margintop100");
        btnStatistic.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                datePickerPopup.setPopupVisible(true);
            }
        });
        btnFoodManagement.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        btnFoodManagement.setCaption(Language.FOOD_MANAGEMENT);
        btnFoodManagement.setWidth("50%");
        btnFoodManagement.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().addWindow(new FoodManagement());
            }
        });

        btnConfiguration.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        btnConfiguration.setCaption(Language.CONFIGURATION);
        btnConfiguration.setWidth("50%");
        
        this.addComponents(btnStatistic, btnFoodManagement, btnConfiguration);
        this.setSpacing(true);
        this.setComponentAlignment(btnStatistic, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnFoodManagement, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnConfiguration, Alignment.MIDDLE_CENTER);
        
        createDatePickerPopup();
    }
    
    private void createDatePickerPopup(){
        Label lblChooseDate = new Label(Language.CHOOSE_DATE);
        Label lblFromDate = new Label(Language.FROM_DATE);
        Label lblToDate = new Label(Language.TO_DATE);

        DateField fromDate = new DateField();
        DateField toDate = new DateField();
        HorizontalLayout frLayout = new HorizontalLayout();
        frLayout.addComponents(lblFromDate, fromDate);
        frLayout.setComponentAlignment(lblFromDate, Alignment.MIDDLE_LEFT);
        frLayout.setComponentAlignment(fromDate, Alignment.MIDDLE_RIGHT);
        fromDate.setValue(new Date());
        fromDate.setRangeEnd(new Date());
        fromDate.setDateFormat("dd-MM-yyyy");

        HorizontalLayout toLayout = new HorizontalLayout();
        toLayout.addComponents(lblToDate, toDate);
        toLayout.setComponentAlignment( lblToDate, Alignment.MIDDLE_LEFT);
        toLayout.setComponentAlignment(toDate, Alignment.MIDDLE_RIGHT);
        toDate.setValue(new Date());
        toDate.setRangeEnd(new Date());
        toDate.setDateFormat("dd-MM-yyyy");

        Button btnClearData = new Button(Language.CLEAR_DATA);
        btnClearData.addStyleName("customizationButton");
        btnClearData.addStyleName(ValoTheme.BUTTON_HUGE);
        btnClearData.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Remove data...");
                // close popup
                datePickerPopup.setPopupVisible(false);
                ConfirmDialog.show( getUI(), Language.CONFIRM_DELETE_TITLE, Language.CONFIRM_DELETE_CONTENT,
                        Language.ASK_FOR_CONFIRM, Language.ASK_FOR_DENIED, new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    System.out.println("user confirmed");
                                    if( removeOrderDataInDateRange(fromDate.getValue(), toDate.getValue()) ){
                                     // notify message
                                        Notification notify = new Notification("<b>Thông báo</b>",
                                                "<i>Đã xóa dữ liệu thành công!</i>",
                                                Notification.Type.TRAY_NOTIFICATION  , true);
                                        notify.setPosition(Position.BOTTOM_RIGHT);
                                        notify.show(Page.getCurrent());
                                    }
                                    else{
                                        System.out.println("Fail to remove data...");
                                    }
                                } else {
                                    System.out.println("user canceled, do nothing!");
                                }
                            }
                        });
            }
        });

        Button btnCreateReport = new Button(Language.CREATE_REPORT);
        btnCreateReport.addStyleName("customizationButton");
        btnCreateReport.addStyleName(ValoTheme.BUTTON_HUGE);
        btnCreateReport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Date begDate = fromDate.getValue();
                Date endDate = toDate.getValue();
                if( begDate != null && endDate != null &&
                        endDate.after(begDate) || endDate.equals(begDate) )
                {
                    System.out.println("Export report...");
                    System.out.println("From Date: " + begDate.toString());
                    System.out.println("To Date: " + endDate.toString());
                    
                    // Export data
                    OrderInfoProducer producer = new OrderInfoProducer(begDate, endDate);
                    if( producer.export() ){
                        // close popup
                        datePickerPopup.setPopupVisible(false);

                        // notify message
                        Notification notify = new Notification("<b>Thông báo</b>",
                                "<i>Báo cáo đã xuất thành công!</i>",
                                Notification.Type.TRAY_NOTIFICATION  , true);
                        notify.setPosition(Position.BOTTOM_RIGHT);
                        notify.show(Page.getCurrent());
                    }
                    else{
                        System.out.println("Fail to export report");
                    }
                }
                else{
                    System.out.println("Invalid date range!");
                }
            }
        });

        HorizontalLayout control = new HorizontalLayout();
        control.addComponents(btnClearData, btnCreateReport);
        control.setSpacing(true);
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponents(lblChooseDate, frLayout, toLayout, control);
        layout.setComponentAlignment(lblChooseDate, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(frLayout, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(toLayout, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(control, Alignment.MIDDLE_CENTER);

        layout.setSpacing(true);
        datePickerPopup = new PopupView(null, layout);
        datePickerPopup.setStyleName("popupStyle");
        datePickerPopup.setPopupVisible(false);
        datePickerPopup.setHideOnMouseOut(false);
        datePickerPopup.addPopupVisibilityListener(new PopupVisibilityListener() {
            
            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                // TODO Auto-generated method stub
                if( !event.isPopupVisible() ) {
                }
            }
        });
        this.addComponent(datePickerPopup);
    }

    private boolean removeOrderDataInDateRange(Date fromDate, Date toDate){
        boolean bOk = true;
        if(fromDate != null && toDate != null &&
           toDate.after(fromDate) || toDate.equals(fromDate) ){
            // Get all order in date range
            List<Order> orderList = Adapter.getOrderListWithState(Types.State.PAID, fromDate, toDate);

            if(orderList == null || orderList.isEmpty() ){
                // Stop here, no data to remove
                System.out.println("No order to remove!");
                return true;
            }

            for( Order order : orderList ){
                // Delete all order details
                List<String> odDetailList = order.getOrderDetailIdList();
                if( odDetailList == null || odDetailList.isEmpty() ){
                    // No order detail to remove
                    System.out.println("No order detail to remove!, orderId" + order.getId());
                    continue;
                }
                for( String odDetailId : order.getOrderDetailIdList() ){
                    bOk &= Adapter.removeOrderDetail(odDetailId);
                }

                // Remove order
                bOk &= Adapter.removeOrder(order.getId());
            }
        }
        return bOk;
    }
}
