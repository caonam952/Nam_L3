package com.globits.da.validate;

import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateCertificate {
    private static CertificateRepository certificateRepository;

    @Autowired
    public ValidateCertificate(CertificateRepository theCertificateRepository) {
        certificateRepository = theCertificateRepository;
    }

    private static final NotifyMessage success = NotifyMessage.SUCCESS;

    public static NotifyMessage validateCode(String code) {
        if (!ValidateGlobal.checkCodeHasSpace(code).equals(success)) {
            return NotifyMessage.CHARACTER_CODE_INVALID;
        } else if (!ValidateGlobal.checkCodeNull(code).equals(success)) {
            return NotifyMessage.CODE_IS_NULL;
        } else if (!ValidateGlobal.checkLengthCode(code).equals(success)) {
            return NotifyMessage.LENGTH_CODE_INVALID;
        } else if (certificateRepository.existsCertificateByCode(code)) {
            return NotifyMessage.CODE_IS_EXIST;
        }
        return success;
    }

    public static NotifyMessage validateName(String name) {
        if (!ValidateGlobal.checkNameNull(name).equals(success)) {
            return NotifyMessage.NAME_IS_NULL;
        }
        return success;
    }

    public static NotifyMessage validateCertificate(CertificateDto certificateDto) {
        NotifyMessage isValidCode = ValidateCertificate.validateCode(certificateDto.getCode());
        NotifyMessage isValidName = ValidateCertificate.validateName(certificateDto.getName());
        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }
}
