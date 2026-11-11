package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedPolicyListDto(
        List<PolicyRespDto> list,
        PaginationInfo paginationInfo
) {
}
