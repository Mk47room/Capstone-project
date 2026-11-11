package com.springboot.careassist.dto.response;

import java.time.Instant;

public record HealthCareProviderRespDto(
        long id,
        String name,
        Boolean active,
        Instant createdAt
) {
}
