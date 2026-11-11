package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.InsurancePlanReqDto;
import com.springboot.careassist.dto.response.InsurancePlanRespDto;
import com.springboot.careassist.dto.response.PaginatedPlanListDto;
import com.springboot.careassist.dto.response.PaginationInfo;
import com.springboot.careassist.exception.InvalidCallException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.mapper.InsurancePlanMapper;
import com.springboot.careassist.model.InsurancePlan;
import com.springboot.careassist.repository.InsurancePlanRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsurancePlanService {
    private final InsurancePlanRepository insurancePlanRepository;

    public void addPlan(InsurancePlanReqDto dto) {
        //1. Map Dto to plan
        InsurancePlan plan = InsurancePlanMapper.convertDtoToInsurancePlan(dto);
        insurancePlanRepository.save(plan);
    }

    public PaginatedPlanListDto getAllPlan(Double basePremium,Double coverageAmount,Integer age,Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<InsurancePlanRespDto> list = insurancePlanRepository.findAllPlans(basePremium,coverageAmount,age,pageable);

        return new PaginatedPlanListDto(
            list.getContent(),
            new PaginationInfo(
                    list.getTotalPages(),list.getTotalElements(),
                    list.stream().toList().size()
            )
        );

    }

    public void updatePlan(Long planId, @Valid InsurancePlanReqDto dto) {
        InsurancePlan plan = insurancePlanRepository.findById(planId)
                .orElseThrow(()-> new ResourceNotFoundException("Plan not found"));

        boolean flag = false;
        if(!(dto.name()  == null) && !(dto.name().trim().isEmpty())) {
            plan.setName(dto.name());
            flag = true;
        }
        if(!(dto.basePremium()  == null)) {
            plan.setBasePremium(dto.basePremium());
            flag = true;
        }
        if(!(dto.coverageAmount()  == null)) {
            plan.setCoverageAmount(dto.coverageAmount());
            flag = true;
        }
        if(!(dto.ageMin()  == null)) {
            plan.setAgeMin(dto.ageMin());
            flag = true;
        }
        if(!(dto.ageMax()  == null)) {
            plan.setAgeMax(dto.ageMax());
            flag = true;
        }

        if(!flag)
            throw new InvalidCallException("Update op terminated");
        insurancePlanRepository.save(plan);
    }
}
