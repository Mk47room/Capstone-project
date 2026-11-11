package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.ClaimStatus;

public record ClaimAddRespDto(
        Long claimId,
        ClaimStatus status
) {
}
