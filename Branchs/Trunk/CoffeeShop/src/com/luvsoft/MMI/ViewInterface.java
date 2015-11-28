package com.luvsoft.MMI;

public interface ViewInterface {
    public void createView();
    public void reloadView();
    public ViewInterface getParentView();
    public void setParentView(ViewInterface parentView);
}
