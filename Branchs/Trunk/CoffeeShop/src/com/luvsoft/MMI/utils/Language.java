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
                LOGOUT = "Logout";
                ORDER = "Order";
                
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
                TABLE_MANAGEMENT = "Table Management";
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
                ADD_TABLE = "Add table";
                ADD_FLOOR = "Add floor";
                DELETE_TABLES = "Remove table";
                DELETE_FLOOR = "Remove floor";
                LOGIN = "Login";
                PINCODE = "Pincode";
                INVALID_PINCODE = "Invalid pincode.";
                NEW_CATEGORY = "New category";
                UPDATE_CATEGORY = "Update category";
                NEW_FOOD = "New food";
                UPDATE_FOOD = "Update food";
                NEW_TABLE = "New table";
                UPDATE_TABLE = "Update table";
                NEW_FLOOR = "New floor";
                UPDATE_FLOOR = "Update floor";
                REPORT_OUTPUT_DIR = "Report output dir";
                CODE = "Code";
                NAME = "Name";
                OPERATOR_PINCODE = "Operator pincode";
                SUP_PINCODE = "Supervisor pincode";
                CATEGORY = "Category";
                NUMBER = "Number";
                NO_ORDER_IN_ORDER_LIST = "There's no order at the moment!";
                ALL_FIELDS_ARE_REQUIRED = "All fields are required!";
                PAY_BILL = "Paid Order";
                NO_ORDER_IN_THIS_TABLE = "No order";
                FOOD = "Food";
                HAS_BEEN_COMPLETED = "has been completed.";
                CHANGE_TABLE = "Change Table";
                MOVE_TO = "Move to ";
                CHECK_ALL= "Check all";
              
                // Errors
                ERROR = "Error";
                CANNOT_CHANGE_TABLE_STATE = "Cannot change table state to Empty, curent Order is not Paid! Please Cancel this order if you want";
                CANNOT_CANCEL_ORDER = "Cannot change table state to Cancel, curent Order is Paid. Please use Empty instead of.";
                // Warnings
                WARNING = "Warning";
                HAVE_NEW_ORDER = "One table has new order";
                PAY_ATTENTION = "Pay Attention";
                ORDER_IN_TABLE = "Order in table ";
                HAS_BEEN_CANCELED = " has been canceled";
                HAS_BEEN_UPDATED = " has been changed";
                ORDERED = " ordered";
                CANNOT_CHANGE_THIS_ORDER_BECAUSE_IT_CHANGED = "The order of this table was changed by other waiter. Please close this window and do this action again.";

                DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
                DATE_TIME_FORMAT_DATE_ONLY = "yyyy/MM/dd";
                FROM = "From";
                TO = "To";

                CONFIRM_DELETE_TITLE = "Confirm to delete";
                CONFIRM_LOGOUT_TITLE = "Confirm to logout";
                CONFIRM_DELETE_CONTENT = "Do you really want to delete?";
                CONFIRM_LOGOUT_CONTENT = "Do you really want to logout?";
                CONIFRM_CANCEL_ORDER = "Do you want to Cancel the order of this table?";
                ASK_FOR_CONFIRM = "Ok";
                ASK_FOR_DENIED = "Cancel";
                CONFIRM_CHANGE_TABLE = "Confirm change table";
                CONFIRM_CHANGE_TABLE_CONTENT = "Do you really want to change table?";

                SAVE = "Save";
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
                LOGOUT = "Thoát";
                ORDER = "Đặt món";

                // States
                COMPLETED = "Xong";
                CANCELED = "Đã hủy";

                SEQUENCE = "STT";
                FOOD_NAME = "Tên";
                STATUS = "TT";
                QUANTITY = "SL";
                PRICE = "Giá";
                ADD_FOOD = "Thêm Món";
                ADD_ORDER = "Đặt Món";
                CONFIRM_PAID = "Xác Nhận T.Toán";
                CONFIRM_ORDER = "Xác Nhận Đ.Món";
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
                TABLE_MANAGEMENT = "Quản Lí Bàn";
                CONFIGURATION = "Cấu Hình";
                CHOOSE_DATE = "Chọn khoảng thời gian";
                FROM_DATE = "Từ: ";
                TO_DATE = "Đến: ";
                CLEAR_DATA = "Xóa Dữ Liệu";
                CREATE_REPORT = "Tạo Báo Cáo";
                DELETE = "Xóa";
                DELETE_FOODS = "Xóa món";
                ADD_CATEGORY = "Thêm danh mục";
                DELETE_CATEGORY = "Xóa danh mục";
                ADD_TABLE = "Thêm bàn";
                ADD_FLOOR = "Thêm tầng";
                DELETE_TABLES = "Xóa bàn";
                DELETE_FLOOR = "Xóa tầng";
                LOGIN = "Đăng nhập";
                PINCODE = "Mật khẩu";
                INVALID_PINCODE = "Mật khẩu không đúng!";
                NEW_CATEGORY = "Danh mục mới";
                UPDATE_CATEGORY = "Chỉnh sửa danh mục";
                NEW_FOOD = "Món mới";
                UPDATE_FOOD = "Chỉnh sửa món";
                NEW_TABLE = "Bàn mới";
                UPDATE_TABLE = "Chỉnh sửa bàn";
                NEW_FLOOR = "Tầng mới";
                UPDATE_FLOOR = "Chỉnh sửa tầng";
                REPORT_OUTPUT_DIR = "Thư mục xuất báo cáo";
                CODE = "Mã";
                NAME = "Tên";
                OPERATOR_PINCODE = "Mật khẩu nhân viên";
                SUP_PINCODE = "Mật khẩu quản lí";
                CATEGORY = "Danh mục";
                NUMBER = "Số";
                ALL_FIELDS_ARE_REQUIRED = "Bạn phải nhập đầy đủ thông tin!";
                PAY_BILL = "Thanh Toán";
                NO_ORDER_IN_THIS_TABLE = "Chưa đặt món";
                FOOD = "Món";
                HAS_BEEN_COMPLETED = "đã xong!";
                CHANGE_TABLE = "Chuyển Bàn";
                MOVE_TO = "Chuyển đến ";
                CHECK_ALL = "Chọn hết";

                // Errors
                ERROR = "Lỗi";
                CANNOT_CHANGE_TABLE_STATE = "Không thể chuyển trạng thái TRỐNG, hóa đơn chưa Thanh toán! Hãy nhấn HỦY nếu muốn hủy hóa đơn này";
                CANNOT_CANCEL_ORDER = "Không thể chuyển trạng thái HỦY, hóa đơn đã thanh toán. Hãy chọn TRỐNG nếu muốn tiếp tục";
                // Warnings
                WARNING = "Cảnh báo";
                HAVE_NEW_ORDER = "Có bàn đặt món";
                PAY_ATTENTION = "Chú Ý";
                ORDER_IN_TABLE = "Đặt món ở bàn ";
                HAS_BEEN_CANCELED = " đã bị HỦY";
                HAS_BEEN_UPDATED = " đã thay đổi";
                ORDERED = " đã đặt món";
                CANNOT_CHANGE_THIS_ORDER_BECAUSE_IT_CHANGED = "Đặt món của bàn này đã bị thay đổi bởi nhân viên khác. Hãy đóng cửa sổ này và thao tác lại";

                DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
                DATE_TIME_FORMAT_DATE_ONLY = "dd/MM/yyyy";
                FROM = "Từ";
                TO = "Đến";

                CONFIRM_DELETE_TITLE = "Xác nhận";
                CONFIRM_LOGOUT_TITLE = "Thoát";
                CONFIRM_DELETE_CONTENT = "Bạn có chắc chắn muốn xóa dữ liệu không?";
                CONFIRM_LOGOUT_CONTENT = "Bạn có chắc chắn muốn thoát không?";
                CONIFRM_CANCEL_ORDER = "Bạn có chắc chắn muốn Hủy hóa đơn của bàn này không?";
                ASK_FOR_CONFIRM = "Đồng ý";
                ASK_FOR_DENIED = "Hủy";
                CONFIRM_CHANGE_TABLE = "Xác nhận";
                CONFIRM_CHANGE_TABLE_CONTENT = "Bạn có chắc chắn muốn chuyển bàn không?";

                SAVE = "Lưu";
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
    public static String LOGOUT;

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
    public static String TABLE_MANAGEMENT;
    public static String CONFIGURATION;
    public static String CHOOSE_DATE;
    public static String FROM_DATE;
    public static String TO_DATE;
    public static String CLEAR_DATA;
    public static String CREATE_REPORT;
    public static String DELETE_FOODS;
    public static String ADD_CATEGORY;
    public static String DELETE_CATEGORY;
    public static String ADD_FLOOR;
    public static String ADD_TABLE;
    public static String DELETE_FLOOR;
    public static String DELETE_TABLES;
    public static String LOGIN;
    public static String PINCODE;
    public static String INVALID_PINCODE;
    public static String NEW_CATEGORY;
    public static String UPDATE_CATEGORY;
    public static String NEW_FOOD;
    public static String UPDATE_FOOD;
    public static String NEW_TABLE;
    public static String UPDATE_TABLE;
    public static String NEW_FLOOR;
    public static String UPDATE_FLOOR;
    public static String REPORT_OUTPUT_DIR;
    public static String CODE;
    public static String NAME;
    public static String OPERATOR_PINCODE;
    public static String SUP_PINCODE;
    public static String CATEGORY;
    public static String NUMBER;
    public static String ALL_FIELDS_ARE_REQUIRED;
    public static String PAY_BILL;
    public static String NO_ORDER_IN_THIS_TABLE;
    public static String FOOD;
    public static String HAS_BEEN_COMPLETED;
    public static String CHANGE_TABLE;
    public static String MOVE_TO;
    public static String CHECK_ALL;

    // Currency
    public static String CURRENCY_SYMBOL;

    public static String ALL_DONE;
    public static String CANCEL;

    public static String NO_ORDER_IN_ORDER_LIST;

    // Errors
    public static String ERROR;
    public static String CANNOT_CHANGE_TABLE_STATE;
    public static String CANNOT_CANCEL_ORDER;
    public static String PAY_ATTENTION;

    // Warnings
    public static String WARNING;
    public static String HAVE_NEW_ORDER;
    public static String ORDER_IN_TABLE;
    public static String HAS_BEEN_CANCELED;
    public static String HAS_BEEN_UPDATED;
    public static String ORDERED;
    public static String ORDER;
    public static String CANNOT_CHANGE_THIS_ORDER_BECAUSE_IT_CHANGED;

    // Date time format
    public static String DATE_TIME_FORMAT;
    public static String DATE_TIME_FORMAT_DATE_ONLY;
    
    // Reports
    public static String FROM;
    public static String TO;
 
    // Confirm dialog
    public static String CONFIRM_DELETE_TITLE;
    public static String CONFIRM_LOGOUT_TITLE;
    public static String CONFIRM_DELETE_CONTENT;
    public static String CONFIRM_LOGOUT_CONTENT;
    public static String CONIFRM_CANCEL_ORDER;
    public static String ASK_FOR_CONFIRM;
    public static String ASK_FOR_DENIED;
    public static String CONFIRM_CHANGE_TABLE;
    public static String CONFIRM_CHANGE_TABLE_CONTENT;

    // Form
    public static String SAVE;
}
