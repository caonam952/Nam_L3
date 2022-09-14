package com.globits.da.repository;

import com.globits.da.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Boolean existsEmployeeByCode(String code);

    boolean existsEmployeeById(UUID uuid);

    Employee getByCode(String code);

    Employee getById(UUID uuid);
}
