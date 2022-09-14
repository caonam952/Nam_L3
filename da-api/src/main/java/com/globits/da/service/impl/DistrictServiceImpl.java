package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.domain.Ward;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.Feedback;
import com.globits.da.dto.WardDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.WardRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateDistrict;
import com.globits.da.validate.ValidateGlobal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    ModelMapper modelMapper;

    private final DistrictRepository districtRepository;

    private final ProvinceRepository provinceRepository;

    final private WardRepository wardRepository;

    private final static NotifyMessage success = NotifyMessage.SUCCESS;

    @Autowired
    public DistrictServiceImpl(DistrictRepository theDistrictRepository, ProvinceRepository theProvinceRepository, WardRepository theWardRepository) {
        districtRepository = theDistrictRepository;
        provinceRepository = theProvinceRepository;
        wardRepository = theWardRepository;
    }

    @Override
    public List<DistrictDto> findAll() {
        return districtRepository.findAll()
                .stream().map(district -> modelMapper.map(district, DistrictDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DistrictDto findById(UUID uuid) {
        Optional<DistrictDto> result = districtRepository.findById(uuid)
                .map(district -> modelMapper.map(district, DistrictDto.class));
        DistrictDto tempDistrict = null;
        if (result.isPresent()) {
            tempDistrict = result.get();
        } else {
            throw new RuntimeException("Did not find district id - " + uuid);
        }
        return tempDistrict;
    }

    @Override
    public Feedback<DistrictDto> save(DistrictDto districtDto) {
        NotifyMessage notifyMessage = ValidateDistrict.validateAddDistrict(districtDto);
        if (notifyMessage.equals(success)) {
            District district = new District();
            district.setCode(districtDto.getCode());
            district.setName(districtDto.getName());
            district.setProvince(provinceRepository.getById(districtDto.getProvince().getId()));
            if (districtDto.getWardDtos() != null) {
                district.setWards(initWard(districtDto.getWardDtos(), district));
            }
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    modelMapper.map(districtRepository.save(district), DistrictDto.class));
        }
        return new Feedback<>(
                ValidateGlobal.resultStatusCode(notifyMessage),
                notifyMessage.getMessage(),
                districtDto);
    }

    @Override
    public Feedback<DistrictDto> update(DistrictDto districtDto, UUID uuid) {
        if (districtRepository.existsDistrictById(uuid)) {
            NotifyMessage notifyMessage = ValidateDistrict.validateUpdateDistrict(districtDto);
            District district = districtRepository.getOne(uuid);
            district.setCode(districtDto.getCode());
            district.setName(districtDto.getName());
            district.setProvince(modelMapper.map(districtDto.getProvince().getId(), Province.class));
            if (!districtDto.getWardDtos().isEmpty()) {
                for (WardDto wardDto : districtDto.getWardDtos()) {
                    wardRepository.findById(wardDto.getId())
                            .map(ward -> {
                                ward.setCode(wardDto.getCode());
                                ward.setName(wardDto.getName());
                                ward.setDistrict(district);
                                return ward;
                            });
                }
            }
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    modelMapper.map(districtRepository.save(district), DistrictDto.class));
        }
        return new Feedback<>(
                NotifyMessage.ID_NOT_EXIST.getCode(),
                NotifyMessage.ID_NOT_EXIST.getMessage(),
                districtDto);
    }

    @Override
    public void deleteById(UUID uuid) {
        if (districtRepository.existsDistrictById(uuid)) {
            districtRepository.deleteById(uuid);
        }
    }

    @Override
    public List<DistrictDto> findAllDistrictByProvinceId(UUID uuid) {
        return districtRepository.findAllDistrictByProvinceId(uuid)
                .stream().map(district -> modelMapper.map(district, DistrictDto.class))
                .collect(Collectors.toList());
    }

    public List<Ward> initWard(List<WardDto> wardDtos, District district) {
        if (!wardDtos.isEmpty() && district != null) {
            return wardDtos.stream()
                    .map(wardDto -> {
                        Ward ward = new Ward();
                        ward.setCode(wardDto.getCode());
                        ward.setName(wardDto.getName());
                        ward.setDistrict(district);
                        return ward;
                    }).collect(Collectors.toList());
        }
        return null;
    }

}
