package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDto extends BaseObjectDto {

    //    @NotEmpty(message = "Code is mandatory")
//    @Size(min = 6, max = 10)
    private String code;

    //    @NotEmpty(message = "Name is mandatory")
    private String name;

    //    @Email(message = "Email is not valid")
//    @NotEmpty(message = "Email is mandatory")
    private String email;

    //    @NotEmpty(message = "Phone is mandatory")
//    @Digits(integer = 11, fraction = 0, message = "Phone input is not valid. Please input all 11 numbers")
    private String phone;

    //    @NotNull(message = "Age is mandatory")
//    @Min(value = 0)
//    @Digits(integer = 3, fraction = 0)
    private Integer age;

    private ProvinceDto provinceDto;

    private DistrictDto districtDto;

    private WardDto wardDto;
    private static ModelMapper modelMapper = new ModelMapper();

    public static EmployeeDto toDto(Employee employee) {
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        dto.setProvinceDto(modelMapper.map(employee.getProvince(), ProvinceDto.class));
        dto.setDistrictDto(modelMapper.map(employee.getDistrict(), DistrictDto.class));
        dto.setWardDto(modelMapper.map(employee.getWard(), WardDto.class));
        return dto;
    }

}
