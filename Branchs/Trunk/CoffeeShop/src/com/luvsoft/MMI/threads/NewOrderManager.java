package com.luvsoft.MMI.threads;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.CoffeeshopUI;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;

public class NewOrderManager extends Thread {

    private boolean runableFlag = true;
    private int waitingTime = 0;
    private Order currentOrder;
    public static List<NewOrderManager> listWaitingOrderThreads = new ArrayList<NewOrderManager>();

    public NewOrderManager(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public void run() {
        super.run();

        while(runableFlag) {
            try {
                currentOrder.setWaitingTime(waitingTime);
                if(currentOrder.getStatus().equals(Types.State.WAITING) && waitingTime > 0) {
                    if( Adapter.updateFieldValueOfOrder(currentOrder.getId(), Order.DB_FIELD_NAME_WAITING_TIME, waitingTime) ) {
                        Broadcaster.broadcast(CoffeeshopUI.UPDATE_WAITING_TIME + "::" + currentOrder.getTableId() + "::" + waitingTime);
                    }
                }
                Thread.sleep(60000);
                waitingTime++;
            }
            catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        listWaitingOrderThreads.add(this);
    }

    @Override
    public void interrupt() {
        runableFlag = false;
        super.interrupt();
    }

    public static boolean interruptWaitingOrderThread(Order order) {
        for (NewOrderManager newOrderManager : listWaitingOrderThreads) {
            if( newOrderManager.getCurrentOrder().getId().equals(order.getId()) ) {
                listWaitingOrderThreads.remove(newOrderManager);
                newOrderManager.interrupt();
                return true;
            }
        }
        return false;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
