package com.globits.da.validate;

import com.globits.da.Constants;
import com.globits.da.dto.CertificateEmployeeDto;
import com.globits.da.repository.CertificateEmployeeRepository;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
public class ValidateCertificateEmployee {
    static private CertificateEmployeeRepository certificateEmployeeRepository;

    private static CertificateRepository certificateRepository;

    private static ProvinceRepository provinceRepository;

    private static EmployeeRepository employeeRepository;

    @Autowired
    public ValidateCertificateEmployee(CertificateEmployeeRepository theCertificateEmployeeRepository, CertificateRepository theCertificateRepository, ProvinceRepository theProvinceRepository, EmployeeRepository theEmployeeRepository) {
        certificateEmployeeRepository = theCertificateEmployeeRepository;
        certificateRepository = theCertificateRepository;
        provinceRepository = theProvinceRepository;
        employeeRepository = theEmployeeRepository;
    }


    private static final NotifyMessage success = NotifyMessage.SUCCESS;

    public static NotifyMessage checkExistsCertificate(CertificateEmployeeDto certificateEmployeeDto) {
        if (ObjectUtils.isEmpty(certificateEmployeeDto.getCertificateDto().getId())) {
            return NotifyMessage.ID_IS_NULL;
        }
        if (!certificateRepository.existsCertificateById(certificateEmployeeDto.getCertificateDto().getId())) {
            return NotifyMessage.ID_NOT_EXIST;
        }
        return success;
    }

    public static NotifyMessage checkExistsProvince(CertificateEmployeeDto certificateEmployeeDto) {
        if (ObjectUtils.isEmpty(certificateEmployeeDto.getProvinceDto().getId())) {
            return NotifyMessage.ID_IS_NULL;
        }
        if (!provinceRepository.existsProvinceById(certificateEmployeeDto.getProvinceDto().getId())) {
            return NotifyMessage.ID_NOT_EXIST;
        }
        return success;
    }

    public static NotifyMessage checkExistsEmployee(CertificateEmployeeDto certificateEmployeeDto) {
        if (ObjectUtils.isEmpty(certificateEmployeeDto.getEmployeeDto().getId())) {
            return NotifyMessage.ID_IS_NULL;
        }
        if (!employeeRepository.existsEmployeeById(certificateEmployeeDto.getEmployeeDto().getId())) {
            return NotifyMessage.ID_NOT_EXIST;
        }

        return success;
    }

    public static NotifyMessage checkCountValidCertificate(CertificateEmployeeDto certificateEmployeeDto, UUID uuid) {
        UUID employeeId = certificateEmployeeDto.getEmployeeDto().getId();
        UUID certificateId = certificateEmployeeDto.getCertificateDto().getId();
        UUID provinceId = certificateEmployeeDto.getProvinceDto().getId();
        UUID id = uuid;

        if (id == null) {
            if (certificateEmployeeRepository.countValidCertificateEmployeeIdAndProvinceIdAndCertificateId(employeeId, certificateId, provinceId) > 0) {
                return NotifyMessage.CERTIFICATE_HAS_EFFECT;
            } else if (certificateEmployeeRepository.countValidCertificateByEmployeeIdAndCertificateId(employeeId, certificateId) >= Constants.LIMIT_NUMBER_OF_CERTIFICATE) {
                return NotifyMessage.NUMBER_CERTIFICATE_EXCEED;
            }
        } else {
            if (certificateEmployeeRepository.countValidCertificateByIdInUse(id, employeeId, certificateId, provinceId) > 0) {
                return NotifyMessage.CERTIFICATE_HAS_EFFECT;
            } else if (certificateEmployeeRepository.countValidCertificateByEmployeeIdAndCertificateIdInUse(id, employeeId, certificateId) >= Constants.LIMIT_NUMBER_OF_CERTIFICATE) {
                return NotifyMessage.NUMBER_CERTIFICATE_EXCEED;
            }
        }

        return success;
    }

    public static NotifyMessage isValidCertificateEmployee(CertificateEmployeeDto certificateEmployeeDto, UUID uuid) {
        NotifyMessage notifyMessageEmployee = checkExistsEmployee(certificateEmployeeDto);
        NotifyMessage notifyMessageCertificate = checkExistsCertificate(certificateEmployeeDto);
        NotifyMessage notifyMessageProvince = checkExistsProvince(certificateEmployeeDto);

        if (!notifyMessageEmployee.equals(success)) {
            return notifyMessageEmployee;
        }
        if (!notifyMessageCertificate.equals(success)) {
            return notifyMessageCertificate;
        }
        if (!notifyMessageProvince.equals(success)) {
            return notifyMessageProvince;
        }
        if (!checkCountValidCertificate(certificateEmployeeDto, uuid).equals(success)) {
            return checkCountValidCertificate(certificateEmployeeDto, uuid);
        }
        return success;
    }

}
