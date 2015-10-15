package com.luvsoft.MMI;

import javax.servlet.annotation.WebServlet;

import com.luvsoft.MMI.utils.Language;
import com.luvsoft.MMI.utils.Language.LANGUAGE;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("coffeeshop")
public class CoffeeshopUI extends UI {

    public static final String MAIN_VIEW = "main";
    public static final String WAITER_VIEW = "waiter";
    public static final String BARTENDER_VIEW = "bartender";
    public static final String MANAGEMENT_VIEW = "management";

    public static Navigator navigator;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CoffeeshopUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        // Set Language is VietNamese
        Language lang = new Language();
        lang.setLanguage(LANGUAGE.VIETNAMESE);

        // Create a navigator to control the view
        navigator = new Navigator(this, this);

        // Create and register the view
        navigator.addView("", new WaiterView());
        navigator.addView(CoffeeshopUI.MAIN_VIEW, new WaiterView());
    }
}