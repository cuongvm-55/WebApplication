package com.luvsoft.entities;

public class Types {
    public static final String DATE_TIME_PARTTERN = "dd/MM/yyyy HH:mm:ss";

    public static enum State{
        UNDEFINED("UNDEFINED"),
        // Table
        EMPTY("EMPTY"), // when table's not occupied yet
        WAITING("WAITING"), // waiting for an order
        FULL("FULL"), // the food are served
        
        // Order
        // WAITING("WAITING"), // making food
        PAID("PAID"), // have already pay the bill
        NOTPAID("NOTPAID"), // have not pay the bill yet
        
        // OrderDetails
        // WAITING("WAITING"), // waiting for bartender
        COMPLETED("COMPLETED"), // the food is ready for waiter to deliver to customer
        CANCELED("CANCELED") // for some reasons, the food is canceled
        ;

        private final String stateStr;

        private State(String st)
        {
            this.stateStr = st;
        }
        public String toString(){
            return this.stateStr;
        }
    }
}
