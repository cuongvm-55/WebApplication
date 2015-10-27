package com.luvsoft.MMI;

import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.components.CoffeeTableElement.TABLE_STATE;
import com.luvsoft.MMI.utils.Language;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TableListView extends Panel{
    public TableListView(){
        super();
        init();
    }

    public void init(){
        this.setSizeFull();
        VerticalLayout vtcContentContainer = new VerticalLayout();
        for (int j = 0; j < 5; j++) {
            GridLayout gridElementContent = new GridLayout();
            gridElementContent.setRows(3);
            gridElementContent.setColumns(3);
            gridElementContent.setWidth("100%");
    
            // Add components to grid layout
            for (int i = 0; i < 6; i++) {
                CoffeeTableElement tableElement = new CoffeeTableElement(TABLE_STATE.STATE_WAITING, i, i);
    
                gridElementContent.addComponent(tableElement);
            }
    
            CustomizationTreeElement treeElement = new CustomizationTreeElement(gridElementContent, Language.FLOOR + " " + (j+1));
            vtcContentContainer.addComponent(treeElement);
            this.setContent(vtcContentContainer);
        }
    }
}
