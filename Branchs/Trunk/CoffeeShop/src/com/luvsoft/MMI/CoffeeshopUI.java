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
    public static final String TABLE_LIST_VIEW = "tablelist";
    public static final String ORDER_LIST_VIEW = "orderlist";
    public static final String MANAGEMENT_VIEW = "management";
    public static final String ORDER_INFO_VIEW = "orderinfo";
    public static final String ADD_FOOD_VIEW   = "addfood";
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
        navigator.addView("", new MainView());
        navigator.addView(CoffeeshopUI.MAIN_VIEW, new MainView());
        
        //OrderFacade orderFacade = new OrderFacade();
        //orderFacade.removeById("560d5815b4f3a8129ce0f387");
        //orderFacade.updateFieldValue("560d579cb4f3a8129ce0f386", "OrderDetailList", "560d55c0b4f3a8129ce0f383,560d55f7b4f3a8129ce0f384");
    }
}