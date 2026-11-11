package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;

import java.time.Instant;

public record ClaimRespDtoV2(
        Long claimId,
        ClaimType claimType,
        Double claimedAmount,
        Double approvedAmount,
        ClaimStatus status,
        String diagnosis,
        Instant submittedDate,
        String hospitalName
) {
}
