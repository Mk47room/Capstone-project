package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedProviderListDto (
        List<HealthCareProviderRespDto> list,
        PaginationInfo paginationInfo
){
}
