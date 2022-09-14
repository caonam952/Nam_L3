package com.globits.da.utils;

import com.globits.core.dto.DepartmentDto;
import com.globits.da.domain.Employee;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.WardDto;
import com.globits.da.service.impl.EmployeeServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import static com.globits.da.utils.NotifyMessage.SUCCESS;
import static com.globits.da.validate.ValidateEmployee.validateEmployee;

public class ImportExportExcelUtil {
    private static Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<String, Integer>();
    private static Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<String, Integer>();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DecimalFormat numberFormatter = new DecimalFormat("######################");
    private final static DataFormatter formatter = new DataFormatter();
    private static Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<String, String>();
    private static List<EmployeeDto> employees;
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    private static EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public ImportExportExcelUtil(EmployeeServiceImpl employeeServiceImpl) {
        ImportExportExcelUtil.employeeServiceImpl = employeeServiceImpl;
    }

    public ImportExportExcelUtil(List<EmployeeDto> employeeDtos) {
        ImportExportExcelUtil.employees = employeeDtos;
        workbook = new XSSFWorkbook();
    }

    public static String[] header = {"Name", "Code", "Email", "Phone", "Age",
            "Province Id", "District Id", "Ward Id"};

    public static void createHeader() {
        sheet = workbook.createSheet("list-employee-database");
        Row row = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        font.setColor(XSSFFont.COLOR_RED);
        cellStyle.setFont(font);

        for (int i = 0; i < header.length; ++i) {
            createCell(sheet, row, i, header[i], cellStyle);
        }
    }

    public static void createData() {
        int rowCount = 1;

        CellStyle cellStyle = workbook.createCellStyle();

        for (EmployeeDto employee : employees) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(sheet, row, columnCount++, employee.getName(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getCode(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getEmail(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getPhone(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getAge(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getProvinceDto().getId(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getDistrictDto().getId(), cellStyle);
            createCell(sheet, row, columnCount++, employee.getWardDto().getId(), cellStyle);

            try (OutputStream os = Files.newOutputStream(Paths.get("E:\\Learn\\SpringOceantech\\Nam_L2\\uploads\\test1.xlsx"))) {
                workbook.write(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createCell(Sheet sheet, Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof UUID) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        cell.setCellStyle(style);
    }

    public void exportExcelFile(HttpServletResponse response) throws IOException {
        createHeader();
        createData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public static List<String> importFileExcel(MultipartFile file) throws IOException {
        List<String> messages = new ArrayList<>();
        InputStream fileInputStream = file.getInputStream();
        employees = new ArrayList<>();
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);
        int rowCount;
        int cellIndex;
        boolean isEmployee;
        for (Row row : sheet) {
            rowCount = row.getRowNum();
            if (rowCount == 0) continue;
            EmployeeDto employeeDto = new EmployeeDto();
            isEmployee = true;
            for (Cell cell : row) {
                cellIndex = cell.getColumnIndex();
                getCellValue(cellIndex, row, employeeDto);
                NotifyMessage message = validateEmployee(employeeDto, cellIndex);
                if (!SUCCESS.equals(message)) {
                    messages.add(message.getMessage() + " Row: " + rowCount + "  Cell: " + cellIndex);
                    isEmployee = false;
                }
            }
            if (isEmployee) {
                Employee employee = new Employee();
                employeeServiceImpl.setEntity(employee, employeeDto);
                employeeServiceImpl.save(employee);
            }
        }
        return messages;
    }


    public static void getCellValue(int cellIndex, Row row, EmployeeDto employeeDto) {

        switch (cellIndex) {
            case 0:
                employeeDto.setCode(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case 1:
                employeeDto.setName(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case 2:
                employeeDto.setEmail(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case 3:
                employeeDto.setPhone(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case 4:
                employeeDto.setAge(Integer.parseInt(formatter.formatCellValue(row.getCell(cellIndex))));
                break;
            case 5:
                ProvinceDto provinceDto = new ProvinceDto();
                provinceDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDto.setProvinceDto(provinceDto);
                break;
            case 6:
                DistrictDto districtDto = new DistrictDto();
                districtDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDto.setDistrictDto(districtDto);
                break;
            case 7:
                WardDto wardDto = new WardDto();
                wardDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDto.setWardDto(wardDto);
                cellIndex = 0;
                break;
            default:
                break;
        }
    }

    private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
        Row row = datatypeSheet.getRow(scanRowIndex);
        int numberCell = row.getPhysicalNumberOfCells();

        hashColumnPropertyConfig.put("staffCode".toLowerCase(), "staffCode");
        hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
        hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
        hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
        hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
        hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
        hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
        hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
        hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
        hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
        hashColumnPropertyConfig.put("password".toLowerCase(), "password");
        hashColumnPropertyConfig.put("email".toLowerCase(), "email");
        hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");

        hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
        hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
        hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");

        for (int i = 0; i < numberCell; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue != null && cellValue.length() > 0) {
                    cellValue = cellValue.toLowerCase().trim();
                    String propertyName = hashColumnPropertyConfig.get(cellValue);
                    if (propertyName != null) {
                        hashStaffColumnConfig.put(propertyName, i);
                    }
                }
            }
        }
    }

    public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
        try {

            List<DepartmentDto> ret = new ArrayList<DepartmentDto>();
            // FileInputStream excelFile = new FileInputStream(new File(filePath));
            // Workbook workbook = new XSSFWorkbook(excelFile);
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            // Iterator<Row> iterator = datatypeSheet.iterator();
            int rowIndex = 4;

            hashDepartmentColumnConfig.put("code", 0);

            hashDepartmentColumnConfig.put("name", 1);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    DepartmentDto department = new DepartmentDto();
                    Integer index = hashDepartmentColumnConfig.get("code");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// code
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String code = numberFormatter.format(value);
                            department.setCode(code);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue();
                            department.setCode(code);
                        }
                    }
                    index = hashDepartmentColumnConfig.get("name");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// name
                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue();
                            department.setName(name);
                        }
                    }
                    ret.add(department);
                }
                rowIndex++;
            }
            return ret;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//	public static void main(String[] agrs) {
//		try {
//			
//				FileInputStream fileIn = new FileInputStream(new File("C:\\Projects\\Globits\\Education\\globits-ecosystem\\hr\\hr-app\\Document\\DanhSachNhanSuDHTL.xlsx"));
//				List lst = getListStaffFromInputStream(fileIn);
//				System.out.println(lst.size());
//			}catch (Exception ex) {
//					ex.printStackTrace();
//			}
//
//	}
}
