package com.luvsoft.MMI.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.format.Colour;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import com.luvsoft.MMI.Adapter;
import com.luvsoft.MMI.order.OrderDetailRecord;
import com.luvsoft.MMI.order.OrderInfo;
import com.luvsoft.MMI.utils.Language;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Types;

public class OrderInfoProducer extends AbstractReportProducer{
    private Date fromDate;
    private Date toDate;

    private enum FIELD_HEADER{
        NO(0),           // No
        CREATING_TIME (1),   // Creating time
        TABLE_NAME(2),   // Table name
        FOOD_NAME(3),    // Food name
        QUANTITY(4),     // Number of foods
        PRICE(5),        // Price of 1 food
        AMOUNT(6),       // Amount = (quantity * price)
        STATUS(7),       // Status of record (should different from WAIT)

        EMPTY(8),        // Space between records and total column

        TOTAL_AMOUNT(9), // Total = nbOfRecords * amount
        PAID_AMOUNT(10),  // Real amount received from customer
        STAFF_NAME(11),  // Staff name
        NOTE(12); // Note
        private final int num;
        private FIELD_HEADER(int index){
            num = index;
        }
        
        public int getValue(){ return num;}
    };
    
    
    public OrderInfoProducer(Date _fromDate, Date _toDate){
        this.fromDate = _fromDate;
        this.toDate = _toDate;
    }

    @Override
    protected boolean buildHeader(List<WritableCell> headers) {
        // date range
        Label lbFrom = createLabelCell(0, 2, Language.FROM);       // line 2
        Label lbTo = createLabelCell(0, 3, Language.TO);           // line 3
        DateTime dateFromDate = createDateCell(1,  2, fromDate, true);
        DateTime dateToDate = createDateCell(1, 3, toDate, true);

        if( headers == null ){
            headers = new ArrayList<WritableCell>();
        }
        headers.add(lbFrom);
        headers.add(dateFromDate);
        headers.add(lbTo);
        headers.add(dateToDate);
        return true;
    }

    @Override
    protected boolean buildFooter(List<WritableCell> footers) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected boolean buildContent(List<WritableCell> contents) {
        // line 4, empty
        // build record headers
        WritableFont times12font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
        try{
            times12font.setColour(Colour.BLACK);
        }catch(WriteException e){
            // do nothing
        }
        WritableCellFormat times12format = new WritableCellFormat (times12font);
        Label lbNo = createLabelCell(FIELD_HEADER.NO.getValue(), 5, "Stt"); // line 5
        Label lbTableName = createLabelCell(FIELD_HEADER.TABLE_NAME.getValue(), 5, "Bàn");
        Label lbFoodName = createLabelCell(FIELD_HEADER.FOOD_NAME.getValue(), 5, "Tên Món");
        Label lbQuantity = createLabelCell(FIELD_HEADER.QUANTITY.getValue(), 5, "S.Lg");
        Label lbPrice = createLabelCell(FIELD_HEADER.PRICE.getValue(), 5, "Giá");
        Label lbAmount = createLabelCell(FIELD_HEADER.AMOUNT.getValue(), 5, "Thành Tiền");
        Label lbStatus = createLabelCell(FIELD_HEADER.STATUS.getValue(), 5, "Trạng Thái");
        Label lbEmpty = createLabelCell(FIELD_HEADER.EMPTY.getValue(), 5, "");
        Label lbTotalAmount = createLabelCell(FIELD_HEADER.TOTAL_AMOUNT.getValue(), 5, "Tổng Tiền");
        Label lbPaidAmount = createLabelCell(FIELD_HEADER.PAID_AMOUNT.getValue(), 5, "Thực Thu");
        Label lbCreatingTime = createLabelCell(FIELD_HEADER.CREATING_TIME.getValue(), 5, "Thời Gian");
        Label lbStaffName = createLabelCell(FIELD_HEADER.STAFF_NAME.getValue(), 5, "Tên Nhân Viên");
        Label lbNote = createLabelCell(FIELD_HEADER.NOTE.getValue(), 5, "Ghi chú");

        lbNo.setCellFormat(times12format);
        lbTableName.setCellFormat(times12format);
        lbFoodName.setCellFormat(times12format);
        lbQuantity.setCellFormat(times12format);
        lbPrice.setCellFormat(times12format);
        lbAmount.setCellFormat(times12format);
        lbStatus.setCellFormat(times12format);
        lbEmpty.setCellFormat(times12format);
        lbTotalAmount.setCellFormat(times12format);
        lbPaidAmount.setCellFormat(times12format);
        lbCreatingTime.setCellFormat(times12format);
        lbStaffName.setCellFormat(times12format);
        lbNote.setCellFormat(times12format);

        if( contents == null ){
            contents = new ArrayList<WritableCell>();
        }
        contents.add(lbNo);
        contents.add(lbCreatingTime);
        contents.add(lbTableName);
        contents.add(lbFoodName);
        contents.add(lbQuantity);
        contents.add(lbPrice);
        contents.add(lbAmount);
        contents.add(lbStatus);
        contents.add(lbEmpty);
        contents.add(lbTotalAmount);
        contents.add(lbPaidAmount);
        contents.add(lbStaffName);
        contents.add(lbNote);

        // Build records
        // Retrieve order list fall in the date range,
        // only retrieve PAID orders
        List<Types.State> states = new ArrayList<Types.State>();
        states.add(Types.State.CANCELED);
        states.add(Types.State.PAID);
        List<Order> orderList = Adapter.getOrderListWithStates(states, fromDate, toDate);
        if( orderList == null ){
            // Stop here is there's no Order with PAID state
            System.out.println("There's no orders with state PAID between "+fromDate.toString() + ", " + toDate.toString());
            return true;
        }

        int row = 6; // start at row 6
        for( Order order : orderList ){
            order.toString();
            OrderInfo orderInfo = Adapter.retrieveOrderInfo(order);
            if( orderInfo == null){
                System.out.println("Fail to retrieve order info, orderId: " + order.getId());
                return false;
            }

            List<OrderDetailRecord> odDtList = orderInfo.getOrderDetailRecordList();
            if( odDtList == null ){
                System.out.println("Fail to get order detail list of order, orderId: " + order.getId());
                return false;
            }

            double totalAmount= 0.00f;
            for( int index=0; index < odDtList.size(); index++ ){
                double amount = 0.00f;
                OrderDetailRecord rc = odDtList.get(index);
                WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
                if( rc.getStatus() == Types.State.CANCELED ){
                    try{
                        font.setColour(Colour.RED);}
                    catch(WriteException e){
                        // do nothing
                    }
                }
                WritableCellFormat contentFormat = new WritableCellFormat (font);

                jxl.write.Number nbrNo = createNumberCell(FIELD_HEADER.NO.getValue(), row, row - 6);
                nbrNo.setCellFormat(contentFormat);
                contents.add(nbrNo);

                DateTime date = createDateCell(FIELD_HEADER.CREATING_TIME.getValue(), row, order.getCreatingTime(), false);
                contents.add(date);
                
                Label lblTblName = createLabelCell(FIELD_HEADER.TABLE_NAME.getValue(), row, orderInfo.getTableName());
                lblTblName.setCellFormat(contentFormat);
                contents.add(lblTblName);

                Label lblFoodName = createLabelCell(FIELD_HEADER.FOOD_NAME.getValue(), row, rc.getFoodName());
                lblFoodName.setCellFormat(contentFormat);
                contents.add(lblFoodName);

                jxl.write.Number nbrQuantity = createNumberCell(FIELD_HEADER.QUANTITY.getValue(), row, rc.getQuantity());
                nbrQuantity.setCellFormat(contentFormat);
                contents.add(nbrQuantity);

                jxl.write.Number nbrPrice = createNumberCell(FIELD_HEADER.PRICE.getValue(), row, rc.getPrice());
                nbrPrice.setCellFormat(contentFormat);
                contents.add(nbrPrice);

                amount = (rc.getPrice()*rc.getQuantity());
                jxl.write.Number nbrAmount = createNumberCell(FIELD_HEADER.AMOUNT.getValue(), row, amount);
                nbrAmount.setCellFormat(contentFormat);
                contents.add(nbrAmount);

                Label lblStatus = createLabelCell(FIELD_HEADER.STATUS.getValue(), row, rc.getStatus().toString());
                lblStatus.setCellFormat(contentFormat);
                contents.add(lblStatus);
                if(rc.getStatus() == Types.State.COMPLETED){
                    totalAmount += amount;
                }

                // for empty field, do not create cell
                // if current record is the last record of order, create the total cell and paid amount cell
                if( index == (odDtList.size() - 1) ){
                    WritableFont fontTotal = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
                    WritableCellFormat totalFormat = new WritableCellFormat (fontTotal);
                    jxl.write.Number nbrTotalAmount = createNumberCell(FIELD_HEADER.TOTAL_AMOUNT.getValue(), row, totalAmount);
                    nbrTotalAmount.setCellFormat(totalFormat);
                    contents.add(nbrTotalAmount);

                    jxl.write.Number nbrPaidAmount = createNumberCell(FIELD_HEADER.PAID_AMOUNT.getValue(), row, order.getPaidMoney());
                    nbrPaidAmount.setCellFormat(totalFormat);
                    contents.add(nbrPaidAmount);

                    // staff name
                    Label lblStaffName = createLabelCell(FIELD_HEADER.STAFF_NAME.getValue(), row, order.getStaffName());
                    lblStaffName.setCellFormat(totalFormat);
                    contents.add(lblStaffName);

                    // Note
                    Label lblNote = createLabelCell(FIELD_HEADER.NOTE.getValue(), row, order.getNote());
                    lblNote.setCellFormat(totalFormat);
                    contents.add(lblNote);
                }

                row++;
            }
        }
        return true;
    }

    @Override
    protected String getReportName() {
        return "OrderDetail_";
    }

    @Override
    protected String getSheetName() {
        return "Hóa Đơn";
    }

    @Override
    protected String getTitle() {
        return "Báo Cáo Tổng Hợp";
    }

}
