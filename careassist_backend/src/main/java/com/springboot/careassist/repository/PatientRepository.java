package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.PatientRespDto;
import com.springboot.careassist.enums.RiskCategory;
import com.springboot.careassist.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query("""
select p
from Patient p
JOIN p.user u
where u.username = ?1
""")
    Patient getByUsername(String patientUsername);

    @Query("""
select p.id,
p.name,
p.dob,
p.gender,
p.contact,
p.riskCategory,
u.isActive,
u.createdAt
from Patient p
JOIN p.user u
where u.isActive = ?1
""")
    Page<PatientRespDto> findAllActive(boolean isActive, Pageable pageable);

}
