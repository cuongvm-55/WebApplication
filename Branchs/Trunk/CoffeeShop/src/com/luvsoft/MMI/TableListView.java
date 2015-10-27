package com.luvsoft.MMI;

import java.util.List;

import com.luvsoft.MMI.components.CoffeeTableElement;
import com.luvsoft.MMI.components.CustomizationTreeElement;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Table;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TableListView extends Panel{
    private List<Floor> floorList;
    private List<Table> tableList;
    public TableListView(){
        super();
        init();
    }

    public void init(){
        this.setSizeFull();
        VerticalLayout vtcContentContainer = new VerticalLayout();
        floorList = Adapter.retrieveFloorList();
        for (int j = 0; j < floorList.size(); j++) {
            GridLayout gridElementContent = new GridLayout();
            gridElementContent.setRows(3);
            gridElementContent.setColumns(3);
            gridElementContent.setWidth("100%");
    
            tableList = Adapter.retrieveTableList(floorList.get(j).getTableIdList());
            // Add components to grid layout
            for (int i = 0; i < tableList.size(); i++) {
                Table table = tableList.get(i);
                CoffeeTableElement tableElement = new CoffeeTableElement(
                        CoffeeTableElement.StringToTableState(table.getState().toString()), // table state
                        i,                                                                  // waiting time
                        Language.TABLE + " " + table.getNumber());                          // table number
    
                gridElementContent.addComponent(tableElement);
            }
    
            CustomizationTreeElement treeElement = new CustomizationTreeElement(gridElementContent, Language.FLOOR + " " + floorList.get(j).getNumber());
            vtcContentContainer.addComponent(treeElement);
            this.setContent(vtcContentContainer);
        }
    }
}
