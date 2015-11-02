package com.luvsoft.entities;

import com.luvsoft.MMI.utils.Language;
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
        UNPAID("UNPAID"), // have not pay the bill yet
        
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

    public static State StringToState(String str){
        State state = State.UNDEFINED;
        if( str.equals(Language.EMPTY)){
            state = State.EMPTY;
        }
        else if( str.equals(Language.WAITING) ){
            state = State.WAITING;
        }
        else if( str.equals(Language.FULL)){
            state = State.FULL;
        }
        else if( str.equals(Language.PAID)){
            state = State.PAID;
        }
        else if( str.equals(Language.UNPAID)){
            state = State.UNPAID;
        }
        else if( str.equals(Language.COMPLETED)){
            state = State.COMPLETED;
        }
        else if( str.equals(Language.CANCELED)){
            state = State.CANCELED;
        }
        else{
            System.out.println("Invalid state string!");
        }
        return state;
    }
}
