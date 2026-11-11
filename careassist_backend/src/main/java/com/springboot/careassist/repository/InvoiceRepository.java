package com.springboot.careassist.repository;

import com.springboot.careassist.dto.response.ClaimInvoiceRespDto;
import com.springboot.careassist.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    @Query("""
select i.invoiceDate as invoiceDate,
i.consultationFee as consultationFee,
i.diagnosticsFee as diagnosticsFee,
i.pharmacyFee as pharmacyFee,
i.roomCharges as roomCharges,
i.tax as tax,
i.totalAmount as totalAmount,
c.id as claimId,
c.diagnosis as diagnosis,
c.claimedAmount as claimedAmount,
c.approvedAmount as approvedAmount
from Invoice i
JOIN i.claim c
where c.id = ?1
""")
    ClaimInvoiceRespDto getClaimByClaimId(Long claimId);

}
