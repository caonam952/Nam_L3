package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DistrictDto extends BaseObjectDto {
    //
//    @NotEmpty(message = "Code is mandatory")
//    @Size(min = 6, max = 10)
    private String code;

    //    @NotEmpty(message = "Name is mandatory")
    private String name;

    private ProvinceDto province;

    private List<WardDto> wardDtos = new ArrayList<>();
}
