package com.luvsoft.MMI.components;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class LuvsoftNotification {

    Notification notify;
    public LuvsoftNotification(String caption, String description, Notification.Type type) {
        description += "<span style=\"position:fixed;top:0;left:0;width:100%;height:100%\"></span>";
        notify = new Notification(caption, description, type, true);
        notify.setDelayMsec(Notification.DELAY_FOREVER);
        notify.setPosition(Position.BOTTOM_RIGHT);
    }

    public void show() {
        notify.show(Page.getCurrent());
    }

    public void show(Page page) {
        notify.show(page);
    }
}
