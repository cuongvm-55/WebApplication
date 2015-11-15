package com.luvsoft.MMI;

import java.util.Date;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.report.ReportRendering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
        
        Button btnCreateReport = new Button(Language.CREATE_REPORT);
        btnCreateReport.addStyleName("customizationButton");
        btnCreateReport.addStyleName(ValoTheme.BUTTON_HUGE);
        btnCreateReport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if( toDate.getValue().after(fromDate.getValue()) || toDate.getValue().equals(fromDate.getValue()) )
                {
                    System.out.println("From Date: " + fromDate.getValue().toString());
                    System.out.println("To Date: " + toDate.getValue().toString());
                    
                    // Export data
                    ReportRendering.export();
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
}
