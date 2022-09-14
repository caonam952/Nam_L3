package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.CertificateEmployee;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.CertificateEmployeeDto;
import com.globits.da.dto.Feedback;
import com.globits.da.repository.CertificateEmployeeRepository;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CertificateEmployeeService;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateCertificateEmployee;
import com.globits.da.validate.ValidateGlobal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateEmployeeServiceImpl implements CertificateEmployeeService {

    @Autowired
    ModelMapper modelMapper;

    CertificateEmployeeRepository certificateEmployeeRepository;

    EmployeeRepository employeeRepository;

    ProvinceRepository provinceRepository;

    CertificateRepository certificateRepository;
    private static NotifyMessage success = NotifyMessage.SUCCESS;

    @Autowired
    public CertificateEmployeeServiceImpl(CertificateEmployeeRepository theCertificateEmployeeRepository, EmployeeRepository theEmployeeRepository, ProvinceRepository theProvinceRepository, CertificateRepository theCertificateRepository) {
        certificateEmployeeRepository = theCertificateEmployeeRepository;
        employeeRepository = theEmployeeRepository;
        provinceRepository = theProvinceRepository;
        certificateRepository = theCertificateRepository;
    }


    @Override
    public List<CertificateEmployeeDto> findAll() {
        return certificateEmployeeRepository.findAll()
                .stream().map(certificateEmployee -> modelMapper.map(certificateEmployee, CertificateEmployeeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CertificateEmployeeDto findById(UUID uuid) {
        Optional<CertificateEmployeeDto> result = certificateEmployeeRepository.findById(uuid)
                .map(certificateEmployee -> modelMapper.map(certificateEmployee, CertificateEmployeeDto.class));
        CertificateEmployeeDto certificateEmployeeDto = null;
        if (result.isPresent()) {
            certificateEmployeeDto = result.get();
        } else throw new RuntimeException("Did not find id - " + uuid);
        return certificateEmployeeDto;
    }

    @Override
    public Feedback<CertificateEmployeeDto> save(CertificateEmployeeDto theCertificate) {
        NotifyMessage notifyMessage = ValidateCertificateEmployee.isValidCertificateEmployee(theCertificate, null);

        if (notifyMessage.equals(success)) {
            CertificateEmployee certificateEmployee = new CertificateEmployee();
            certificateEmployee.setExpDate(theCertificate.getExpDate());
            certificateEmployee.setEmployee(modelMapper.map(theCertificate.getEmployeeDto(), Employee.class));
            certificateEmployee.setProvince(modelMapper.map(theCertificate.getProvinceDto(), Province.class));
            certificateEmployee.setCertificate(modelMapper.map(theCertificate.getCertificateDto(), Certificate.class));
            return new Feedback<>(
                    success.getCode(),
                    success.getMessage(),
                    modelMapper.map(certificateEmployeeRepository.save(certificateEmployee), CertificateEmployeeDto.class));
        }
        return new Feedback<>(
                ValidateGlobal.resultStatusCode(notifyMessage),
                notifyMessage.getMessage(),
                theCertificate
        );
    }

    @Override
    public Feedback<CertificateEmployeeDto> update(CertificateEmployeeDto theCertificate, UUID uuid) {
        NotifyMessage notifyMessage = ValidateCertificateEmployee.isValidCertificateEmployee(theCertificate, uuid);
        if (notifyMessage.equals(success)) {
            CertificateEmployee certificateEmployee = certificateEmployeeRepository.getOne(uuid);
            certificateEmployee.setExpDate(theCertificate.getExpDate());
            certificateEmployee.setEmployee(employeeRepository.getOne(theCertificate.getEmployeeDto().getId()));
            certificateEmployee.setProvince(provinceRepository.getOne(theCertificate.getProvinceDto().getId()));
            certificateEmployee.setCertificate(certificateRepository.getOne(theCertificate.getCertificateDto().getId()));
            return new Feedback<>(
                    success.getCode(),
                    success.getMessage(),
                    modelMapper.map(certificateEmployeeRepository.save(certificateEmployee), CertificateEmployeeDto.class));
        }
        return new Feedback<>(
                notifyMessage.getCode(),
                notifyMessage.getMessage(),
                null);
    }

    @Override
    public void deleteById(UUID uuid) {
        if (certificateRepository.existsCertificateById(uuid)) {
            certificateRepository.deleteById(uuid);
        }
    }
}
