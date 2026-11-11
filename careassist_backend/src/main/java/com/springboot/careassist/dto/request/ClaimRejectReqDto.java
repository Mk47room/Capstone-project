package com.springboot.careassist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClaimRejectReqDto(

        @NotNull(message = "Rejected Reason is required")
        @NotBlank(message = "Rejected Reason is required")
        String rejectedReason
) {
}
