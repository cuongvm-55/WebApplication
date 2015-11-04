package com.luvsoft.MMI.Order;

import com.luvsoft.MMI.utils.Language;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author datnq.55
 *
 */

@SuppressWarnings("serial")
public class AddFood extends Window {

    public AddFood() {
        super();
        initView();
    }

    private void initView() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setSizeFull();

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        Label lblTableNumber = new Label(Language.TABLE + " ");
        lblTableNumber.addStyleName("bold huge FONT_TAHOMA TEXT_CENTER TEXT_WHITE BACKGROUND_BLUE");
        lblTableNumber.setWidth("100%");

        container.addComponents(lblTableNumber);

        setContent(container);
    }

    public Component buildContent() {
        VerticalLayout container = new VerticalLayout();
        return container;
    }
}
