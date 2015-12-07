package com.luvsoft.MMI.management;

import java.io.File;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Configuration;
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
public class ConfigForm extends Window{
    private PropertysetItem configItem;
    private Configuration config;
    public ConfigForm(Configuration _config){
        this.config = _config;
        this.setCaption(Language.CONFIGURATION);
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("340px");
        this.setHeight("240px");
        this.center();

        configItem = new PropertysetItem();
        configItem.addItemProperty("supPincode", new ObjectProperty<String>(config.getSupPincode()));
        configItem.addItemProperty("reportOutputDir", new ObjectProperty<String>(config.getReportOutputDir()));

        TextField pincodeField = new TextField(Language.SUP_PINCODE);
        pincodeField.setRequired(true);
        pincodeField.focus();

        TextField dirField = new TextField(Language.REPORT_OUTPUT_DIR);
        dirField.setRequired(true);

        // Now create the binder and bind the fields
        FieldGroup fieldGroup = new FieldGroup(configItem);
        fieldGroup.bind(pincodeField, "supPincode");
        fieldGroup.bind(dirField, "reportOutputDir");
        fieldGroup.setBuffered(true);
        fieldGroup.addCommitHandler(new CommitHandler() {
            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                // validate data
                if( pincodeField.getValue().equals("") && dirField.getValue().equals("") ){
                    throw new CommitException("All fields are required!");
                }
                else if(pincodeField.getValue().length() != Configuration.PINCODE_LENGTH){
                    throw new CommitException("Invalid pincode!");
                }
                
                // Check report output directory
                File theDir = new File(dirField.getValue());
                if( !theDir.exists() ){
                    throw new CommitException("Invalid directory!");
                }
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                // save data to database
                PropertysetItem item = (PropertysetItem)commitEvent.getFieldBinder().getItemDataSource();
                config.setSupPincode(item.getItemProperty("supPincode").getValue().toString());
                config.setReportOutputDir(item.getItemProperty("reportOutputDir").getValue().toString());
                if( Adapter.updateConfiguration(config) ){
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
                close();
            }
        });
        HorizontalLayout hzLayout = new HorizontalLayout();
        //hzLayout.setSizeFull();
        hzLayout.addComponents(btnSave, btnCancel);
        hzLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        hzLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
        hzLayout.setSpacing(true);

        VerticalLayout vtcLayout = new VerticalLayout();
        vtcLayout.setSizeFull();
        vtcLayout.addComponents(pincodeField, dirField, hzLayout);
        vtcLayout.setComponentAlignment(pincodeField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(dirField, Alignment.MIDDLE_CENTER);
        vtcLayout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);
        vtcLayout.setSpacing(true);
        this.setContent(vtcLayout);
    }
}
