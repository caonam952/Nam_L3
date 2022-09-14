package com.globits.da.validate;

import com.globits.da.Constants;
import com.globits.da.utils.NotifyMessage;

import java.util.regex.Matcher;

public class ValidateGlobal {

    public static NotifyMessage checkCodeNull(String code) {
        if (code == null || code.isEmpty()) {
            return NotifyMessage.CODE_IS_NULL;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkLengthCode(String code) {
        if (code.length() < Constants.MIN_LENGTH_CODE || code.length() > Constants.MAX_LENGTH_CODE) {
            return NotifyMessage.LENGTH_CODE_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkCodeHasSpace(String code) {
        if (code.contains(Constants.SPACE)) {
            return NotifyMessage.CHARACTER_CODE_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkNameNull(String name) {
        if (name == null || name.isEmpty()) {
            return NotifyMessage.NAME_IS_NULL;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkEmailNull(String email) {
        if (email == null || email.isEmpty()) {
            return NotifyMessage.EMAIL_IS_NULL;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkEmailValid(String email) {
        Matcher matcher = Constants.REGEX_VALID_EMAIL.matcher(email);
        if (!matcher.find()) {
            return NotifyMessage.EMAIL_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkPhoneNull(String phone) {
        if (phone == null || phone.isEmpty()) {
            return NotifyMessage.PHONE_IS_NULL;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkLengthPhone(String phone) {
        if (phone.length() != Constants.LENGTH_PHONE) {
            return NotifyMessage.LENGTH_PHONE_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkPhoneValid(String phone) {
        if (!phone.matches(Constants.REGEX_VALID_PHONE)) {
            return NotifyMessage.CHARACTER_PHONE_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static NotifyMessage checkAgeValid(int age) {
        if (age > 0) {
            return NotifyMessage.AGE_INVALID;
        }
        return NotifyMessage.SUCCESS;
    }

    public static int resultStatusCode(NotifyMessage notifyMessage) {
        switch (notifyMessage) {
            case ID_IS_EXIST:
                return NotifyMessage.ID_IS_EXIST.getCode();
            case CODE_IS_EXIST:
                return NotifyMessage.CODE_IS_EXIST.getCode();
            case CODE_IS_NULL:
                return NotifyMessage.CODE_IS_NULL.getCode();
            case CHARACTER_CODE_INVALID:
                return NotifyMessage.CHARACTER_CODE_INVALID.getCode();
            case LENGTH_CODE_INVALID:
                return NotifyMessage.LENGTH_CODE_INVALID.getCode();
            case NAME_IS_EXIST:
                return NotifyMessage.NAME_IS_EXIST.getCode();
            case NAME_IS_NULL:
                return NotifyMessage.NAME_IS_NULL.getCode();
            case PROVINCE_ID_NOT_EXIST:
                return NotifyMessage.PROVINCE_ID_NOT_EXIST.getCode();
            case EMAIL_IS_NULL:
                return NotifyMessage.EMAIL_IS_NULL.getCode();
            case EMAIL_INVALID:
                return NotifyMessage.EMAIL_INVALID.getCode();
            case AGE_INVALID:
                return NotifyMessage.AGE_INVALID.getCode();
            case PHONE_IS_NULL:
                return NotifyMessage.PHONE_IS_NULL.getCode();
            case LENGTH_PHONE_INVALID:
                return NotifyMessage.LENGTH_PHONE_INVALID.getCode();
            case CHARACTER_PHONE_INVALID:
                return NotifyMessage.CHARACTER_PHONE_INVALID.getCode();
            case WARD_IS_NULL:
                return NotifyMessage.WARD_IS_NULL.getCode();
            case WARD_NOT_FOUND:
                return NotifyMessage.WARD_NOT_FOUND.getCode();
            case WARD_NOT_IN_DISTRICT:
                return NotifyMessage.WARD_NOT_IN_DISTRICT.getCode();
            case DISTRICT_IS_NULL:
                return NotifyMessage.DISTRICT_IS_NULL.getCode();
            case DISTRICT_NOT_FOUND:
                return NotifyMessage.DISTRICT_NOT_FOUND.getCode();
            case DISTRICT_NOT_IN_PROVINCE:
                return NotifyMessage.DISTRICT_NOT_IN_PROVINCE.getCode();
            case PROVINCE_IS_NULL:
                return NotifyMessage.PROVINCE_IS_NULL.getCode();
            case PROVINCE_NOT_FOUND:
                return NotifyMessage.PROVINCE_NOT_FOUND.getCode();
            case EMPLOYEE_IS_NULL:
                return NotifyMessage.EMPLOYEE_IS_NULL.getCode();
            case EMPLOYEE_NOT_FOUND:
                return NotifyMessage.EMPLOYEE_NOT_FOUND.getCode();
            case CERTIFICATE_NOT_FOUND:
                return NotifyMessage.CERTIFICATE_NOT_FOUND.getCode();
            case CERTIFICATE_HAS_EFFECT:
                return NotifyMessage.CERTIFICATE_HAS_EFFECT.getCode();
            case NUMBER_CERTIFICATE_EXCEED:
                return NotifyMessage.NUMBER_CERTIFICATE_EXCEED.getCode();
            case FILE_EXCEL_IS_Blank:
                return NotifyMessage.FILE_EXCEL_IS_Blank.getCode();
            case EXPORT_FILE_FAIL:
                return NotifyMessage.EXPORT_FILE_FAIL.getCode();
            case LIST_IS_EMPTY:
                return NotifyMessage.LIST_IS_EMPTY.getCode();
            case EMAIL_IS_EXIST:
                return NotifyMessage.EMAIL_IS_EXIST.getCode();
            case NOT_SUCCESS:
                return NotifyMessage.NOT_SUCCESS.getCode();
            case SUCCESS:
                return NotifyMessage.SUCCESS.getCode();

        }
        return 0;
    }
}
