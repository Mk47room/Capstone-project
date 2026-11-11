package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.ClaimRespDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.model.Claim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim,Long> {

    @Query("""
select c.id as claimId,
c.claimType as claimType,
c.status as status,
c.diagnosis as diagnosis,
c.treatment as treatment,
c.submittedDate as submittedDate,
c.claimedAmount as claimedAmount,
c.approvedAmount as approvedAmount,
c.rejectedReason as rejectedReason,
p.id as policyId,
CASE
WHEN c.claimType = "CASHLESS"
 THEN h.name
ELSE c.hospitalName
END as hospitalName,
pt.name as patientName,
pt.riskCategory as riskCategory
from Claim c
LEFT JOIN c.policy p
LEFT JOIN c.healthCareProvider h
JOIN p.patient pt
where (?1 IS NULL OR c.claimType = ?1)
AND (?2 IS NULL OR c.status = ?2)
AND (?3 IS NULL OR c.submittedDate >= ?3)
AND (?4 IS NULL OR c.submittedDate <= ?4)
""")
    Page<ClaimRespDto> dynamicFilterClaims(ClaimType claimType, ClaimStatus claimStatus, Instant fromDate, Instant toDate, Pageable pageable);

    @Query("""
select c.id as claimId,
c.claimType as claimType,
c.claimedAmount as claimedAmount,
c.approvedAmount as approvedAmount,
c.status as status,
c.diagnosis as diagnosis,
c.submittedDate as submittedDate,
CASE
    WHEN c.claimType = 'CASHLESS'
    THEN h.name
    ELSE c.hospitalName
END as hospitalName
from Claim c
JOIN c.policy p
JOIN p.patient pt
JOIN pt.user u
LEFT JOIN c.healthCareProvider h
where u.username = ?1
AND (?2 IS NULL OR c.claimType = ?2)
AND (?3 IS NULL OR c.status = ?3)
AND (?4 IS NULL OR c.submittedDate >= ?4)
AND (?5 IS NULL OR c.submittedDate <= ?5)
""")
    Page<ClaimRespDtoV2> getClaims(String patientUsername,ClaimType claimType, ClaimStatus claimStatus, Instant fromDate, Instant toDate, Pageable pageable);

    @Query("""
select c.id as claimId,
c.claimType as claimType,
c.claimedAmount as claimedAmount,
c.approvedAmount as approvedAmount,
c.status as status,
c.diagnosis as diagnosis,
c.submittedDate as submittedDate,
h.name as hospitalName
from Claim c
JOIN c.policy p
JOIN c.healthCareProvider h
JOIN h.user u
where u.username = ?1
""")
    List<ClaimRespDtoV2> getClaimsByProvider(String providerUsername, Pageable pageable);
}
