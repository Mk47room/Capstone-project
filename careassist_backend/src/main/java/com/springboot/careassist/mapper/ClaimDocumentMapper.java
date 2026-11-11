package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.response.ClaimDocumentRespDto;
import com.springboot.careassist.model.ClaimDocument;
import org.springframework.stereotype.Component;

@Component
public class ClaimDocumentMapper {

    public static ClaimDocumentRespDto claimDocumentToClaimDocumentRespDto(ClaimDocument document) {
        return  new ClaimDocumentRespDto(
                document.getId(),
                document.getDocType(),
                document.getFilePath()
        );
    }
}
