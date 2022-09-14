package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {
    List<District> findAllDistrictByProvinceId(UUID uuid);

    District getById(UUID id);

    boolean existsDistrictByCode(String code);

    boolean existsDistrictById(UUID uuid);

    @Query("SELECT p from Province as p ,District as d WHERE p.id = ?1 AND d.province.id = ?1  AND d.id=?2")
    Province findDistinctInProvince(UUID provinceId, UUID districtId);

}
