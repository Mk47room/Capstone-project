package com.springboot.careassist.dto.response;

public record PaginationInfo(
        int totalPages,
        Long totalRecords,
        Integer numRecordsInPage
) {
}
