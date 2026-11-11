package com.springboot.careassist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ClaimReqDto(
        @NotNull(message = "Diagnosis is required")
        @NotBlank(message = "Diagnosis is required")
        String diagnosis,

        @NotNull(message = "Treatment is required")
        @NotBlank(message = "Treatment is required")
        String treatment,

        @NotNull(message = "Claim Amount is required")
        @Positive(message = "Amount should be higher than zero")
        Double claimedAmount,

        @NotNull(message = "AdmissionDate is required")
        LocalDate admissionDate
) {
}
