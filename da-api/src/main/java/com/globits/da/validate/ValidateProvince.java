package com.globits.da.validate;

import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.stereotype.Service;

@Service
public class ValidateProvince {

    static private ProvinceRepository provinceRepository;

    public ValidateProvince(ProvinceRepository theProvinceRepository) {
        provinceRepository = theProvinceRepository;
    }

    private static final NotifyMessage success = NotifyMessage.SUCCESS;

    public static NotifyMessage validateCode(String code) {
        if (!ValidateGlobal.checkCodeHasSpace(code).equals(success)) {
            return NotifyMessage.CHARACTER_CODE_INVALID;
        } else if (!ValidateGlobal.checkCodeNull(code).equals(success)) {
            return NotifyMessage.CODE_IS_NULL;
        } else if (!ValidateGlobal.checkLengthCode(code).equals(success)) {
            return NotifyMessage.LENGTH_CODE_INVALID;
        } else if (provinceRepository.existsProvinceByCode(code)) {
            return NotifyMessage.CODE_IS_EXIST;
        }
        return success;
    }

    public static NotifyMessage validateName(String name) {
        if (!ValidateGlobal.checkNameNull(name).equals(success)) {
            return NotifyMessage.NAME_IS_NULL;
        } else if (provinceRepository.existsProvinceByName(name)) {
            return NotifyMessage.NAME_IS_EXIST;
        }
        return success;
    }

    public static NotifyMessage validateAddProvince(ProvinceDto provinceDto) {
        NotifyMessage isValidCode = ValidateProvince.validateCode(provinceDto.getCode());
        NotifyMessage isValidName = ValidateProvince.validateName(provinceDto.getName());

        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }

    public static NotifyMessage validateUpdateProvince(ProvinceDto provinceDto) {
        NotifyMessage isValidName = ValidateProvince.validateName(provinceDto.getName());

        if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }
}
