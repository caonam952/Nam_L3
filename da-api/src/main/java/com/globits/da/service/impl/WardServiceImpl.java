package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Ward;
import com.globits.da.dto.Feedback;
import com.globits.da.dto.WardDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.WardRepository;
import com.globits.da.service.WardService;
import com.globits.da.utils.NotifyMessage;
import com.globits.da.validate.ValidateGlobal;
import com.globits.da.validate.ValidateWard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    ModelMapper modelMapper;

    private final WardRepository wardRepository;

    final private DistrictRepository districtRepository;

    @Autowired
    public WardServiceImpl(WardRepository theWardRepository, DistrictRepository theDistrictRepository) {
        wardRepository = theWardRepository;
        districtRepository = theDistrictRepository;
    }

    @Override
    public List<WardDto> findAll() {
        return wardRepository.findAll()
                .stream()
                .map(ward -> modelMapper.map(ward, WardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WardDto findById(UUID uuid) {
        Optional<WardDto> result = wardRepository.findById(uuid)
                .map(ward -> modelMapper.map(ward, WardDto.class));
        WardDto tempWard = null;
        if (result.isPresent()) {
            tempWard = result.get();
        } else {
            throw new RuntimeException("Did not find ward id - " + uuid);
        }
        return tempWard;
    }

    @Override
    public Feedback<WardDto> save(WardDto theWard) {
        NotifyMessage success = NotifyMessage.SUCCESS;
        NotifyMessage notifyMessage = ValidateWard.validateAddWard(theWard);
        if (notifyMessage.equals(success)) {
            Ward ward = new Ward();
            ward.setCode(theWard.getCode());
            ward.setName(theWard.getName());
            if (districtRepository.existsDistrictById(theWard.getDistrict().getId())) {
                ward.setDistrict(modelMapper.map(theWard.getDistrict().getId(), District.class));
            } else return new Feedback<>(
                    NotifyMessage.CODE_IS_NULL.getCode(),
                    NotifyMessage.EMAIL_IS_NULL.getMessage(),
                    null);
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    modelMapper.map(wardRepository.save(ward), WardDto.class));
        }
        return new Feedback<>(
                ValidateGlobal.resultStatusCode(notifyMessage),
                notifyMessage.getMessage(),
                theWard);
    }

    @Override
    public Feedback<WardDto> update(WardDto theWard, UUID uuid) {
        if (wardRepository.existsWardById(uuid)) {
            NotifyMessage success = NotifyMessage.SUCCESS;
            NotifyMessage notifyMessage = ValidateWard.validateUpdateWard(theWard);
            if (notifyMessage.equals(success)) {
                Ward ward = wardRepository.getOne(uuid);
                ward.setCode(theWard.getCode());
                ward.setName(theWard.getName());
                return new Feedback<>(
                        ValidateGlobal.resultStatusCode(notifyMessage),
                        notifyMessage.getMessage(),
                        modelMapper.map(wardRepository.save(ward), WardDto.class));
            }
            return new Feedback<>(
                    ValidateGlobal.resultStatusCode(notifyMessage),
                    notifyMessage.getMessage(),
                    theWard);
        } else return new Feedback<>(
                NotifyMessage.ID_NOT_EXIST.getCode(),
                NotifyMessage.ID_NOT_EXIST.getMessage(),
                theWard);
    }

    @Override
    public void deleteById(UUID uuid) {
        if (wardRepository.existsWardById(uuid)) {
            wardRepository.deleteById(uuid);
        }
    }

    @Override
    public List<WardDto> findAllWardByDistrictId(UUID uuid) {
        return wardRepository.findAllWardByDistrictId(uuid)
                .stream().map(ward -> modelMapper.map(ward, WardDto.class))
                .collect(Collectors.toList());
    }
}
