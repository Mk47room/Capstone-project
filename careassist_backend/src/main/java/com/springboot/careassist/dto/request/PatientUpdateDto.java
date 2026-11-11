package com.springboot.careassist.dto.request;

import com.springboot.careassist.enums.RiskCategory;

public record PatientUpdateDto(
        String name,
        String contact,
        RiskCategory riskCategory,
        String chronicIllness,
        String preExisting,
        String acuteSymptoms
) {
}
