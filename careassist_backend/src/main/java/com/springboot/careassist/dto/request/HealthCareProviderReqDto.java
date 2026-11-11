package com.springboot.careassist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HealthCareProviderReqDto(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be null")
        String name,

        @NotNull(message = "Username is required")
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20)
        String username,
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password is required")
        String password
) {
}
