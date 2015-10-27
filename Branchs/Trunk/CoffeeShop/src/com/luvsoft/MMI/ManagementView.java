package com.luvsoft.MMI;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author datnq.55
 *
 * This class is used to implement Management View
 */
@SuppressWarnings("serial")
public class ManagementView extends VerticalLayout{
    public ManagementView() {
        // TODO Add your components at here
        this.addComponent(new Label("This is management view"));
    }
}
