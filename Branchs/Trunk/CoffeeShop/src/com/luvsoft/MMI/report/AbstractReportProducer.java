package com.luvsoft.MMI.report;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.luvsoft.MMI.Adapter;

public abstract class AbstractReportProducer {
    public static String REPORT_DATE_TIME_FORMAT_DATE_ONLY = "dd/MM/yyyy";
    public static String REPORT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    // returns all header cells
    protected abstract boolean buildHeader(List<WritableCell> headers);

    // returns all footer cells
    protected abstract boolean buildFooter(List<WritableCell> footers);

    // returns all content cells
    protected abstract boolean buildContent(List<WritableCell> contents);

    // returns report name
    protected abstract String getReportName();

    // returns report name
    protected abstract String getSheetName();

    // returns title
    protected abstract String getTitle();

    public Label createLabelCell(int col, int row, String str){
        return new Label(col, row, str);
    }

    public Number createNumberCell(int col, int row, double fl){
        return new Number(col, row, fl);
    }

    public DateTime createDateCell(int col, int row, Date date, boolean isHeader){
        DateFormat customDateFormat;
        if( isHeader ){
            customDateFormat = new DateFormat(REPORT_DATE_TIME_FORMAT_DATE_ONLY); 
        }
        else{
            customDateFormat = new DateFormat(REPORT_DATE_TIME_FORMAT); 
        }
        
        WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
        return new DateTime(col, row, date, dateFormat);
    }

    public boolean export(){
        try{
            try{
                // Create work book
                // File name + current date
                SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss"); // it's not depend on the current language
                String fileName = Adapter.getConfiguration().getReportOutputDir()+"/"+getReportName() + formatter.format(new Date()) + ".xls";
                WritableWorkbook workBook = Workbook.createWorkbook(new File( fileName ));

                // Create sheet on the desired work book
                WritableSheet sheet = workBook.createSheet(getSheetName(), 0);

                // Create title
                Label lbTitle = createLabelCell(3, 1, getTitle());  // col 4, row 1
                WritableFont font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false);
                WritableCellFormat format = new WritableCellFormat (font);
                lbTitle.setCellFormat(format);
                sheet.addCell(lbTitle);

                // Build headers
                List<WritableCell> headers = new ArrayList<WritableCell>();
                if( buildHeader(headers) ){
                    for( WritableCell cell : headers ){
                        sheet.addCell(cell);
                    }
                }
                else{
                    return false;
                }

                // Build contents
                List<WritableCell> contents = new ArrayList<WritableCell>();
                if( buildContent(contents) ){
                    for( WritableCell cell : contents){
                        sheet.addCell(cell);
                    }
                }
                else{
                    return false;
                }

                // Build footers
                List<WritableCell> footers = new ArrayList<WritableCell>();
                if( buildFooter(footers) ){
                    for( WritableCell cell : footers){
                        sheet.addCell(cell);
                    }
                }
                else{
                    return false;
                }

                // All sheets and cells added. Now write out the workbook 
                workBook.write();
                workBook.close();
                return true;
            }catch(WriteException wE){
                wE.printStackTrace();
                return false;
            }
        }catch(IOException ioE){
            ioE.printStackTrace();
            return false;
        }
    }
}
