package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.InvoiceReqDto;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.exception.InvalidClaimStatusException;
import com.springboot.careassist.exception.OwnershipException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.mapper.InvoiceMapper;
import com.springboot.careassist.model.Claim;
import com.springboot.careassist.model.Invoice;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClaimRepository claimRepository;

    public void insertInvoice(String providerName, Long claimId, InvoiceReqDto dto) {
        //1a. Validate id
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Claim not Found"));

        //1b. Verify Claim Status
        if(! (claim.getStatus().equals(ClaimStatus.APPROVED) &&
                (claim.getClaimType().equals(ClaimType.CASHLESS))) ){
            throw new InvalidClaimStatusException("Only Cashless Approved claims can be invoiced");
        }

        //1c. Validate the HealthcareProvider
        if(! (claim.getHealthCareProvider().getUser().getUsername().equals(providerName))){
            throw new OwnershipException("HealthCareProvider has no Access to this Claim");
        }

        //2. Mapping DTO to Invoice
        Invoice invoice = InvoiceMapper.mapDtoToInvoice(dto);

        //3. Set Claim Status
        claim.setStatus(ClaimStatus.INVOICED);

        //4. Attach Claim to Invoice
        invoice.setClaim(claim);

        //5. Save db
        invoiceRepository.save(invoice);
    }



}
