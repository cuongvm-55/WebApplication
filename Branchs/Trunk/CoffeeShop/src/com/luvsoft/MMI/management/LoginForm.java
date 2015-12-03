package com.luvsoft.MMI.management;

import com.luvsoft.MMI.MainView;
import com.luvsoft.MMI.ManagementView;
import com.luvsoft.MMI.utils.Language;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginForm extends Window{
    private static final int PINCODE_LENGTH = 5;
    private VerticalLayout layout;
    private PasswordField tfPincode;
    private Label lblMsg;

    private MainView parent;
    public LoginForm( MainView parent ){
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("330px");
        this.setHeight("220px");
        this.center();

        this.parent = parent;
        createView();
    }

    public void createView() {
        Label lblLogin = new Label(Language.LOGIN);
        lblLogin.setStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblLogin.setWidth("100%");

        HorizontalLayout hzLayout = new HorizontalLayout();
        Label lblPincode = new Label(Language.PINCODE);
        tfPincode = new PasswordField();
        tfPincode.setMaxLength(PINCODE_LENGTH);
        hzLayout.addComponents(lblPincode, tfPincode);
        hzLayout.setComponentAlignment(lblPincode, Alignment.MIDDLE_CENTER);
        hzLayout.setComponentAlignment(tfPincode, Alignment.MIDDLE_CENTER);
        hzLayout.setSpacing(true);

        HorizontalLayout hzLayoutBtn = new HorizontalLayout();
        Button btnLogin = new Button(Language.LOGIN);
        btnLogin.addStyleName(ValoTheme.BUTTON_HUGE);
        btnLogin.addStyleName("customizationButton");
        btnLogin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                login();
            }
        });
        hzLayoutBtn.addComponent(btnLogin);
        hzLayoutBtn.setComponentAlignment(btnLogin, Alignment.MIDDLE_CENTER);

        lblMsg = new Label("");
        lblMsg.addStyleName("TEXT_CENTER");
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponents(lblLogin, hzLayout, lblMsg, hzLayoutBtn);
        layout.setComponentAlignment(lblLogin, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(lblMsg, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(hzLayoutBtn, Alignment.MIDDLE_CENTER);
        layout.setExpandRatio(lblLogin, 1.0f);
        layout.setExpandRatio(hzLayout, 4f);
        layout.setExpandRatio(lblMsg, 1f);
        layout.setExpandRatio(hzLayoutBtn, 1.5f);
        
        this.setContent(layout);
    }

    private void login(){
        String pincode = tfPincode.getValue();
        if( pincode.length() == PINCODE_LENGTH
            && pincode.equals("17930")){
            parent.getMainLayout().addComponent(new ManagementView());
            close();
        }
        else{
            lblMsg.addStyleName("TEXT_RED");
            lblMsg.setValue(Language.INVALID_PINCODE);
        }
    }
}
