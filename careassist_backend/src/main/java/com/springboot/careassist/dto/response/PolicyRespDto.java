package com.springboot.careassist.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record PolicyRespDto(
        Long policyId,
        LocalDate startingDate,
        LocalDate endingDate,
        double premiumAmount,
        double coverageAmount,
        Long InsurancePlanId,
        String InsurancePlanName
) {
}
