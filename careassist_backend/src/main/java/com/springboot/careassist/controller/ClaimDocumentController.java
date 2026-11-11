package com.springboot.careassist.controller;

import com.springboot.careassist.dto.response.ClaimDocumentRespDto;
import com.springboot.careassist.service.ClaimDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClaimDocumentController {

    private final ClaimDocumentService documentService;

    @PostMapping("/api/claim/documents/{claimId}")
    public void uploadDocument(Principal principal,
                               @PathVariable Long claimId,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("docType") String docType) throws IOException {
        documentService.uploadDocument(principal.getName(), claimId, file, docType);
    }

    @GetMapping("/api/claim/documents/{claimId}")
    public List<ClaimDocumentRespDto> getDocuments(Principal principal,
                                                   @PathVariable Long claimId) {
        return documentService.getDocuments(principal.getName(), claimId);
    }

    // 3. Download a document
    @GetMapping("/api/claim/documents/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(Principal principal,
                                                     @PathVariable Long documentId) throws IOException {

        return documentService.downloadDocument(principal.getName(), documentId);
    }
}