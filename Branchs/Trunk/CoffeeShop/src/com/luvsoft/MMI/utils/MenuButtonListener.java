package com.luvsoft.MMI.utils;

import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.MMI.MainView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MenuButtonListener implements ClickListener{

    private String menuItem;
    MainView mainView;

    public MenuButtonListener(String menuItem, MainView mainView) {
        this.menuItem = menuItem;
        this.mainView = mainView;
    }
    @Override
    public void buttonClick(ClickEvent event) {
        mainView.setViewChangedByClickingButton(true);
        UI.getCurrent().getNavigator().navigateTo(CoffeeshopUI.MAIN_VIEW + "/" + menuItem);
    }
}
