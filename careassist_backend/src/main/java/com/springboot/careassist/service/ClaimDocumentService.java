package com.springboot.careassist.service;

import com.springboot.careassist.dto.response.ClaimDocumentRespDto;
import com.springboot.careassist.dto.response.DetailsDto;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.InvalidCallException;
import com.springboot.careassist.exception.OwnershipException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.mapper.ClaimDocumentMapper;
import com.springboot.careassist.model.Claim;
import com.springboot.careassist.model.ClaimDocument;
import com.springboot.careassist.repository.ClaimDocumentRepository;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaimDocumentService {

    private final ClaimRepository claimRepository;
    private final ClaimDocumentRepository documentRepository;
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(ClaimDocument.class);

    private static final String uploadDir ="D:/documents/hexaware/FSD/capstone project/Project/frontend/careassist-ui/public/uploads/claimDocuments/";

    public void uploadDocument(String username, Long claimId,MultipartFile file, String docType) throws IOException {

        // Fetch claim
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        // Validate ownership
        validateClaimAccess(username, claim);

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new InvalidCallException("Please upload a valid file");
        }

        if (docType == null || docType.trim().isEmpty()) {
            throw new InvalidCallException("Document type is required");
        }

        String ext = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        logger.info("File extension {}",  ext);
        String originalFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename() ;

        // Save file
        Files.copy(file.getInputStream() , Paths.get(uploadDir + originalFileName), StandardCopyOption.REPLACE_EXISTING);

        // Save document details in DB
        ClaimDocument document = new ClaimDocument();
        document.setFilePath(originalFileName);
        document.setDocType(docType.trim());
        document.setClaim(claim);
        documentRepository.save(document);
    }

    public List<ClaimDocumentRespDto> getDocuments(String username,Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        validateClaimAccess(username, claim);

        return documentRepository.findByClaim_Id(claimId)
                .stream()
                .map(ClaimDocumentMapper :: claimDocumentToClaimDocumentRespDto)
                .toList();
    }

    public ResponseEntity<Resource> downloadDocument(String username,Long documentId) throws IOException {

        ClaimDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        validateClaimAccess(username, document.getClaim());

        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = directory.resolve(document.getFilePath()).normalize();
        if (!filePath.startsWith(directory) || !Files.exists(filePath)) {
            throw new ResourceNotFoundException("Document file not found");
        }

        Resource resource = new FileSystemResource(filePath);
        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(document.getFilePath())
                                .build().toString())
                .body(resource);
    }

    private void validateClaimAccess(String username, Claim claim) {
        DetailsDto user = userRepository.getUserDetails(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // Insurer can access all claim documents
        if (user.role() == Role.INSURER) {
            return;
        }

        // CASHLESS claim - Healthcare Provider
        if (claim.getClaimType() == ClaimType.CASHLESS) {
            if (!claim.getHealthCareProvider().getUser().getUsername().equals(username)) {
                throw new OwnershipException("You cannot access documents for this claim");
            }
        }

        // REIMBURSEMENT claim - Patient
        else if (claim.getClaimType() == ClaimType.REIMBURSEMENT) {
            if (!claim.getPolicy().getPatient().getUser().getUsername().equals(username)) {
                throw new OwnershipException("You cannot access documents for this claim");
            }
        }
    }
}