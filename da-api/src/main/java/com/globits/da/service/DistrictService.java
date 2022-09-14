package com.globits.da.service;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.Feedback;

import java.util.List;
import java.util.UUID;

public interface DistrictService {
    List<DistrictDto> findAll();

    DistrictDto findById(UUID uuid);

    Feedback<DistrictDto> save(DistrictDto districtDto);

    Feedback<DistrictDto> update(DistrictDto districtDto, UUID uuid);

    void deleteById(UUID uuid);

    List<DistrictDto> findAllDistrictByProvinceId(UUID uuid);
}
