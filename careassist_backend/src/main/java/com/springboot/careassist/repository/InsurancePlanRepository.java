package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.InsurancePlanRespDto;
import com.springboot.careassist.model.InsurancePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePlanRepository extends JpaRepository<InsurancePlan,Long> {

    @Query("""
select p.id as planId,
p.name as name,
p.basePremium as basePremium,
p.coverageAmount as coverageAmount,
p.ageMin as ageMin,
p.ageMax as ageMax
from InsurancePlan p
WHERE (?1 IS NULL OR basePremium <= ?1)
AND (?2 IS NULL OR coverageAmount <= ?2)
AND (?3 IS NULL OR ageMin <= ?3)
AND (?3 IS NULL OR ageMax >= ?3)
""")
    Page<InsurancePlanRespDto> findAllPlans(Double basePremium, Double coverageAmount, Integer age, Pageable pageable);
}
