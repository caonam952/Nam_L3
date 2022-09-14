package com.globits.da.rest;

import com.globits.da.dto.Feedback;
import com.globits.da.dto.WardDto;
import com.globits.da.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestWardController {
    final private WardService wardService;

    @Autowired
    public RestWardController(WardService theWardService) {
        wardService = theWardService;
    }

    @GetMapping("/wards/list")
    public List<WardDto> findAll() {
        return wardService.findAll();
    }

    @GetMapping("/wards/{wardId}")
    public WardDto getWard(@PathVariable UUID wardId) {
        WardDto ward = wardService.findById(wardId);

        if (ward == null) {
            throw new RuntimeException("Ward id not found - " + wardId);
        }
        return ward;
    }

    @PostMapping("/wards")
    public Feedback<?> addWard(@RequestBody WardDto ward) {
//        ward.setId(null);
        return wardService.save(ward);
    }

    @PutMapping("/wards/{wardId}")
    public Feedback<?> updateWard(@RequestBody WardDto ward, @PathVariable UUID wardId){
        return wardService.update(ward, wardId);
    }

    @DeleteMapping("/wards/{wardId}")
    public String deleteWard(@PathVariable UUID wardId) {
        WardDto ward = wardService.findById(wardId);
        if (ward == null) {
            throw new RuntimeException("Ward id not found - " + wardId);
        }
        wardService.deleteById(wardId);
        return "Deleted ward on id - " + wardId;
    }
}
