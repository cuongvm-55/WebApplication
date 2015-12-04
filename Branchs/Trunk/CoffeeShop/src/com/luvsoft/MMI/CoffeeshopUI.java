package com.luvsoft.MMI;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.annotation.WebServlet;

import com.luvsoft.MMI.threads.Broadcaster;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.Language.LANGUAGE;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("tests-valo-facebook")
@Title("CoffeeShop")
@Push
public class CoffeeshopUI extends UI {

    public static final String MAIN_VIEW = "main";
    public static final String TABLE_LIST_VIEW = "tablelist";
    public static final String ORDER_LIST_VIEW = "orderlist";
    public static final String MANAGEMENT_VIEW = "management";

    public static final String NEW_ORDER_MESSAGE = "new_order";
    public static final String ORDER_COMPLETED_MESSAGE = "order_completed";
    public static final String UPDATE_WAITING_TIME = "update_waiting_time";
    public static final String CANCELED_ORDER = "canceled_order";
    public static final String CHANGE_TABLE_STATE = "change_table_state";
    public static final String ORDER_WAS_PAID = "order_was_paid";

    public Navigator navigator;
    public MainView mainView;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CoffeeshopUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        // Set Language is VietNamese
        Language lang = new Language();
        lang.setLanguage(LANGUAGE.VIETNAMESE);
        setLocale(new Locale("vi", "VN"));
        // Create a navigator to control the view
        navigator = new Navigator(this, this);

        // Adapter.createDataForMongoDB(); // just for test

        mainView = new MainView();
        // Create and register the view
        navigator.addView("", mainView);
        navigator.addView(CoffeeshopUI.MAIN_VIEW, mainView);
        
        //OrderFacade orderFacade = new OrderFacade();
        //orderFacade.removeById("560d5815b4f3a8129ce0f387");
        //orderFacade.updateFieldValue("560d579cb4f3a8129ce0f386", "OrderDetailList", "560d55c0b4f3a8129ce0f383,560d55f7b4f3a8129ce0f384");
        Broadcaster.register(this::receiveBroadcast);
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
        }
    }
}