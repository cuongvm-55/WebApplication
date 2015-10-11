package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Floor;
import com.luvsoft.entities.Table;
import com.luvsoft.facades.FloorFacade;
import com.luvsoft.facades.TableFacade;

public class FloorController extends AbstractController{
    /*
     * Gets floor list
     */
    public List<Floor> getAllFloor()
    {
        List<Floor> list = new ArrayList<Floor>();
        FloorFacade floorFacade = new FloorFacade();
        floorFacade.findAll(list);
        return list;
    }

    /*
     * Gets a table info by _id
     */
    public Table getTableInfo(String tableId)
    {
        TableFacade tableFacade = new TableFacade();
        Table table = new Table();
        tableFacade.findById(tableId, table);
        return table;
    }

}
