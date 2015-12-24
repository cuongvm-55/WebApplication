package com.luvsoft.MMI.threads;

import java.util.ArrayList;
import java.util.Iterator;
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
            System.out.println("Thread is running...");
            try {
                currentOrder.setWaitingTime(waitingTime);
                if(currentOrder.getStatus().equals(Types.State.WAITING) && waitingTime > 0) {
                    if( Adapter.updateFieldValueOfOrder(currentOrder.getId(), Order.DB_FIELD_NAME_WAITING_TIME, waitingTime) ) {
                        Broadcaster.broadcast(CoffeeshopUI.UPDATE_WAITING_TIME + "::" + currentOrder.getTableId() + "::" + waitingTime);
                    }
                }
                Thread.sleep(60000);
                // Thread.sleep(1000);
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

    public synchronized void start(int waitingTime) {
        this.waitingTime = waitingTime;
        super.start();
        listWaitingOrderThreads.add(this);
    }

    @Override
    public void interrupt() {
        runableFlag = false;
        super.interrupt();
    }

    public static boolean interruptWaitingOrderThread(Order order) {
        boolean ret = false;
        for(Iterator<NewOrderManager> it = listWaitingOrderThreads.iterator(); it.hasNext(); ) {
            NewOrderManager newOrderManager = it.next();
            if( newOrderManager.getCurrentOrder().getId().equals(order.getId()) ) {
                it.remove();
                newOrderManager.interrupt();
                ret = true;
            }
        }
        return ret;
    }

    public static void updateOrder(Order srcOrder, Order destOrder) {
        for (NewOrderManager newOrderManager : listWaitingOrderThreads) {
            if(newOrderManager.getCurrentOrder().getId().equals(srcOrder.getId())) {
                System.out.println("ORDER FOUND");
                newOrderManager.setCurrentOrder(destOrder);
            }
        }
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
