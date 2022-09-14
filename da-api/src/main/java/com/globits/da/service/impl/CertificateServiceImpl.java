package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.Feedback;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateCertificate;
import com.globits.da.validate.ValidateGlobal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    ModelMapper modelMapper;

    CertificateRepository certificateRepository;

    @Autowired
    public CertificateServiceImpl(CertificateRepository theCertificateRepository) {
        certificateRepository = theCertificateRepository;
    }

    private static final NotifyMessage success = NotifyMessage.SUCCESS;

    @Override
    public List<CertificateDto> findAll() {
        return certificateRepository.findAll()
                .stream().map(certificate -> modelMapper.map(certificate, CertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto findById(UUID uuid) {
        Optional<CertificateDto> result = certificateRepository.findById(uuid)
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class));

        CertificateDto tempCertificate = null;
        if (result.isPresent()) {
            tempCertificate = result.get();
        } else {
            throw new RuntimeException("Did not find certificate id - " + uuid);
        }
        return tempCertificate;
    }

    @Override
    public Feedback<CertificateDto> save(CertificateDto theCertificate) {
        NotifyMessage notifyMessage = ValidateCertificate.validateCertificate(theCertificate);
        if (notifyMessage.equals(success)) {
            Certificate certificate = new Certificate();
            certificate.setCode(theCertificate.getCode());
            certificate.setName(theCertificate.getName());
            certificate.setGrantedBy(theCertificate.getGrantedBy());
            certificate.setValidDate(theCertificate.getValidDate());
            certificate.setExpDate(theCertificate.getExpDate());
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    modelMapper.map(certificateRepository.save(certificate), CertificateDto.class));
        }

        return new Feedback<>(ValidateGlobal.resultStatusCode(notifyMessage), notifyMessage.getMessage(), null);
    }

    @Override
    public Feedback<CertificateDto> update(CertificateDto theCertificate, UUID uuid) {
        if (certificateRepository.existsCertificateById(uuid)) {
            NotifyMessage notifyMessage = ValidateCertificate.validateCertificate(theCertificate);
            if (notifyMessage.equals(success)) {
                Certificate certificate = certificateRepository.getOne(uuid);
                certificate.setCode(theCertificate.getCode());
                certificate.setName(theCertificate.getName());
                certificate.setGrantedBy(theCertificate.getGrantedBy());
                certificate.setValidDate(theCertificate.getValidDate());
                certificate.setExpDate(theCertificate.getExpDate());
                return new Feedback<>(
                        ValidateGlobal.resultStatusCode(notifyMessage),
                        notifyMessage.getMessage(),
                        modelMapper.map(certificateRepository.save(certificate), CertificateDto.class));
            }
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    theCertificate);
        }
        return new Feedback<>(
                NotifyMessage.ID_IS_EXIST.getCode(),
                NotifyMessage.ID_IS_EXIST.getMessage(),
                theCertificate);
    }

    @Override
    public void deleteById(UUID uuid) {
        certificateRepository.deleteById(uuid);
    }
}
