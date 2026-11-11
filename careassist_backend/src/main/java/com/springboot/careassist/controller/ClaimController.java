package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.ClaimRejectReqDto;
import com.springboot.careassist.dto.request.ClaimReqDto;
import com.springboot.careassist.dto.request.ClaimReqDtoV2;
import com.springboot.careassist.dto.response.*;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/api/claim/add/cashless/{policyId}")
    public ClaimRespDtoV2 addClaimByProvider(Principal principal,
                                             @PathVariable Long policyId,
                                             @Valid @RequestBody ClaimReqDto dto){
        return claimService.addClaimByProvider(principal.getName(),policyId,dto);
    }

    @PostMapping("/api/claim/add/{policyId}")
    public ClaimAddRespDto addClaimByPatient(Principal principal, @PathVariable Long policyId,
                                  @Valid @RequestBody ClaimReqDtoV2 claimReqDtoV2){
        return claimService.addClaimByPatient(principal.getName(),policyId, claimReqDtoV2);
    }


    @GetMapping("/api/claim/filter")
    public PaginatedClaimListDtoV1 dynamicFilterClaims(@RequestParam(value = "claimType",required = false)ClaimType claimType,
                                                       @RequestParam(value = "status",required = false) ClaimStatus claimStatus,
                                                       @RequestParam(value = "fromDate",required = false) LocalDate fromDate,
                                                       @RequestParam(value = "toDate",required = false) LocalDate toDate,
                                                       @RequestParam(name = "page",required = false,defaultValue = "0")Integer page,
                                                       @RequestParam(name = "size",required = false,defaultValue = "5")Integer size){
        return claimService.dynamicFilterClaims(claimType,claimStatus,fromDate,toDate,page,size);
    }

    @GetMapping("/api/claim/invoice/{claimId}")
    public ClaimInvoiceRespDto getClaimByClaimId(@PathVariable Long claimId){
        return claimService.getClaimByClaimId(claimId);
    }

    @PutMapping("/api/claim/approve/{claimId}")
    public void verifyPolicyApproveClaim(@PathVariable Long claimId){
        claimService.verifyPolicyApproveClaim(claimId);
    }

    @PutMapping("/api/claim/pay/{claimId}")
    public void payClaim(@PathVariable Long claimId){
        claimService.payClaim(claimId);
    }

    @PutMapping("/api/claim/reject/{claimId}")
    public void rejectClaim(@PathVariable Long claimId,
                            @Valid @RequestBody ClaimRejectReqDto dto){
        claimService.rejectClaim(claimId,dto);
    }



}
