package com.springboot.careassist.repository;

import com.springboot.careassist.model.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Long> {

    @Query("""
select cd
from ClaimDocument cd
JOIN cd.claim c
where c.id = ?1
""")
    List<ClaimDocument> findByClaim_Id(Long claimId);
}
