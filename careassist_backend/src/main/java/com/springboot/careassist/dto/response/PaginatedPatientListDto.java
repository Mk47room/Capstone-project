package com.springboot.careassist.dto.response;

import java.util.List;

public record PaginatedPatientListDto (
        List<PatientRespDto> list,
        PaginationInfo paginationInfo
){
}
