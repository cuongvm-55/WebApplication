package com.luvsoft.MMI.utils;

import com.luvsoft.MMI.CoffeeshopUI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class MenuButtonListener implements ClickListener{

    private String menuItem;

    public MenuButtonListener(String menuItem) {
        this.menuItem = menuItem;
    }
    @Override
    public void buttonClick(ClickEvent event) {
        CoffeeshopUI.navigator.navigateTo(CoffeeshopUI.MAIN_VIEW + "/" + menuItem);
    }
    
}
