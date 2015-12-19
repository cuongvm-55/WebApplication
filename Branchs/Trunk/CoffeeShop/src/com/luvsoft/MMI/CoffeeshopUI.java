package com.luvsoft.MMI;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.Language.LANGUAGE;
import com.luvsoft.MMI.utils.ValoThemeSessionInitListener;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("tests-valo-facebook")
@Title("Huyền Coffee")
@Push
public class CoffeeshopUI extends UI {
    public static final String LOGIN_VIEW = "login";
    public static final String MAIN_VIEW = "main";
    public static final String TABLE_LIST_VIEW = "tablelist";
    public static final String ORDER_LIST_VIEW = "orderlist";
    public static final String MANAGEMENT_VIEW = "management";

    public static final String NEW_ORDER_MESSAGE = "new_order";
    public static final String ORDER_UPDATED_MESSAGE = "order_updated";
    public static final String ORDER_COMPLETED_MESSAGE = "order_completed";
    public static final String UPDATE_WAITING_TIME = "update_waiting_time";
    public static final String CANCELED_ORDER = "canceled_order";
    public static final String CHANGE_TABLE_STATE = "change_table_state";
    public static final String ORDER_WAS_PAID = "order_was_paid";
    public static final String FOOD_WAS_COMPLETED = "food_was_complete";

    public Navigator navigator;
    public MainView mainView;
    public LoginView lgView;

    private boolean testMode = false;

    CssLayout menu = new CssLayout();

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CoffeeshopUI.class)
    public static class Servlet extends VaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(
                    new ValoThemeSessionInitListener());
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        if (request.getParameter("test") != null) {
            testMode = true;

            if (browserCantRenderFontsConsistently()) {
                getPage().getStyles().add(
                        ".v-app.v-app.v-app {font-family: Sans-Serif;}");
            }
        }

        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }

        if (!testMode) {
            Responsive.makeResponsive(this);
        }

        // Set Language is VietNamese
        Language lang = new Language();
        lang.setLanguage(LANGUAGE.VIETNAMESE);
        setLocale(new Locale("vi", "VN"));
        addStyleName(ValoTheme.UI_WITH_MENU);
        // Create a navigator to control the view
        navigator = new Navigator(this, this);
        lgView = new LoginView();

        Broadcaster.register(this::receiveBroadcast);

        // main view
        mainView = new MainView();
        mainView.setMenu(menu);
        mainView.createView();

        navigator.addView(CoffeeshopUI.MAIN_VIEW, mainView);
        navigator.addView("", mainView);

        // Create and register the view
        navigator.addView(CoffeeshopUI.LOGIN_VIEW, lgView);
        lgView.setLoggedIn(getSession().getAttribute("user") != null);

        //
        // We use a view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;
                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(CoffeeshopUI.LOGIN_VIEW);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        // IE8 also has randomness in its font rendering...

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS")
                || (getPage().getWebBrowser().isIE() && getPage()
                        .getWebBrowser().getBrowserMajorVersion() <= 9);
    }

    static boolean isTestMode() {
        return ((CoffeeshopUI) getCurrent()).testMode;
    }

    @Override
    public void detach() {
        Broadcaster.unregister(this::receiveBroadcast);
        super.detach();
    }

    /**
     * This function is used to analyze the message from any broadcast events
     * 
     * @param message
     * message has two part: messageId and messageData. MessageId and messageData is separated by :: token
     */
    public void receiveBroadcast(final String message) {
        if(!lgView.isLoggedIn()) {
            System.out.println("Login failed!....");
            return;
        }

        StringTokenizer st = new StringTokenizer(message, "::");
        String messageId, messageData;

        if(st.countTokens() < 2) {
            messageId = message;
            messageData = "";
        } else {
            messageId = (String) st.nextElement();
            messageData = (String) st.nextElement();
        }

        if(messageId.equals(CoffeeshopUI.UPDATE_WAITING_TIME)) {
            access(() -> mainView.getTableListView().updateWaitingTime());
        } else if(messageId.equals(CoffeeshopUI.NEW_ORDER_MESSAGE)) {
            access(() -> mainView.getOrderListView().haveNewOrder(messageData));
            access(() -> mainView.getTableListView().reloadView());
        } else if(messageId.equals(CoffeeshopUI.ORDER_UPDATED_MESSAGE)){
            access(() -> mainView.getOrderListView().haveNewOrderUpdated(messageData));
            access(() -> mainView.getTableListView().reloadView());
        } else if(messageId.equals(CoffeeshopUI.ORDER_COMPLETED_MESSAGE)) {
            access(() -> mainView.getTableListView().haveNewOrderCompleted(messageData));
            access(() -> mainView.getOrderListView().reloadView());
        } else if(messageId.equals(CoffeeshopUI.CANCELED_ORDER)) {
            access(() -> mainView.getOrderListView().haveCanceledOrder(messageData));
            access(() -> mainView.getTableListView().haveCanceledOrder());
        } else if(messageId.equals(CoffeeshopUI.CHANGE_TABLE_STATE)) {
            access(() -> mainView.getTableListView().reloadView());
        } else if(messageId.equals(CoffeeshopUI.ORDER_WAS_PAID)) {
            access(() -> mainView.getTableListView().orderWasPaid(messageData));
            access(() -> mainView.getOrderListView().reloadView());
        } else if(messageId.equals(CoffeeshopUI.FOOD_WAS_COMPLETED)) {
            access(() -> mainView.getTableListView().foodWasCompleted(messageData));
        }

    }
}