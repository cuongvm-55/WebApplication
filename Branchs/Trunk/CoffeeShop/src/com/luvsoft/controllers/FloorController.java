package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Table;
import com.luvsoft.entities.Types;
import com.luvsoft.facades.FloorFacade;
import com.luvsoft.facades.TableFacade;

public class FloorController extends AbstractController{
    private static TableFacade tableFacade = new TableFacade();
    private static FloorFacade floorFacade = new FloorFacade();

    /*
     * Gets floor list
     */
    public List<Floor> getAllFloor()
    {
        List<Floor> list = new ArrayList<Floor>();
        floorFacade.findAll(list);
        // Sort floor by floor number
        Collections.sort(list);
        return list;
    }

    /*
     * Gets a table by _id
     */
    public Table getTableById(String tableId)
    {
        Table table = new Table();
        tableFacade.findById(tableId, table);
        return table;
    }
    
    /*
     * Change table state
     */
    public boolean setTableStatus(String tableId, Types.State status){
        return tableFacade.updateFieldValue(tableId, Table.DB_FIELD_NAME_STATE, status.toString());
    }
}
