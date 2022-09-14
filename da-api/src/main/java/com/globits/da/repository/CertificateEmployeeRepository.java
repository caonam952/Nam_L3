package com.globits.da.repository;

import com.globits.da.domain.CertificateEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CertificateEmployeeRepository extends JpaRepository<CertificateEmployee, UUID> {
    @Query(value = "select count(id) from certificate_employee "
            + "where id != ?1 "
            + "and employee_id = ?2 "
            + "and certificate_id = ?3 "
            + "and province_id = ?4 "
            + "and datediff(curdate(), exp_date) < 0", nativeQuery = true)
    int countValidCertificateByIdInUse(UUID id, UUID employeeId, UUID certificateId , UUID provinceId);

    @Query(value = "select count(id) from certificate_employee "
            + "where id != ?1 "
            + "and employee_id = ?2 "
            + "and certificate_id = ?3 "
            + "and datediff(curdate(), exp_date) < 0", nativeQuery = true)
    int countValidCertificateByEmployeeIdAndCertificateIdInUse(UUID id, UUID employeeId, UUID certificateId);

    @Query(value = "select count(id) from certificate_employee "
            + "where employee_id = ?1 "
            + "and certificate_id = ?2 "
            + "and province_id = ?3 "
            + "and datediff(curdate(), exp_date) < 0", nativeQuery = true)
    int countValidCertificateEmployeeIdAndProvinceIdAndCertificateId(UUID employeeId, UUID certificateId, UUID provinceId);

    @Query(value = "select count(id) from certificate_employee "
            + "where employee_id = ?1 "
            + "and certificate_id = ?2 "
            + "and datediff(curdate(), exp_date) < 0", nativeQuery = true)
    int countValidCertificateByEmployeeIdAndCertificateId(UUID employeeId, UUID certificateId);

}
