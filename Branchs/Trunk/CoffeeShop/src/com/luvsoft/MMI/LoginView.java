package com.luvsoft.MMI;

import com.luvsoft.MMI.utils.Language;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class LoginView extends CustomComponent implements View, Button.ClickListener{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final TextField user;

    private final PasswordField password;

    private final Button loginButton;

    private final VerticalLayout fields;
    public LoginView() {
        setSizeFull();

        Label lblWelcome = new Label("Chào Mừng Bạn Đã Đến Với Phần Mềm Quản Lý Huyền Coffee");
        lblWelcome.addStyleName(ValoTheme.LABEL_HUGE);
        lblWelcome.addStyleName("TEXT_CENTER");
        // Create the user input field
        user = new TextField("Nhập tên của bạn:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInvalidAllowed(false);
        user.addValidator(new StringLengthValidator("", 1, 256, false));

        // Create the password input field
        password = new PasswordField("Mật khẩu:");
        password.setWidth("300px");
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");
        //password.addValidator(new PasswordValidator());

        // Create login button
        loginButton = new Button("Đăng Nhập");
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(KeyCode.ENTER, null);
        loginButton.addClickListener(this);

        // Add both to a panel
        fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption(Language.LOGIN);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(lblWelcome, fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(lblWelcome, Alignment.BOTTOM_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        viewLayout.setExpandRatio(fields, 1.0f);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        user.focus();
    }

    public void login() {
        if (!user.isValid() || !password.isValid()) {
            fields.setCaption(Language.ALL_FIELDS_ARE_REQUIRED);
            return;
        }

        String username = user.getValue();
        String password = this.password.getValue();
        if (Adapter.loginOperatorReq(password)) {
            // Store the current user in the service session
            getSession().setAttribute("user", username);

            // Navigate to main view
            UI.getCurrent().getNavigator().navigateTo(CoffeeshopUI.MAIN_VIEW);

        } else {
            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            this.password.focus();
            fields.setCaption(Language.INVALID_PINCODE);
        }
    }

//    // Validator for validating the passwords
//    @SuppressWarnings("serial")
//    private static final class PasswordValidator extends
//            AbstractValidator<String> {
//
//        public PasswordValidator() {
//            super("The password provided is not valid");
//        }
//
//        @Override
//        protected boolean isValidValue(String value) {
//            if (value != null
//                    && (value.length() != Configuration.PINCODE_LENGTH || !value.matches(".*\\d.*"))) {
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        public Class<String> getType() {
//            return String.class;
//        }
//        
//        @Override
//        public String getErrorMessage() {
//            // TODO Auto-generated method stub
//            return Language.INVALID_PINCODE;
//        }
//    }
    
    @Override
    public void buttonClick(ClickEvent event) {
        login();
    }

}
