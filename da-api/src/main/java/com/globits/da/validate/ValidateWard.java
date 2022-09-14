package com.globits.da.validate;

import com.globits.da.dto.WardDto;
import com.globits.da.repository.WardRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateWard {

    static private WardRepository wardRepository;

    @Autowired
    public ValidateWard(WardRepository theWardRepository) {
        wardRepository = theWardRepository;
    }

    private final static NotifyMessage success = NotifyMessage.SUCCESS;


    public static NotifyMessage validateCode(String code) {
        if (!ValidateGlobal.checkCodeHasSpace(code).equals(success)) {
            return NotifyMessage.CHARACTER_CODE_INVALID;
        } else if (!ValidateGlobal.checkCodeNull(code).equals(success)) {
            return NotifyMessage.CODE_IS_NULL;
        } else if (!ValidateGlobal.checkLengthCode(code).equals(success)) {
            return NotifyMessage.LENGTH_CODE_INVALID;
        } else if (wardRepository.existsWardByCode(code)) {
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

    public static NotifyMessage validateAddWard(WardDto wardDto) {
        NotifyMessage isValidCode = ValidateWard.validateCode(wardDto.getCode());
        NotifyMessage isValidName = ValidateWard.validateName(wardDto.getName());

        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }

    public static NotifyMessage validateUpdateWard(WardDto wardDto) {
        NotifyMessage isValidName = ValidateWard.validateName(wardDto.getName());

        if (!isValidName.equals(success)) {
            return isValidName;
        }
        return success;
    }
}
