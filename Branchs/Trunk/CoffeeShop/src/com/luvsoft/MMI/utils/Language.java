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
    public static LANGUAGE currentLanguage;
    public static enum LANGUAGE {
        ENGLISH, VIETNAMESE, JAPANESE
    }

    public void setLanguage(LANGUAGE lang) {
        currentLanguage = lang;
        switch (lang) {
            case ENGLISH:
            {
                FLOOR = "Floor";
                TABLE = "Table";
                EMPTY = "Available";
                FULL = "Full";
                WAITING = "Waiting";
                UNPAID = "Unpaid";
                PAID = "Paid";
                MINUTE = "minute";
                WAITER = "Waiter";
                BARTENDER = "Bartender";
                MANAGEMENT = "Management";
                
                // States
                COMPLETED = "Completed";
                CANCELED = "Canceled";
                
                SEQUENCE = "No";
                FOOD_NAME = "Name";
                STATUS = "Status";
                QUANTITY = "Quantity";
                PRICE = "Price";
                ADD_FOOD = "Add Food";
                ADD_ORDER = "Add Order";
                CONFIRM_PAID = "Confirm Paid";
                CONFIRM_ORDER = "Confirm Order";
                CONFIRM = "Confirm";
                TOTAL_AMOUNT = "Total: ";
                PAID_AMOUNT = "Paid:  ";
                CURRENCY_SYMBOL = "$";
                NOTE = "Note";
                ALL_DONE = "All Done";
                CANCEL = "Cancel";
                CANCEL_ORDER = "Cancel order";
                STATISTIC = "Statistic";
                FOOD_MANAGEMENT = "Food Management";
                CONFIGURATION = "Configuration";
                CHOOSE_DATE = "Choose the date range";
                FROM_DATE = "From Date: ";
                TO_DATE = "To Date: ";
                CLEAR_DATA = "Clear Data";
                CREATE_REPORT = "Create Report";
                DELETE = "Delete";
                DELETE_FOODS = "Remove food";
                ADD_CATEGORY = "Add category";
                DELETE_CATEGORY = "Remove category";

                NO_ORDER_IN_ORDER_LIST = "There's no order at the moment!";

                // Errors
                ERROR = "Error";
                CANNOT_CHANGE_TABLE_STATE = "Cannot change table state to Empty, curent Order is not Paid! Please Cancel this order if you want";
                CANNOT_CANCEL_ORDER = "Cannot change table state to Cancel, curent Order is Paid. Please use Empty instead of.";
                // Warnings
                WARNING = "Warning";
                
                DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
                DATE_TIME_FORMAT_DATE_ONLY = "yyyy/MM/dd";
                FROM = "From";
                TO = "To";

                CONFIRM_DELETE_TITLE = "Confirm to delete";
                CONFIRM_DELETE_CONTENT = "Do you really want to delete?";
                CONIFRM_CANCEL_ORDER = "Do you want to Cancel the order of this table?";
                ASK_FOR_CONFIRM = "Ok";
                ASK_FOR_DENIED = "Cancel";
            }
                break;
            case VIETNAMESE:
            {
                FLOOR = "Tầng";
                TABLE = "Bàn";
                EMPTY = "Trống";
                FULL = "Đầy";
                WAITING = "Đợi";
                UNPAID = "Chưa Thanh Toán";
                PAID = "Đã Thanh Toán";
                MINUTE = "phút";
                WAITER = "Nhân Viên Phục Vụ";
                BARTENDER = "Nhân Viên Pha Chế";
                MANAGEMENT = "Quản Lý";

                // States
                COMPLETED = "Xong";
                CANCELED = "Đã hủy";

                SEQUENCE = "STT";
                FOOD_NAME = "Tên";
                STATUS = "Trạng Thái";
                QUANTITY = "S.Lượng";
                PRICE = "Đơn Giá";
                ADD_FOOD = "Thêm Món";
                ADD_ORDER = "Đặt Món";
                CONFIRM_PAID = "Xác Nhận T.Toán";
                CONFIRM_ORDER = "Xác Nhận H.Đơn";
                CONFIRM = "Xác Nhận";
                TOTAL_AMOUNT = "Tổng: ";
                PAID_AMOUNT = "Thực thu: ";
                CURRENCY_SYMBOL = "vnđ";
                NOTE = "Ghi chú";
                ALL_DONE = "Xong Hết";
                CANCEL = "Thoát";
                CANCEL_ORDER = "Hủy Đặt Món";
                STATISTIC = "Thống Kê";
                NO_ORDER_IN_ORDER_LIST = "Hiện tại chưa có bàn nào đặt món!";
                FOOD_MANAGEMENT = "Quản Lí Món";
                CONFIGURATION = "Cấu Hình";
                CHOOSE_DATE = "Chọn khoảng thời gian";
                FROM_DATE = "Từ Ngày: ";
                TO_DATE = "Đến ngày: ";
                CLEAR_DATA = "Xóa Dữ Liệu";
                CREATE_REPORT = "Tạo Báo Cáo";
                DELETE = "Xóa";
                DELETE_FOODS = "Xóa món";
                ADD_CATEGORY = "Thêm danh mục";
                DELETE_CATEGORY = "Xóa danh mục";

                // Errors
                ERROR = "Lỗi";
                CANNOT_CHANGE_TABLE_STATE = "Không thể chuyển trạng thái Trống, hóa đơn chưa Thanh toán! Hãy nhấn Hủy nếu muốn hủy hóa đơn này";
                CANNOT_CANCEL_ORDER = "Không thể chuyển trạng thái Hủy, hóa đơn đã thanh toán. Hãy chọn Trống nếu muốn tiếp tục";
                // Warnings
                WARNING = "Cảnh báo";

                DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
                DATE_TIME_FORMAT_DATE_ONLY = "dd/MM/yyyy";
                FROM = "Từ";
                TO = "Đến";

                CONFIRM_DELETE_TITLE = "Xác nhận";
                CONFIRM_DELETE_CONTENT = "Bạn có chắc chắn muốn xóa dữ liệu không?";
                CONIFRM_CANCEL_ORDER = "Bạn có chắc chắn muốn Hủy hóa đơn của bàn này không?";
                ASK_FOR_CONFIRM = "Đồng ý";
                ASK_FOR_DENIED = "Hủy";
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
    public static String MINUTE;
    public static String WAITER;
    public static String BARTENDER;
    public static String MANAGEMENT;

    // States
    public static String EMPTY;
    public static String FULL;
    public static String WAITING;
    public static String UNPAID;
    public static String PAID;
    public static String COMPLETED;
    public static String CANCELED;
    public static String CANCEL_ORDER;
    public static String DELETE;
    
    // OrderInfoView texts
    public static String SEQUENCE;
    public static String FOOD_NAME;
    public static String STATUS;
    public static String QUANTITY;
    public static String PRICE;
    public static String ADD_FOOD;
    public static String ADD_ORDER;
    public static String CONFIRM;
    public static String CONFIRM_PAID;
    public static String CONFIRM_ORDER;
    public static String TOTAL_AMOUNT;
    public static String PAID_AMOUNT;
    public static String NOTE;
    public static String STATISTIC;
    public static String FOOD_MANAGEMENT;
    public static String CONFIGURATION;
    public static String CHOOSE_DATE;
    public static String FROM_DATE;
    public static String TO_DATE;
    public static String CLEAR_DATA;
    public static String CREATE_REPORT;
    public static String DELETE_FOODS;
    public static String ADD_CATEGORY;
    public static String DELETE_CATEGORY;

    // Currency
    public static String CURRENCY_SYMBOL;

    public static String ALL_DONE;
    public static String CANCEL;

    public static String NO_ORDER_IN_ORDER_LIST;

    // Errors
    public static String ERROR;
    public static String CANNOT_CHANGE_TABLE_STATE;
    public static String CANNOT_CANCEL_ORDER;

    // Warnings
    public static String WARNING;
    
    // Date time format
    public static String DATE_TIME_FORMAT;
    public static String DATE_TIME_FORMAT_DATE_ONLY;
    
    // Reports
    public static String FROM;
    public static String TO;
 
    // Confirm dialog
    public static String CONFIRM_DELETE_TITLE;
    public static String CONFIRM_DELETE_CONTENT;
    public static String CONIFRM_CANCEL_ORDER;
    public static String ASK_FOR_CONFIRM;
    public static String ASK_FOR_DENIED;
}
