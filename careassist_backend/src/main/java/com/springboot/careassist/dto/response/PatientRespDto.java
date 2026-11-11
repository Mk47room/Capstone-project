package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.Gender;
import com.springboot.careassist.enums.RiskCategory;

import java.time.Instant;
import java.time.LocalDate;

public record PatientRespDto(
        long id,
        String name,
        LocalDate dob,
        Gender gender,
        String contact,
        RiskCategory riskCategory,
        boolean isActive,
        Instant createdAt
) {
}
