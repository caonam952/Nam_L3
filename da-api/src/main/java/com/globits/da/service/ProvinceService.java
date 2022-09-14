package com.globits.da.service;

import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.Feedback;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public interface ProvinceService {

    List<ProvinceDto> findAll();

    ProvinceDto findById(UUID uuid);

    Feedback<ProvinceDto> save(ProvinceDto theProvince);

    Feedback<ProvinceDto> update(ProvinceDto theProvince, UUID uuid);

    void deleteById(UUID uuid);

}
