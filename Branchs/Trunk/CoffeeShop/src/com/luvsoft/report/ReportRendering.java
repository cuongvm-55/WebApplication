package com.luvsoft.report;
import jxl.*; 
import jxl.write.*;
import jxl.write.Number;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ReportRendering {
    public ReportRendering(){

    }
    
    public static LocalDateTime DateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static void export(){
        try{
            try{
                // Create work book
                WritableWorkbook workBook = Workbook.createWorkbook(new File("D:/TestExcel/output.xls"));
                
                // Create sheet on the desired work book
                WritableSheet sheet = workBook.createSheet("First Sheet", 0);
                // Add data to sheet
                Label label = new Label(0, 0, "This a label (0,0)");
                Number number = new Number( 1, 0, 3.14282079);
                sheet.addCell(label);
                sheet.addCell(number);

                // All sheets and cells added. Now write out the workbook 
                workBook.write(); 
                workBook.close();
            }catch(WriteException wE){
                wE.printStackTrace();
            }
        }catch(IOException ioE){
            ioE.printStackTrace();
        }
    }
}
