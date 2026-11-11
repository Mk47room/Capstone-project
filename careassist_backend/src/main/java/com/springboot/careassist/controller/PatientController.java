package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.PatientReqDto;
import com.springboot.careassist.dto.request.PatientUpdateDto;
import com.springboot.careassist.dto.response.*;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.RiskCategory;
import com.springboot.careassist.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/api/patient/add")
    public void addPatient(@Valid @RequestBody PatientReqDto dto){
        patientService.addPatient(dto);
    }

    @GetMapping("/api/patient/view")
    public PatientRespDtoV2 viewPatient(Principal principal){
        return patientService.viewPatient(principal.getName());
    }

    @PutMapping("/api/patient/update")
    public void updatePatient(Principal principal,
                              @RequestBody PatientUpdateDto updateDto){
        patientService.updatePatient(principal.getName(),updateDto);
    }

    @GetMapping("/api/patient/claim/all")
    public PaginatedClaimListDto getClaims(Principal principal,
                                           @RequestParam(value = "claimType",required = false) ClaimType claimType,
                                           @RequestParam(value = "status",required = false) ClaimStatus claimStatus,
                                           @RequestParam(value = "fromDate",required = false) LocalDate fromDate,
                                           @RequestParam(value = "toDate",required = false) LocalDate toDate,
                                           @RequestParam(name = "page",required = false,defaultValue = "0")Integer page,
                                           @RequestParam(name = "size",required = false,defaultValue = "5") Integer size){
        return patientService.getClaims(principal.getName(),claimType,claimStatus,fromDate,toDate,page,size);
    }

    //Admin controls
    //1. View all patients
    @GetMapping("/api/patient/all")
    public PaginatedPatientListDto getAllPatient(@RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
                                              @RequestParam(name = "size",required = false,defaultValue = "5") Integer size){
        return patientService.getAllPatient(page,size);
    }

    //2. Soft delete patient details.
    @PutMapping("/api/patient/deactive/{patientId}")
    public void deletePatient(@PathVariable Long patientId){
        patientService.deletePatient(patientId);
    }

    //3. View by id
    @GetMapping("/api/patient/{patientId}")
    public PatientRespDtoV2 getPatient(@PathVariable Long patientId){
        return patientService.getPatient(patientId);
    }
}
