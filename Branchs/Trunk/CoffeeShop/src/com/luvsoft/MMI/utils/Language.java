package com.luvsoft.MMI.utils;

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
}
