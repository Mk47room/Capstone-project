package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedClaimListDtoV1(
        List<ClaimRespDto> list,
        PaginationInfo paginationInfo
) {
}
