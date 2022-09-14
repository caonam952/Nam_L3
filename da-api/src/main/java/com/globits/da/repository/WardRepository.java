package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Ward;
import com.globits.da.dto.WardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    Ward getById(UUID uuid);

    List<Ward> findAllWardByDistrictId(UUID uuid);

    boolean existsWardByCode(String code);

    boolean existsWardById(UUID uuid);

    @Query("SELECT d from District as d ,Ward  as c WHERE d.id = ?1 AND c.district.id = ?1 AND c.id = ?2")
    District findWardInDistrict(UUID districtId, UUID wardId);
}
