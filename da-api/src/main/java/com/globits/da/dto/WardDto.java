package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WardDto extends BaseObjectDto {

    //    @NotEmpty(message = "Code is mandatory")
//    @Size(min = 6, max = 10)
    private String code;

    //    @NotEmpty(message = "Name is mandatory")
    private String name;

    private DistrictDto district;
}
