package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.ClaimRejectReqDto;
import com.springboot.careassist.dto.request.ClaimReqDto;
import com.springboot.careassist.dto.request.ClaimReqDtoV2;
import com.springboot.careassist.dto.response.*;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.exception.*;
import com.springboot.careassist.mapper.ClaimMapper;
import com.springboot.careassist.model.Claim;
import com.springboot.careassist.model.HealthCareProvider;
import com.springboot.careassist.model.Policy;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.HealthCareProviderRepository;
import com.springboot.careassist.repository.InvoiceRepository;
import com.springboot.careassist.repository.PolicyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClaimService {

    private final HealthCareProviderRepository healthCareProviderRepository;
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final InvoiceRepository invoiceRepository;

    public ClaimRespDtoV2 addClaimByProvider(String providerUsername, Long policyId, ClaimReqDto dto) {
        //1. Validate policy id.
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(()-> new ResourceNotFoundException("Policy Not found"));

        HealthCareProvider provider = healthCareProviderRepository.getByUsername(providerUsername);

        //2. Map DTO to Claim and Set claimType
        Claim claim = ClaimMapper.convertDtoToClaim(dto);
        claim.setClaimType(ClaimType.CASHLESS);

        //3. Attach policy and provider
        claim.setPolicy(policy);
        claim.setHealthCareProvider(provider);

        //4. Save in DB
        log.info("Claim approval requested by Provider. Claim ID: "+ claim.getId());
        claim = claimRepository.save(claim);
        return ClaimMapper.convertClaimToClaimRespDtoV2(claim);
    }

    public ClaimAddRespDto addClaimByPatient(String patientUserName, Long policyId, @Valid ClaimReqDtoV2 claimReqDtoV2) {
        //1. Validate policy id.
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(()-> new ResourceNotFoundException("Policy Not found"));
        if(! (policy.getPatient().getUser().getUsername().equals(patientUserName))){
            throw new OwnershipException("Patient doesn't hold the given Policy");
        }

        //2. Map DTO to Claim and Set claimType
        Claim claim = ClaimMapper.convertDtoToClaimV2(claimReqDtoV2);
        claim.setClaimType(ClaimType.REIMBURSEMENT);

        //3. Attach policy and provider
        claim.setPolicy(policy);

        //4. Save in DB
        claimRepository.save(claim);

        log.info("Claim approval requested by Patient. Claim ID: "+ claim.getId());
        return ClaimMapper.convertClaimToDto(claim);
    }

    public PaginatedClaimListDtoV1 dynamicFilterClaims(ClaimType claimType, ClaimStatus claimStatus, LocalDate fromDate, LocalDate toDate, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ClaimRespDto> list = claimRepository.dynamicFilterClaims(claimType,
                claimStatus,
                fromDate == null? null: fromDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                toDate == null? null: toDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                pageable);

        return new PaginatedClaimListDtoV1(
                list.getContent(),
                new PaginationInfo(
                        list.getTotalPages(),list.getTotalElements(),
                        list.stream().toList().size()
                )
        );
    }

    public void verifyPolicyApproveClaim(Long claimId) {
        //1a. Verify id
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()->new ResourceNotFoundException("Claim Not Found"));

        //1b. Verify Claim Status
        if(! claim.getStatus().equals(ClaimStatus.SUBMITTED)){
            throw new InvalidClaimStatusException("Only Submitted claims can verify");
        }

        //2. Verify Policy Activation
        if(! (claim.getAdmissionDate().isAfter(claim.getPolicy().getStartingDate())   &&
                claim.getAdmissionDate().isBefore(claim.getPolicy().getEndingDate())) ){
            throw  new PolicyExpireException("Policy Expired/ No Policy");
        }

        //3. Setting Approved Amount
        if (claim.getClaimedAmount() <= claim.getPolicy().getCoverageAmount()){
            claim.setApprovedAmount(claim.getClaimedAmount());
        } else {
            claim.setApprovedAmount(claim.getPolicy().getCoverageAmount());
        }
        log.info("Claim approved successfully. Claim ID: " + claimId);
        //4. Update Claim Status
        claim.setStatus(ClaimStatus.APPROVED);
    }

    public ClaimInvoiceRespDto getClaimByClaimId(Long claimId)  {
        //1a. Validate claim
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("claim Not found"));

        //1b. Validate ClaimType as Cashless
        if(! claim.getClaimType().equals(ClaimType.CASHLESS) ){
            throw new InvalidClaimTypeException("Claim Type Can only be Cashless");
        }

        return invoiceRepository.getClaimByClaimId(claimId);
    }



    public void payClaim(Long claimId) {
        //1a. Verify id
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()->new ResourceNotFoundException("Claim Not Found"));

        //1b. Verify Claim Status
        if (!( (claim.getClaimType().equals(ClaimType.CASHLESS) && claim.getStatus().equals(ClaimStatus.INVOICED)) ||
                (claim.getClaimType().equals(ClaimType.REIMBURSEMENT) && claim.getStatus().equals(ClaimStatus.APPROVED)) )) {
            throw new InvalidClaimStatusException("Only approved claims can be paid");
        }

        //2. resetting Coverage amount
        claim.getPolicy().setCoverageAmount(claim.getPolicy().getCoverageAmount()
                                                        - claim.getApprovedAmount());

        //3. Setting Status
        log.info("Claim Paid successfully. Claim ID: " + claimId+" Amount: " + claim.getApprovedAmount());
        claim.setStatus(ClaimStatus.PAID);
    }

    public void rejectClaim(Long claimId, ClaimRejectReqDto dto) {
        //1a. Verify id
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()->new ResourceNotFoundException("Claim Not Found"));

        //1b. Verify Claim Status
        if( claim.getStatus().equals(ClaimStatus.PAID) ){
            throw new InvalidClaimStatusException(" Paid claims cannot be Rejected");
        }

        //2. Setting Status and reason
        log.info("Claim Rejected for " + dto.rejectedReason() + ". Claim ID: " + claimId);
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setRejectedReason(dto.rejectedReason());
    }

}
