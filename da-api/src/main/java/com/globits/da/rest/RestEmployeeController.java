package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.Feedback;
import com.globits.da.service.EmployeeService;
import com.globits.da.service.impl.EmployeeServiceImpl;
import com.globits.da.utils.ExportFile;
import com.globits.da.utils.ImportExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.globits.da.utils.NotifyMessage.NOT_SUCCESS;
import static com.globits.da.utils.NotifyMessage.SUCCESS;

@RestController
@RequestMapping("/api")
//@EnableAutoConfiguration
public class RestEmployeeController {
    private final Path root = Paths.get("E:\\Learn\\SpringOceantech\\Nam_L2\\uploads");
    private final EmployeeService employeeService;

    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public RestEmployeeController(EmployeeService theEmployeeService, EmployeeServiceImpl theEmployeeServiceImpl) {
        employeeService = theEmployeeService;
        employeeServiceImpl = theEmployeeServiceImpl;
    }

    @GetMapping("/employees/list")
    public List<EmployeeDto> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        return employee;
    }

    @PostMapping("/employees")
    public Feedback<?> addEmployee(@RequestBody EmployeeDto employee) {

        Feedback<EmployeeDto> feedback = employeeService.saveEmployee(employee);
        if (feedback.getMessenger().equals(SUCCESS.getMessage())) {
            return new Feedback<>(SUCCESS.getCode(), SUCCESS.getMessage(), feedback.getData());
        }
        return new Feedback<>(feedback.getStatusCode(), feedback.getMessenger(), null);


    }

    @PutMapping("/employees/{employeeId}")
    public Feedback<?> updateEmployee(@RequestBody EmployeeDto employee, @PathVariable UUID employeeId) {
        return employeeService.update(employee, employeeId);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee on id - " + employeeId;
    }

    @GetMapping("/employees/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<EmployeeDto> listEmployees = employeeService.findAll();
        ExportFile generator = new ExportFile(listEmployees);
        generator.export(response);
    }

    @GetMapping("/employees/export-excel")
    public Feedback<Boolean> exportIntoExcelFile(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=employee" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            List<EmployeeDto> employees = employeeService.findAll();
            ImportExportExcelUtil generator = new ImportExportExcelUtil(employees);
            generator.exportExcelFile(response);
            return new Feedback<>(SUCCESS.getCode(), SUCCESS.getMessage(), true);
        } catch (IOException e) {
            return new Feedback<>(NOT_SUCCESS.getCode(), NOT_SUCCESS.getMessage(), false);
        }

    }

    @PostMapping("/employees/import-excel")
    public ResponseEntity<List<String>> importEmployee(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.importExcel(file));
    }
}
