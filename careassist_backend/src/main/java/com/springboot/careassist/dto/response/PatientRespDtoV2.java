package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.Gender;
import com.springboot.careassist.enums.RiskCategory;

import java.time.Instant;
import java.time.LocalDate;

public record PatientRespDtoV2(
        String name,
        LocalDate dob,
        String contact,
        RiskCategory riskCategory,
        String chronicIllness,
        String preExisting,
        String acuteSymptoms
) {
}
