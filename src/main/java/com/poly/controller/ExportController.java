package com.poly.controller;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/export")
public class ExportController {

    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> exportToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Booking Data");

        // Tạo tiêu đề
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("User ID");
        header.createCell(2).setCellValue("Tour ID");
        header.createCell(3).setCellValue("Tên tour");
        header.createCell(4).setCellValue("Số lượng người");
        header.createCell(5).setCellValue("Ngày khởi hành");
        header.createCell(6).setCellValue("Tổng tiền");
        header.createCell(7).setCellValue("Trạng thái");
        header.createCell(8).setCellValue("Phương thức thanh toán");
        header.createCell(9).setCellValue("Ghi chú");
        header.createCell(10).setCellValue("Booking at");

        // Thêm dữ liệu vào sheet
        // Giả sử bạn có một danh sách booking, ở đây chỉ là ví dụ
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue("User" + (i + 1));
            row.createCell(2).setCellValue("Tour" + (i + 1));
            row.createCell(3).setCellValue("Tour Name" + (i + 1));
            row.createCell(4).setCellValue(i + 5);
            row.createCell(5).setCellValue("2024-10-10");
            row.createCell(6).setCellValue(1000 + (i * 100));
            row.createCell(7).setCellValue("Đang xử lý");
            row.createCell(8).setCellValue("Tiền mặt");
            row.createCell(9).setCellValue("Ghi chú " + (i + 1));
            row.createCell(10).setCellValue("2024-10-10");
        }

        // Ghi vào output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookings.xlsx")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    // Tương tự cho xuất Word
    @GetMapping("/word")
    public ResponseEntity<ByteArrayResource> exportToWord() throws IOException {
        // Tạo tài liệu Word sử dụng Apache POI
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Danh sách booking");

        // Thêm bảng
        XWPFTable table = document.createTable(1, 11);
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("ID");
        headerRow.getCell(1).setText("User ID");
        headerRow.getCell(2).setText("Tour ID");
        headerRow.getCell(3).setText("Tên tour");
        headerRow.getCell(4).setText("Số lượng người");
        headerRow.getCell(5).setText("Ngày khởi hành");
        headerRow.getCell(6).setText("Tổng tiền");
        headerRow.getCell(7).setText("Trạng thái");
        headerRow.getCell(8).setText("Phương thức thanh toán");
        headerRow.getCell(9).setText("Ghi chú");
        headerRow.getCell(10).setText("Booking at");

        // Thêm dữ liệu vào bảng
        for (int i = 0; i < 10; i++) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            row.getCell(1).setText("User" + (i + 1));
            row.getCell(2).setText("Tour" + (i + 1));
            row.getCell(3).setText("Tour Name" + (i + 1));
            row.getCell(4).setText(String.valueOf(i + 5));
            row.getCell(5).setText("2024-10-10");
            row.getCell(6).setText(String.valueOf(1000 + (i * 100)));
            row.getCell(7).setText("Đang xử lý");
            row.getCell(8).setText("Tiền mặt");
            row.getCell(9).setText("Ghi chú " + (i + 1));
            row.getCell(10).setText("2024-10-10");
        }

        // Ghi vào output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookings.docx")
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
