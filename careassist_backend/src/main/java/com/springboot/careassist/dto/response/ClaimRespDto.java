package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.RiskCategory;

import java.time.Instant;
import java.time.LocalDate;

public record ClaimRespDto(
        Long claimId,
        ClaimType claimType,
        ClaimStatus status,
        String diagnosis,
        String treatment,
        Instant submittedDate,
        double claimedAmount,
        double approvedAmount,
        String rejectedReason,
        Long policyId,
        String hospitalName,
        String patientName,
        RiskCategory riskCategory
) {
}
