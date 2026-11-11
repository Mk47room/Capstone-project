package com.springboot.careassist.controller;

import com.springboot.careassist.dto.response.PaginatedPolicyListDto;
import com.springboot.careassist.dto.response.PolicyRespDto;
import com.springboot.careassist.model.Policy;
import com.springboot.careassist.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PolicyController {
    private final PolicyService policyService;

    @PostMapping("/api/policy/add/{planId}")
    public void buyPolicy(Principal principal,
                          @PathVariable Long planId,
                          @RequestParam("policyYears") Integer policyYears){
        policyService.buyPolicy(principal.getName(),planId,policyYears);
    }

    @GetMapping("/api/policy/all")
    public PaginatedPolicyListDto getAllPolicyByPatient(Principal principal,
                                                        @RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size",required = false,defaultValue = "5") Integer size){
        return policyService.getAllPolicyByPatient(principal.getName(),page,size);
    }
}
