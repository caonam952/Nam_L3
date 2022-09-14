package com.globits.da.rest;

import com.globits.da.dto.CertificateDto;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestCertificateController {
    final private CertificateService certificateService;

    @Autowired
    public RestCertificateController(CertificateService theCertificateService) {
        certificateService = theCertificateService;
    }

    @GetMapping("/certificates/list")
    public List<CertificateDto> findAll() {
        return certificateService.findAll();
    }

    @GetMapping("/certificates/{certificatesId}")
    public CertificateDto getCertificates(@PathVariable UUID certificatesId) {
        CertificateDto certificate = certificateService.findById(certificatesId);

        if (certificate == null) {
            throw new RuntimeException("Certificate id not found - " + certificatesId);
        }

        return certificate;
    }

    @PostMapping("/certificates")
    public CertificateDto addCertificate(@RequestBody CertificateDto certificate) {
        certificate.setId(null);
        certificateService.save(certificate);
        return certificate;
    }

    @PutMapping("/certificates/{certificateId}")
    public CertificateDto updateCertificate(@RequestBody CertificateDto certificate, @PathVariable UUID certificateId) {
        certificateService.update(certificate, certificateId);
        return certificate;
    }

    @DeleteMapping("/certificates/{certificateId}")
    public String deleteCertificate(@PathVariable UUID certificateId) {
        CertificateDto certificate = certificateService.findById(certificateId);
        if (certificate == null) {
            throw new RuntimeException("Certificate id not found - " + certificateId);
        }
        certificateService.deleteById(certificateId);
        return "Deleted certificate on id - " + certificateId;
    }

}
