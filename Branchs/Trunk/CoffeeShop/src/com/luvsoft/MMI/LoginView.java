package com.luvsoft.MMI;

import com.luvsoft.MMI.utils.Language;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LoginView extends CustomComponent implements View{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final TextField user;

    private final PasswordField password;

    private final Button loginButton;

    private final VerticalLayout fields;
    @SuppressWarnings("serial")
    public LoginView() {
        setSizeFull();

        // Create the user input field
        user = new TextField("Nhập tên của bạn:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInvalidAllowed(false);

        // Create the password input field
        password = new PasswordField("Mật khẩu:");
        password.setWidth("300px");
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Login");
        loginButton.setClickShortcut(KeyCode.ENTER, null);
        loginButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                login();
            }
        });

        // Add both to a panel
        fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption(Language.LOGIN);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        getSession().setAttribute("user", null);
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

}
