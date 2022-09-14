package com.globits.da.validate;

import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.stereotype.Service;

@Service
public class ValidateDistrict {
    static private DistrictRepository districtRepository;

    public ValidateDistrict(DistrictRepository theDistrictRepository) {
        districtRepository = theDistrictRepository;
    }

    private static final NotifyMessage success = NotifyMessage.SUCCESS;


    public static NotifyMessage validateCode(String code) {
        if (!ValidateGlobal.checkCodeHasSpace(code).equals(success)) {
            return NotifyMessage.CHARACTER_CODE_INVALID;
        } else if (!ValidateGlobal.checkCodeNull(code).equals(success)) {
            return NotifyMessage.CODE_IS_NULL;
        } else if (!ValidateGlobal.checkLengthCode(code).equals(success)) {
            return NotifyMessage.LENGTH_CODE_INVALID;
        } else if (districtRepository.existsDistrictByCode(code)) {
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

    public static NotifyMessage validateAddDistrict(DistrictDto districtDto) {

        NotifyMessage isValidCode = ValidateDistrict.validateCode(districtDto.getCode());
        NotifyMessage isValidName = ValidateDistrict.validateName(districtDto.getName());

        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }

    public static NotifyMessage validateUpdateDistrict(DistrictDto districtDto) {
        NotifyMessage isValidName = ValidateDistrict.validateName(districtDto.getName());

        if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }
}
