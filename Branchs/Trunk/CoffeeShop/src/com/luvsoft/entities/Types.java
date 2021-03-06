package com.luvsoft.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.luvsoft.MMI.utils.Language;
public class Types {
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
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

    public static String StateToLanguageString(State state){
        String str = "UNDEFINED";
        switch( state){
        case EMPTY:
            str = Language.EMPTY;
            break;
        case WAITING:
            str = Language.WAITING;
            break;
        case FULL:
            str = Language.FULL;
            break;
        case PAID:
            str = Language.PAID;
            break;
        case UNPAID:
            str = Language.UNPAID;
            break;
        case COMPLETED:
            str = Language.COMPLETED;
            break;
        case CANCELED:
            str = Language.CANCELED;
            break;
        default:
            System.out.println("Undefined state!");
            break;
        }
        return str;
    }

    public static State StringToState(String str){
        State state = State.UNDEFINED;
        if( str.equals("EMPTY") || str.equals(Language.EMPTY)){
            state = State.EMPTY;
        }
        else if( str.equals("WAITING") || str.equals(Language.WAITING)){
            state = State.WAITING;
        }
        else if( str.equals("FULL") || str.equals(Language.FULL)){
            state = State.FULL;
        }
        else if( str.equals("PAID") || str.equals(Language.PAID)){
            state = State.PAID;
        }
        else if( str.equals("UNPAID") || str.equals(Language.UNPAID)){
            state = State.UNPAID;
        }
        else if( str.equals("COMPLETED") || str.equals(Language.COMPLETED)){
            state = State.COMPLETED;
        }
        else if( str.equals("CANCELED") || str.equals(Language.CANCELED) || str.equals(Language.CANCEL_ORDER)){
            state = State.CANCELED;
        }
        else{
            System.out.println("Invalid state string!");
        }
        return state;
    }
    
    /**
     * use when save list string to database
     * @param list
     * @return
     */
    public static String formatListToString(List<String> list){
        String str="";
        for(int i=0;i<list.size()-1;i++)
        {
            str += list.get(i) + ",";
        }
        if( list.size() > 0 ){
            str += list.get(list.size()-1);
        }
        return str;
    }
    
    /**
     * use when read string list from db
     */
    public static List<String> stringToList(String str){
        List<String> retList = new ArrayList<String>();
        if( !str.equals("") ){
            String[] list = str.split(",");
            retList.addAll(Arrays.asList(list));
        }
        return retList;
    }
    
    public static Date StringToDate(String str){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT); // it's not depend on the current language
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            System.out.println("Fail to parse date form str: " + str);
        }
        return null;
    }
    
    public static String DateToString(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT); // it's not depend on the current language
        return formatter.format(date);
    }
    
    public static String DateToTimeStampString(Date date){
        return date.getTime() + "";
    }
    
    public static Date TimeStampStringToDate(String timestamp){
        return new Date(Long.parseLong(timestamp) );
    }
    
    public static Date reachDayBegin(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date reachDayEnd(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static NumberFormat getNumberFormat(){
        NumberFormat nf = DecimalFormat.getInstance((Locale.ITALY));
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator(',');
        customSymbol.setGroupingSeparator('.');
        ((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
        nf.setGroupingUsed(true);
        return nf;
    }
    
    public static double getDoubleValueFromFormattedStr(String str){
        try{
            return getNumberFormat().parse(str).doubleValue();
        }
        catch(Exception e){
            System.out.println("Fail to parse string: " + str + " to double.");
        }
        return 0.00d;
    }
}
