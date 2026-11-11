package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedClaimListDto(
        List<ClaimRespDtoV2> list,
        PaginationInfo paginationInfo
) {
}
