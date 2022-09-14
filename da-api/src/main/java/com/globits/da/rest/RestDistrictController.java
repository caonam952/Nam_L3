package com.globits.da.rest;


import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.Feedback;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestDistrictController {
    private final DistrictService districtService;

    @Autowired
    public RestDistrictController(DistrictService theDistrictService) {
        districtService = theDistrictService;
    }

    @GetMapping("/districts/list")
    public List<DistrictDto> findAll() {
        return districtService.findAll();
    }

    @GetMapping("/districts/{districtId}")
    public DistrictDto getDistrict(@PathVariable UUID districtId) {
        DistrictDto district = districtService.findById(districtId);

        if (district == null) {
            throw new RuntimeException("District id not found - " + districtId);
        }
        return district;
    }

    @PostMapping("/districts")
    public Feedback<?> addDistrict(@RequestBody DistrictDto district) {
//        district.setId(null);
        return districtService.save(district);
    }

    @PutMapping("/districts/{districtId}")
    public Feedback<?> updateDistrict(@RequestBody DistrictDto district, @PathVariable UUID districtId) {
        return districtService.update(district, districtId);
    }

    @DeleteMapping("/districts/{districtId}")
    public String deleteDistrict(@PathVariable UUID districtId) {
        DistrictDto district = districtService.findById(districtId);
        if (district == null) {
            throw new RuntimeException("District id not found - " + districtId);
        }
        districtService.deleteById(districtId);
        return "Deleted district on id - " + districtId;
    }

    @GetMapping("/districts/listbyprovince/{provinceId}")
    public List<DistrictDto> findAllDistrictByProvinceId(@PathVariable UUID provinceId) {
        return districtService.findAllDistrictByProvinceId(provinceId);
    }
}
