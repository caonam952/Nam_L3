package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public interface EmployeeService {
    List<EmployeeDto> findAll();

    Employee findById(UUID uuid);

    Feedback<EmployeeDto> saveEmployee(EmployeeDto theEmployee);

    Feedback<EmployeeDto> update(EmployeeDto theEmployee, UUID uuid);

    void deleteById(UUID uuid);

    List<String> importExcel(MultipartFile multipartFile) throws IOException;

    List<EmployeeDto> exportExcel() throws IOException;

}
