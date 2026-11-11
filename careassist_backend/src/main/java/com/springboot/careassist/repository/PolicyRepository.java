package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.PolicyRespDto;
import com.springboot.careassist.model.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,Long> {


    @Query("""
        select p.id as policyId,
        p.startingDate as startingDate,
        p.endingDate as endingDate,
        p.premiumAmount as premiumAmount,
        p.coverageAmount as coverageAmount,
        i.id as InsurancePlanId,
        i.name as InsurancePlanName
        from Policy p
        Left join p.insurancePlan i
        JOIN p.patient pt
        JOIN pt.user u
        where u.username = ?1
        """)
    Page<PolicyRespDto> getAllPolicyByPatient(String patientUsername, Pageable pageable);
}
