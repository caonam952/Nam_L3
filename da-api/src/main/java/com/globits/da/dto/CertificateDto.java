package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificateDto extends BaseObjectDto {

    private String code;

    private String name;

    private String grantedBy;

    private LocalDate validDate;

    private LocalDate expDate;
}
