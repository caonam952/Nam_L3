package com.globits.da.rest;


import com.globits.da.dto.Feedback;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestProvinceController {
    final private ProvinceService provinceService;

    @Autowired
    public RestProvinceController(ProvinceService theProvinceService) {
        provinceService = theProvinceService;
    }

    @GetMapping("/provinces/list")
    public List<ProvinceDto> findAll() {
        return provinceService.findAll();
    }

    @GetMapping("/provinces/{provinceId}")
    public ProvinceDto getProvince(@PathVariable UUID provinceId) {
        ProvinceDto province = provinceService.findById(provinceId);

        if (province == null) {
            throw new RuntimeException("Province id not found - " + provinceId);
        }

        return province;
    }

    @PostMapping("/provinces")
    public Feedback<?> addProvince(@RequestBody ProvinceDto province) {
        //  province.setId(null);
        return provinceService.save(province);
    }

    @PutMapping("/provinces/{provinceId}")
    public Feedback<?> updateProvince(@RequestBody ProvinceDto province, @PathVariable UUID provinceId) {
        return provinceService.update(province, provinceId);
    }

    @DeleteMapping("/provinces/{provinceId}")
    public String deleteProvince(@PathVariable UUID provinceId) {
        ProvinceDto province = provinceService.findById(provinceId);
        if (province == null) {
            throw new RuntimeException("Province id not found - " + provinceId);
        }
        provinceService.deleteById(provinceId);
        return "Deleted province on id - " + provinceId;
    }
}
