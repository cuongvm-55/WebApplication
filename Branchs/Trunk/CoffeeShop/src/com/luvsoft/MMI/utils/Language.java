package com.luvsoft.MMI.utils;

/**
 * @author datnq.55
 * 
 * This class is used to set up language for application
 * 
 * Tip: Language lang = new Language();
 *      lang.setLanguage(yourLanguage);
 */
public class Language {
    public enum LANGUAGE {
        ENGLISH, VIETNAMESE, JAPANESE
    }

    public void setLanguage(LANGUAGE lang) {
        switch (lang) {
            case ENGLISH:
            {
                FLOOR = "Floor";
                TABLE = "Table";
                WAITING = "Waiting";
                MINUTE = "minute";
                WAITER = "Waiter";
                BARTENDER = "Bartender";
                MANAGEMENT = "Management";
                
                SEQUENCE = "No";
                FOOD_NAME = "Name";
                STATUS = "Status";
                QUANTITY = "Quantity";
                PRICE = "Price";
                ADD_FOOD = "Add Food";
                CONFIRM_PAID = "Confirm Paid";
                CONFIRM_ORDER = "Confirm Order";
                TOTAL_AMOUNT = "Total: ";
                PAID_AMOUNT = "Paid:  ";
                CURRENCY_SYMBOL = "$";
                NOTE = "Note";
                ALL_DONE = "All Done";
            }
                break;
            case VIETNAMESE:
            {
                FLOOR = "Tầng";
                TABLE = "Bàn";
                WAITING = "Đợi";
                MINUTE = "phút";
                WAITER = "Nhân Viên Phục Vụ";
                BARTENDER = "Nhân Viên Pha Chế";
                MANAGEMENT = "Quản Lý";
                
                SEQUENCE = "STT";
                FOOD_NAME = "Tên";
                STATUS = "Trạng Thái";
                QUANTITY = "S.Lượng";
                PRICE = "Đơn Giá";
                ADD_FOOD = "Thêm Món";
                CONFIRM_PAID = "Xác Nhận T.Toán";
                CONFIRM_ORDER = "Xác Nhận H.Đơn";
                TOTAL_AMOUNT = "Tổng: ";
                PAID_AMOUNT = "Thực thu: ";
                CURRENCY_SYMBOL = "vnđ";
                NOTE = "Ghi chú";
                ALL_DONE = "Xong Hết";
            }
                break;
            case JAPANESE:
                break;
            default:
                break;
        }
    }

    public static String FLOOR;
    public static String TABLE;
    public static String WAITING;
    public static String MINUTE;
    public static String WAITER;
    public static String BARTENDER;
    public static String MANAGEMENT;

    // OrderInfoView texts
    public static String SEQUENCE;
    public static String FOOD_NAME;
    public static String STATUS;
    public static String QUANTITY;
    public static String PRICE;
    public static String ADD_FOOD;
    public static String CONFIRM_PAID;
    public static String CONFIRM_ORDER;
    public static String TOTAL_AMOUNT;
    public static String PAID_AMOUNT;
    public static String NOTE;
    // Currency
    public static String CURRENCY_SYMBOL;

    public static String ALL_DONE;
}
