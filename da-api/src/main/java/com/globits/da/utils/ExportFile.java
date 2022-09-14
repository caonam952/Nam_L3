package com.globits.da.utils;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ExportFile {
    private List<EmployeeDto> listEmployees;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExportFile(List<EmployeeDto> employees){
        this.listEmployees = employees;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader(){
        sheet = workbook.createSheet("list-employee");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Id", style);
        createCell(row, 1, "Code", style);
        createCell(row, 2, "Name", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Phone", style);
        createCell(row, 5, "Age", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void write(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (EmployeeDto e : listEmployees) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, e.getId(),style);
            createCell(row, columnCount++, e.getCode(),style);
            createCell(row, columnCount++, e.getName(),style);
            createCell(row, columnCount++, e.getEmail(),style);
            createCell(row, columnCount++, e.getPhone(),style);
            createCell(row, columnCount++, e.getAge(),style);
        }

//        try (OutputStream os = Files.newOutputStream(Paths.get("E:\\Learn\\SpringOceantech\\Nam_L2\\uploads\\test.xlsx"))) {
//            workbook.write(os);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();;
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
