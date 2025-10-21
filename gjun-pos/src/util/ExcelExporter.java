package util;

import model.Product;
import model.Member;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelExporter {
    public static void exportProducts(java.util.List<Product> products, String filepath) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Products");
        int rownum = 0;
        Row header = sheet.createRow(rownum++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("SKU");
        header.createCell(2).setCellValue("Name");
        header.createCell(3).setCellValue("Price");
        header.createCell(4).setCellValue("Stock");

        for (Product p: products) {
            Row r = sheet.createRow(rownum++);
            r.createCell(0).setCellValue(p.getId());
            r.createCell(1).setCellValue(p.getSku());
            r.createCell(2).setCellValue(p.getName());
            r.createCell(3).setCellValue(p.getPrice().doubleValue());
            r.createCell(4).setCellValue(p.getStock());
        }

        for (int i=0;i<5;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(filepath)) { wb.write(fos); }
        wb.close();
    }

    public static void exportMembers(List<Member> members, String filepath) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Members");
        int rownum = 0;
        Row header = sheet.createRow(rownum++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Account");
        header.createCell(2).setCellValue("Name");
        header.createCell(3).setCellValue("Email");
        header.createCell(4).setCellValue("Phone");
        header.createCell(5).setCellValue("Role");
        header.createCell(6).setCellValue("Created At");

        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle dateStyle = wb.createCellStyle();
        short df = createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
        dateStyle.setDataFormat(df);

        for (Member m: members) {
            Row r = sheet.createRow(rownum++);
            r.createCell(0).setCellValue(m.getId());
            r.createCell(1).setCellValue(m.getAccount());
            r.createCell(2).setCellValue(m.getName());
            r.createCell(3).setCellValue(m.getEmail());
            r.createCell(4).setCellValue(m.getPhone());
            r.createCell(5).setCellValue(m.getRole());
            if (m.getCreatedAt()!=null) {
                Cell c = r.createCell(6);
                c.setCellValue(java.sql.Timestamp.valueOf(m.getCreatedAt()));
                c.setCellStyle(dateStyle);
            }
        }

        for (int i=0;i<7;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(filepath)) { wb.write(fos); }
        wb.close();
    }

    public static void exportSalesSummary(java.util.List<java.util.Map<String,Object>> rows, String filepath) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sales Summary");
        int rownum = 0;
        Row header = sheet.createRow(rownum++);
        header.createCell(0).setCellValue("Product ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Total Qty");
        header.createCell(3).setCellValue("Total Amount");

        for (Map<String,Object> rdata: rows) {
            Row r = sheet.createRow(rownum++);
            Object pid = rdata.get("product_id"); r.createCell(0).setCellValue(pid==null?0:((Number)pid).intValue());
            Object name = rdata.get("name"); r.createCell(1).setCellValue(name==null?"":name.toString());
            Object qty = rdata.get("total_qty"); r.createCell(2).setCellValue(qty==null?0:((Number)qty).doubleValue());
            Object amt = rdata.get("total_amount"); r.createCell(3).setCellValue(amt==null?0:((Number)amt).doubleValue());
        }

        for (int i=0;i<4;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(filepath)) { wb.write(fos); }
        wb.close();
    }
}
