package com.springboot.careassist.dto.response;

public record InsurancePlanRespDto(
        Long planId,
        String name,
        double basePremium,
        double coverageAmount,
        int ageMin,
        int ageMax
) {
}
