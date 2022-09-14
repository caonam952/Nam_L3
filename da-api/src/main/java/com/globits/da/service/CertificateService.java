package com.globits.da.service;

import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.Feedback;

import java.util.List;
import java.util.UUID;

public interface CertificateService {
    List<CertificateDto> findAll();

    CertificateDto findById(UUID uuid);

    Feedback<CertificateDto> save(CertificateDto theCertificate);

    Feedback<CertificateDto> update(CertificateDto theCertificate, UUID uuid);

    void deleteById(UUID uuid);

}
