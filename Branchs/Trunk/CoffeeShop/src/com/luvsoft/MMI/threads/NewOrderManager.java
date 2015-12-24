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
            System.out.println("Thread running...TableId: " + currentOrder.getTableId() + ", OrderId: " + currentOrder.getId());
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
                // Restore the interrupted status
                Thread.currentThread().interrupt();
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
        if( !super.isInterrupted() ){
            super.interrupt();
        }
    }

    public static boolean interruptWaitingOrderThread(Order order) {
        boolean ret = false;
        for(Iterator<NewOrderManager> it = listWaitingOrderThreads.iterator(); it.hasNext(); ) {
            NewOrderManager newOrderManager = it.next();
            if( newOrderManager.getCurrentOrder().getId().equals(order.getId()) ) {
                System.out.println("Interrupt thread...TableId: " + order.getTableId() + ", OrderId: " + order.getId());
                newOrderManager.interrupt();
                it.remove();
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Used to update or create or remove a thread corresponding to an order
     * 
     * @param order
     */
    public static void onOrderStateChange(Order order) {
        if(order == null) {
            return;
        }

        boolean isExisted = false;
        for(Iterator<NewOrderManager> it = listWaitingOrderThreads.iterator(); it.hasNext(); ) {
            NewOrderManager newOrderManager = it.next();
            if( newOrderManager.getCurrentOrder().getId().equals(order.getId()) ) {
                isExisted = true;
                if(order.getStatus().equals(Types.State.WAITING)) {
                    System.out.println("Update thread...TableId: " + order.getTableId() + ", OrderId: " + order.getId());
                    newOrderManager.setCurrentOrder(order);
                } else {
                    newOrderManager.interrupt();
                    it.remove();
                }
                break;
            }
        }

        if(!isExisted && !order.getId().equals("")) {
            NewOrderManager newthread = new NewOrderManager(order);
            newthread.start(order.getWaitingTime());
            System.out.println("Add new thread...TableId: " + order.getTableId() + ", OrderId: " + order.getId());
        }
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
