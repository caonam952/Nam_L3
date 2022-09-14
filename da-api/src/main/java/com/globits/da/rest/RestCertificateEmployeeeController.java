package com.globits.da.rest;

import com.globits.da.dto.CertificateEmployeeDto;
import com.globits.da.dto.Feedback;
import com.globits.da.service.CertificateEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestCertificateEmployeeeController {
    private final CertificateEmployeeService certificateEmployeeService;

    @Autowired
    public RestCertificateEmployeeeController(CertificateEmployeeService theCertificateEmployeeService) {
        certificateEmployeeService = theCertificateEmployeeService;
    }

    @GetMapping("/certificate-employees/list")
    public List<CertificateEmployeeDto> findAll() {
        return certificateEmployeeService.findAll();
    }

    @GetMapping("/certificate-employees/{id}")
    public CertificateEmployeeDto getCertificateEmployee(@PathVariable UUID id) {
        CertificateEmployeeDto certificateEmployeeDto = certificateEmployeeService.findById(id);

        if (certificateEmployeeDto == null) {
            throw new RuntimeException("Id not found - " + id);
        }
        return certificateEmployeeDto;
    }

    @PostMapping("/certificate-employees")
    public Feedback<?> addCertificateEmployee(@RequestBody CertificateEmployeeDto certificateEmployee) {
        return certificateEmployeeService.save(certificateEmployee);
    }

    @PutMapping("/certificate-employees/{id}")
    public Feedback<?> updateCertificateEmployee(@RequestBody CertificateEmployeeDto certificateEmployee, @PathVariable UUID id) {
        return certificateEmployeeService.update(certificateEmployee, id);
    }

    @DeleteMapping("/certificate-employees/{id}")
    public String deleteCertificateEmployee(@PathVariable UUID id) {
        CertificateEmployeeDto certificateEmployee = certificateEmployeeService.findById(id);
        if (certificateEmployee == null) {
            throw new RuntimeException("Id not found - " + id);
        }
        certificateEmployeeService.deleteById(id);
        return "Deleted Certificate on id - " + id;
    }
}
