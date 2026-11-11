package com.springboot.careassist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.aspectj.lang.annotation.After;

public record InsurancePlanReqDto(

        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Base Premium is required")
        @PositiveOrZero(message = "Base Premium cannot be negative")
        Double basePremium,

        @NotNull(message = "Coverage Amount is required")
        @PositiveOrZero(message = "Coverage Amount cannot be negative")
        Double coverageAmount,


        @NotNull(message = "Min Age is required")
        @PositiveOrZero(message = "Age cannot be negative")
        Integer ageMin,

        @NotNull(message = "Max Age is required")
        @PositiveOrZero(message = "Age cannot be negative")
        Integer ageMax
) {
}
