package com.springboot.careassist.dto.request;

import com.springboot.careassist.enums.Gender;
import com.springboot.careassist.enums.RiskCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientReqDto(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "DOB is required")
        @Past(message = "DOB must be in past")
        LocalDate dob,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Contact is required")
        @NotBlank(message = "Contact is required")
        @Size(min = 10, max = 10, message = "Contact should be 10 digit mobile number")
        String contact,

        RiskCategory riskCategory,
        String chronicIllness,
        String preExisting,
        String acuteSymptoms,
        @NotNull(message = "Username is required")
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 15)
        String username,
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password is required")
        String password
) {
}
