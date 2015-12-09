package com.luvsoft.MMI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.luvsoft.MMI.management.ConfigForm;
import com.luvsoft.MMI.management.FoodManagement;
import com.luvsoft.MMI.management.TableManagement;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
    private Button btnTableManagement;
    private Button btnConfiguration;

    public ManagementView() {
        // TODO Add your components at here
        btnStatistic = new Button();
        btnFoodManagement = new Button();
        btnConfiguration = new Button();
        btnTableManagement = new Button();
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
                Window window = new Window();
                window.setModal(true);
                window.setResizable(false);
                window.setDraggable(false);
                window.setWidth("400px");
                window.setHeight("300px");
                window.center();
                window.setContent(createDatePickerLayout());
                getUI().addWindow(window);
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

        
        btnTableManagement.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        btnTableManagement.setCaption(Language.TABLE_MANAGEMENT);
        btnTableManagement.setWidth("50%");
        btnTableManagement.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().addWindow(new TableManagement());
            }
        });

        btnConfiguration.setStyleName("customizationButton FULL_SIZE FONT_OVERSIZE");
        btnConfiguration.setCaption(Language.CONFIGURATION);
        btnConfiguration.setWidth("50%");
        btnConfiguration.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().addWindow(new ConfigForm(Adapter.getConfiguration()));
            }
        });
        
        this.addComponents(btnStatistic, btnFoodManagement, btnTableManagement, btnConfiguration);
        this.setSpacing(true);
        this.setComponentAlignment(btnStatistic, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnFoodManagement, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnTableManagement, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(btnConfiguration, Alignment.MIDDLE_CENTER);
    }
    
    private VerticalLayout createDatePickerLayout(){
        Label lblChooseDate = new Label(Language.CHOOSE_DATE);
        lblChooseDate.addStyleName(ValoTheme.LABEL_BOLD);
        lblChooseDate.addStyleName(ValoTheme.LABEL_HUGE);
        lblChooseDate.addStyleName("TEXT_CENTER");
        Label lblFromDate = new Label(Language.FROM_DATE);
        Label lblToDate = new Label(Language.TO_DATE);

        DateField fromDate = new DateField();
        DateField toDate = new DateField();
        HorizontalLayout frLayout = new HorizontalLayout();
        //frLayout.setSizeFull();
        frLayout.addComponents(lblFromDate, fromDate);
        frLayout.setExpandRatio(lblFromDate, 4.0f);
        frLayout.setExpandRatio(fromDate, 6.0f);
        frLayout.setComponentAlignment(lblFromDate, Alignment.MIDDLE_CENTER);
        frLayout.setComponentAlignment(fromDate, Alignment.MIDDLE_CENTER);
        fromDate.setValue(new Date());
        fromDate.setRangeEnd(new Date());
        fromDate.setDateFormat(Language.DATE_TIME_FORMAT_DATE_ONLY);
        frLayout.setSpacing(true);

        HorizontalLayout toLayout = new HorizontalLayout();
        //toLayout.setSizeFull();
        toLayout.addComponents(lblToDate, toDate);
        toLayout.setExpandRatio(lblToDate, 4.0f);
        toLayout.setExpandRatio(toDate, 6.0f);
        toLayout.setComponentAlignment( lblToDate, Alignment.MIDDLE_CENTER);
        toLayout.setComponentAlignment(toDate, Alignment.MIDDLE_CENTER);
        toLayout.setSpacing(true);
        toDate.setValue(new Date());
        toDate.setRangeEnd(new Date());
        toDate.setDateFormat(Language.DATE_TIME_FORMAT_DATE_ONLY);

        Button btnClearData = new Button(Language.CLEAR_DATA);
        btnClearData.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnClearData.addStyleName(ValoTheme.BUTTON_HUGE);
        btnClearData.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                System.out.println("Remove data...");
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
        btnCreateReport.addStyleName(ValoTheme.BUTTON_PRIMARY);
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
        return layout;
    }

    private boolean removeOrderDataInDateRange(Date fromDate, Date toDate){
        boolean bOk = true;
        if(fromDate != null && toDate != null &&
           toDate.after(fromDate) || toDate.equals(fromDate) ){
            // Get all order in date range
            List<Types.State> states = new ArrayList<Types.State>();
            states.add(Types.State.PAID);
            states.add(Types.State.CANCELED);
            List<Order> orderList = Adapter.getOrderListWithStates(states, fromDate, toDate);

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
