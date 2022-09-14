package com.globits.da.service;

import com.globits.da.dto.CertificateEmployeeDto;
import com.globits.da.dto.Feedback;

import java.util.List;
import java.util.UUID;

public interface CertificateEmployeeService {

    List<CertificateEmployeeDto> findAll();

    CertificateEmployeeDto findById(UUID uuid);

    Feedback<CertificateEmployeeDto> save(CertificateEmployeeDto theCertificate);

    Feedback<CertificateEmployeeDto> update(CertificateEmployeeDto theCertificate, UUID uuid);

    void deleteById(UUID uuid);

}
