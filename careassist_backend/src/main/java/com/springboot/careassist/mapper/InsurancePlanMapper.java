package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.request.InsurancePlanReqDto;
import com.springboot.careassist.model.InsurancePlan;
import org.springframework.stereotype.Component;

@Component
public class InsurancePlanMapper {

    public static InsurancePlan convertDtoToInsurancePlan(InsurancePlanReqDto dto){
        InsurancePlan plan = new InsurancePlan();

        plan.setName(dto.name());
        plan.setBasePremium(dto.basePremium());
        plan.setCoverageAmount(dto.coverageAmount());
        plan.setAgeMin(dto.ageMin());
        plan.setAgeMax(dto.ageMax());

        return plan;
    }
}
