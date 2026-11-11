package com.springboot.careassist.dto.response;

import com.springboot.careassist.enums.Role;

public record DetailsDto(
        String username,
        Boolean isActive,
        Role role
) {
}
