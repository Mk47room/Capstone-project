package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.InsurancePlanReqDto;
import com.springboot.careassist.dto.response.InsurancePlanRespDto;
import com.springboot.careassist.dto.response.PaginatedPlanListDto;
import com.springboot.careassist.model.InsurancePlan;
import com.springboot.careassist.service.InsurancePlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InsurancePlanController {
    private  final InsurancePlanService insurancePlanService;

    //Insurer Access
    @PostMapping("/api/insuranceplan/add")
    public void addPlan(@Valid @RequestBody InsurancePlanReqDto dto){
        insurancePlanService.addPlan(dto);
    }

    @PutMapping("/api/insuranceplan/update/{planId}")
    public void updatePlan(@PathVariable Long planId,
                           @Valid @RequestBody InsurancePlanReqDto dto){
        insurancePlanService.updatePlan(planId,dto);
    }

    //Patient And Insurer Access
    @GetMapping("/api/insuranceplan/all")
    public PaginatedPlanListDto getAllPlan(@RequestParam(value = "basePremium",required = false) Double basePremium,
                                           @RequestParam(value = "coverageAmount",required = false) Double coverageAmount,
                                           @RequestParam(value = "age",required = false) Integer age,
                                           @RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
                                           @RequestParam(name = "size",required = false,defaultValue = "5") Integer size){
        return insurancePlanService.getAllPlan(basePremium,coverageAmount,age,page,size);
    }
}
