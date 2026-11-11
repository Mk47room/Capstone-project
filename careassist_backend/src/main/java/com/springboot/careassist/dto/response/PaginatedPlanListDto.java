package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedPlanListDto(
        List<InsurancePlanRespDto> list,
        PaginationInfo paginationInfo
) {
}
