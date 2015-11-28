package com.luvsoft.MMI.order;

import com.luvsoft.MMI.ViewInterface;
import com.luvsoft.entities.Table;
import com.vaadin.ui.Window;

/**
 * 
 * @author datnq.55
 *
 */

@SuppressWarnings("serial")
public abstract class AbstractOrderView extends Window implements ViewInterface{
    private ViewInterface parentView;
    private Table currentTable;

    public ViewInterface getParentView() {
        return parentView;
    }

    public void setParentView(ViewInterface parentView) {
        this.parentView = parentView;
    }

    public Table getCurrentTable() {
        return currentTable;
    }

    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }
}
