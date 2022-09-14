package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.domain.Ward;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.Feedback;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.WardDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.WardRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateDistrict;
import com.globits.da.validate.ValidateGlobal;
import com.globits.da.validate.ValidateProvince;
import com.globits.da.validate.ValidateWard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    ModelMapper modelMapper;

    final private ProvinceRepository provinceRepository;

    final private DistrictRepository districtRepository;

    final private WardRepository wardRepository;

//    final private DistrictServiceImpl districtService;

    final private static NotifyMessage success = NotifyMessage.SUCCESS;


    @Autowired
    public ProvinceServiceImpl(ProvinceRepository theProvinceRepository, DistrictRepository theDistrictRepository, WardRepository theWardRepository, DistrictServiceImpl districtService) {
        provinceRepository = theProvinceRepository;
        districtRepository = theDistrictRepository;
        wardRepository = theWardRepository;
//        this.districtService = districtService;
    }

    @Override
    public List<ProvinceDto> findAll() {
        return provinceRepository.findAll()
                .stream()
                .map(province -> modelMapper.map(province, ProvinceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProvinceDto findById(UUID uuid) {
        Optional<ProvinceDto> result = provinceRepository.findById(uuid)
                .map(province -> modelMapper.map(province, ProvinceDto.class));

        ProvinceDto tempProvince;

        if (result.isPresent()) {
            tempProvince = result.get();
        } else {
            throw new RuntimeException("Did not find province id - " + uuid);
        }
        return tempProvince;
    }

    @Override
    public Feedback<ProvinceDto> save(ProvinceDto provinceDto) {
        NotifyMessage notifyMessage = ValidateProvince.validateAddProvince(provinceDto);
        if (notifyMessage.equals(success)) {
            Province province = new Province();
            province.setCode(provinceDto.getCode());
            province.setName(provinceDto.getName());
            if (!ObjectUtils.isEmpty(provinceDto.getDistrictDtos())) {
                province.setDistricts(initDistrict(provinceDto.getDistrictDtos(), province).getData());
            }
            return new Feedback<>(
                    success.getCode(),
                    success.getMessage(),
                    modelMapper.map(provinceRepository.save(province), ProvinceDto.class));
        }
        return new Feedback<>(
                ValidateGlobal.resultStatusCode(notifyMessage),
                notifyMessage.getMessage(),
                provinceDto);
    }

    @Override
    public Feedback<ProvinceDto> update(ProvinceDto provinceDto, UUID uuid) {
        if (provinceRepository.existsProvinceById(uuid)) {
            NotifyMessage notifyMessage = ValidateProvince.validateUpdateProvince(provinceDto);
            if (notifyMessage.equals(success)) {
                Province province = provinceRepository.getOne(uuid);
                province.setCode(provinceDto.getCode());
                province.setName(provinceDto.getName());
                if (!provinceDto.getDistrictDtos().isEmpty()) {
                    for (DistrictDto districtDto : provinceDto.getDistrictDtos()) {
                        districtRepository.findById(districtDto.getId())
                                .map(district -> {
                                    district.setCode(districtDto.getCode());
                                    district.setName(districtDto.getName());
                                    district.setProvince(province);
                                    return district;
                                });
                        for (WardDto wardDto : districtDto.getWardDtos()) {
                            if (!ObjectUtils.isEmpty(wardDto.getId())) {
                                wardRepository.findById(wardDto.getId())
                                        .map(ward -> {
                                            ward.setCode(wardDto.getCode());
                                            ward.setName(wardDto.getName());
                                            return ward;
                                        });
                            }
                        }
                    }

                }
                return new Feedback<>(ValidateGlobal.resultStatusCode(notifyMessage),
                        notifyMessage.getMessage(),
                        modelMapper.map(provinceRepository.save(province), ProvinceDto.class));
            } else return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    provinceDto);
        }
        return new Feedback<>(
                NotifyMessage.PROVINCE_ID_NOT_EXIST.getCode(),
                NotifyMessage.PROVINCE_ID_NOT_EXIST.getMessage(),
                provinceDto);
    }


    @Override
    public void deleteById(UUID uuid) {
        if (provinceRepository.existsProvinceById(uuid)) {
            provinceRepository.deleteById(uuid);
        }
    }

    public Feedback<List<District>> initDistrict(List<DistrictDto> districtDtos, Province province) {
        if (!districtDtos.isEmpty() && province != null) {
            List<District> districts = new ArrayList<>();
            for (DistrictDto districtDto : districtDtos) {
                NotifyMessage notifyMessage = ValidateDistrict.validateAddDistrict(districtDto);
                if (notifyMessage.equals(success)) {
                    District district = new District();
                    district.setCode(districtDto.getCode());
                    district.setName(districtDto.getName());
                    district.setProvince(province);
                    district.setWards(initWard(districtDto.getWardDtos(), district).getData());
                    districts.add(district);
                }

            }
            return new Feedback<>(success.getCode(), success.getMessage(), districts);
        }
        return new Feedback<>(
                NotifyMessage.DISTRICT_NOT_FOUND.getCode(),
                NotifyMessage.DISTRICT_NOT_FOUND.getMessage(),
                null);
    }

    public Feedback<List<Ward>> initWard(List<WardDto> wardDtos, District district) {
        if (!wardDtos.isEmpty() && district != null) {
            List<Ward> wards = new ArrayList<>();
            for (WardDto wardDto : wardDtos) {
                NotifyMessage notifyMessage = ValidateWard.validateAddWard(wardDto);
                if (notifyMessage.equals(success)) {
                    Ward ward = new Ward();
                    ward.setCode(wardDto.getCode());
                    ward.setName(wardDto.getName());
                    ward.setDistrict(district);
                    wards.add(ward);
                }

            }
            return new Feedback<>(success.getCode(), success.getMessage(), wards);
        }
        return new Feedback<>(
                NotifyMessage.WARD_NOT_FOUND.getCode(),
                NotifyMessage.WARD_NOT_FOUND.getMessage(),
                null);
    }
}
