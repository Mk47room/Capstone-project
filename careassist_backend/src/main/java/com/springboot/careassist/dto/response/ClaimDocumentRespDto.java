package com.springboot.careassist.dto.response;

public record ClaimDocumentRespDto(
        Long documentId,
        String docType,
        String fileName
) {
}
