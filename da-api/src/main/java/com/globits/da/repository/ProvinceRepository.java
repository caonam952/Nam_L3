package com.globits.da.repository;

import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    Province getById(UUID id);

    boolean existsProvinceByCode(String code);

    boolean existsProvinceById(UUID id);

    boolean existsProvinceByName(String name);


}
