package com.globits.da.validate;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.WardRepository;
import com.globits.da.utils.NotifyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidateEmployee {
    private static EmployeeRepository employeeRepository;

    private static ProvinceRepository provinceRepository;

    private static DistrictRepository districtRepository;

    private static WardRepository wardRepository;

    @Autowired
    public ValidateEmployee(EmployeeRepository theEmployeeRepository, ProvinceRepository theProvinceRepository, DistrictRepository theDistrictRepository, WardRepository theWardRepository) {
        employeeRepository = theEmployeeRepository;
        provinceRepository = theProvinceRepository;
        districtRepository = theDistrictRepository;
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
        } else if (employeeRepository.existsEmployeeByCode(code)) {
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

    public static NotifyMessage validateEmail(String email) {
        if (!ValidateGlobal.checkEmailNull(email).equals(success)) {
            return NotifyMessage.EMAIL_IS_NULL;
        } else if (!ValidateGlobal.checkEmailValid(email).equals(success)) {
            return NotifyMessage.EMAIL_INVALID;
        }
        return success;
    }

    public static NotifyMessage validatePhone(String phone) {
        if (!ValidateGlobal.checkPhoneNull(phone).equals(success)) {
            return NotifyMessage.PHONE_IS_NULL;
        } else if (!ValidateGlobal.checkLengthPhone(phone).equals(success)) {
            return NotifyMessage.LENGTH_PHONE_INVALID;
        } else if (!ValidateGlobal.checkPhoneValid(phone).equals(success)) {
            return NotifyMessage.CHARACTER_PHONE_INVALID;
        }
        return success;
    }

    public static NotifyMessage validateAge(int age) {
        if (ValidateGlobal.checkAgeValid(age).equals(success)) {
            return NotifyMessage.AGE_INVALID;
        }
        return success;
    }

    public static NotifyMessage validateEmployee(EmployeeDto employeeDto) {
        NotifyMessage isValidCode = ValidateEmployee.validateCode(employeeDto.getCode());
        NotifyMessage isValidName = ValidateEmployee.validateName(employeeDto.getName());
        NotifyMessage isValidEmail = ValidateEmployee.validateEmail(employeeDto.getEmail());
        NotifyMessage isValidAge = ValidateEmployee.validateAge(employeeDto.getAge());
        NotifyMessage isValidPhone = ValidateEmployee.validatePhone(employeeDto.getPhone());
        NotifyMessage isValidProvince = validateProvince(employeeDto);
        NotifyMessage isValidDistrict = validateDistrict(employeeDto);
        NotifyMessage isValidCommune = validateWard(employeeDto);
        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        } else if (!isValidEmail.equals(success)) {
            return isValidEmail;
        } else if (!isValidPhone.equals(success)) {
            return isValidPhone;
        } else if (!isValidAge.equals(success)) {
            return isValidAge;
        } else if (!isValidProvince.equals(success)) {
            return isValidProvince;
        } else if (!isValidDistrict.equals(success)) {
            return isValidDistrict;
        } else if (!isValidCommune.equals(success)) {
            return isValidCommune;
        }
        return success;
    }

    public static NotifyMessage validateEmployee(EmployeeDto employeeDto, int index) {
        switch (index) {
            case 0:
                NotifyMessage isValidCode = validateCode(employeeDto.getCode());
                if (!isValidCode.equals(success))
                    return isValidCode;
                break;
            case 1:
                NotifyMessage isValidName = validateName(employeeDto.getName());
                if (!isValidName.equals(success))
                    return isValidName;
                break;
            case 2:
                NotifyMessage isValidEmail = validateEmail(employeeDto.getEmail());
                if (!isValidEmail.equals(success))
                    return isValidEmail;
                break;
            case 3:
                NotifyMessage isValidPhone = validatePhone(employeeDto.getPhone());
                if (!isValidPhone.equals(success))
                    return isValidPhone;
                break;
            case 4:
                NotifyMessage isValidAge = validateAge(employeeDto.getAge());
                if (!isValidAge.equals(success))
                    return isValidAge;
                break;
            case 5:
                NotifyMessage isValidProvince = validateProvince(employeeDto);
                if (!isValidProvince.equals(success))
                    return isValidProvince;
                break;
            case 6:
                NotifyMessage isValidDistrict = validateDistrict(employeeDto);
                if (!isValidDistrict.equals(success))
                    return isValidDistrict;
                break;
            case 7:
                NotifyMessage isValidWard = validateWard(employeeDto);
                if (!isValidWard.equals(success))
                    return isValidWard;
                break;

        }
        return success;
    }

    public static NotifyMessage validateProvince(EmployeeDto employeeDto) {
        UUID provinceId = employeeDto.getProvinceDto().getId();
        if (provinceRepository.existsProvinceById(provinceId)) {
            return NotifyMessage.SUCCESS;
        } else return NotifyMessage.PROVINCE_ID_NOT_EXIST;
    }

    public static NotifyMessage validateDistrict(EmployeeDto employeeDto) {
        UUID provinceId = employeeDto.getProvinceDto().getId();
        UUID districtId = employeeDto.getDistrictDto().getId();
        if (districtRepository.existsDistrictById(districtId)) {
            if (districtRepository.findDistinctInProvince(provinceId, districtId) == null) {
                return NotifyMessage.DISTRICT_NOT_IN_PROVINCE;
            }
        }
        return NotifyMessage.DISTRICT_NOT_FOUND;
    }

    public static NotifyMessage validateWard(EmployeeDto employeeDto) {
        UUID districtId = employeeDto.getDistrictDto().getId();
        UUID wardId = employeeDto.getWardDto().getId();
        if (wardRepository.existsWardById(wardId)) {
            if (wardRepository.findWardInDistrict(districtId, wardId) == null) {
                return NotifyMessage.WARD_NOT_IN_DISTRICT;
            }
        }
        return NotifyMessage.WARD_NOT_FOUND;
    }
}
