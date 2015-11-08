package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Category;
import com.luvsoft.facades.CategoryFacade;

/**
 * 
 * @author datnq.55
 *
 */

public class CategoryController extends AbstractController{
    private static CategoryFacade categoryFacade = new CategoryFacade();

    /**
     * Get all category from database
     * @return list of category
     */
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<Category>();
        categoryFacade.findAll(list);
        return list;
    }
}
