package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.ClaimRejectReqDto;
import com.springboot.careassist.dto.request.ClaimReqDto;
import com.springboot.careassist.dto.request.ClaimReqDtoV2;
import com.springboot.careassist.dto.response.ClaimAddRespDto;
import com.springboot.careassist.dto.response.ClaimRespDto;
import com.springboot.careassist.enums.*;
import com.springboot.careassist.exception.InvalidClaimStatusException;
import com.springboot.careassist.exception.InvalidClaimTypeException;
import com.springboot.careassist.exception.OwnershipException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.*;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.HealthCareProviderRepository;
import com.springboot.careassist.repository.InvoiceRepository;
import com.springboot.careassist.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClaimServiceTest {

    @Mock
    HealthCareProviderRepository healthCareProviderRepository;
    @Mock
    ClaimRepository claimRepository;
    @Mock
    PolicyRepository policyRepository;
    @Mock
    InvoiceRepository invoiceRepository;

    @InjectMocks
    ClaimService claimService;

    @Test
    public void addClaimByProviderTest() {
        Policy policy = new Policy(1L, LocalDate.now(), LocalDate.now().plusYears(1), 10000, 500000, null, null);
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setHealthCareProvider(provider);

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(healthCareProviderRepository.getByUsername("apollo")).thenReturn(provider);
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        ClaimReqDto reqDto = new ClaimReqDto("Fever", "Antibiotics", 15000.0, LocalDate.of(2026, 10, 1));

        claimService.addClaimByProvider("apollo", 1L, reqDto);

        verify(claimRepository, times(1)).save(any(Claim.class));
        verify(policyRepository, times(1)).findById(1L);
        verify(healthCareProviderRepository, times(1)).getByUsername("apollo");

        verifyNoMoreInteractions(policyRepository);
        verifyNoMoreInteractions(healthCareProviderRepository);
        verifyNoMoreInteractions(claimRepository);
    }


    @Test
    public void addClaimByProviderTest_Invalid() {
        ClaimReqDto dto = new ClaimReqDto("Fever", "Antibiotics", 15000.0, LocalDate.of(2026, 10, 1));

        when(policyRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class,
                () -> claimService.addClaimByProvider("apollo", 5L, dto));

        verify(policyRepository, times(1)).findById(5L);
        verifyNoInteractions(healthCareProviderRepository);
        verifyNoInteractions(claimRepository);
    }


    @Test
    public void addClaimByPatientTest() {
        User user = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(1L, "john", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user);
        Policy policy = new Policy(1L, LocalDate.now(), LocalDate.now().plusYears(1), 10000, 500000, patient, null);
        ClaimReqDtoV2 dto = new ClaimReqDtoV2("Apollo", "Fever", "Antibiotics", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 15000.0);

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(claimRepository.save(any(Claim.class))).thenReturn(any(Claim.class));

        claimService.addClaimByPatient("john", 1L, dto);

        verify(claimRepository, times(1)).save(any(Claim.class));
        verify(policyRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(policyRepository);
        verifyNoMoreInteractions(claimRepository);
    }

    @Test
    public void addClaimByPatientTest_InvalidPolicy() {
        ClaimReqDtoV2 dto = new ClaimReqDtoV2("Apollo", "Fever", "Antibiotics", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 15000.0);
        when(policyRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> claimService.addClaimByPatient("john", 5L, dto));

        verify(policyRepository, times(1)).findById(5L);
        verifyNoInteractions(claimRepository);
    }

    @Test
    public void addClaimByPatientTest_InvalidOwnership() {
        User user1 = new User(2L, "harry", "harry@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(2L, "harry", LocalDate.of(1998, 3, 10), Gender.MALE, "9876543211", RiskCategory.NONE, null, null, null, Instant.now(), user1);
        Policy policy = new Policy(1L, LocalDate.now(), LocalDate.now().plusYears(1), 10000, 500000, patient, null);
        ClaimReqDtoV2 dto = new ClaimReqDtoV2("Apollo", "Fever", "Antibiotics", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 15000.0);

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        assertThrows(OwnershipException.class,
                () -> claimService.addClaimByPatient("john", 1L, dto));

        verify(policyRepository, times(1)).findById(1L);
        verify(claimRepository, times(0)).save(any(Claim.class));
    }

    @Test
    public void dynamicFilterClaimsTest() {
        ClaimRespDto dto1 = new ClaimRespDto(1L, ClaimType.CASHLESS, ClaimStatus.APPROVED, "Fever","Injection", Instant.parse("2026-10-10T10:00:00Z"), 15000.0, 12000.0, null, 1L, "Apollo", "john", RiskCategory.NONE);
        ClaimRespDto dto2 = new ClaimRespDto(2L, ClaimType.CASHLESS, ClaimStatus.APPROVED, "Fracture","Hospitalised ", Instant.parse("2026-10-20T10:00:00Z"), 30000.0, 25000.0, null, 2L, "Fortis", "harry", RiskCategory.PRE_EXISTING);

        Pageable  pageable = PageRequest.of(0, 5);
        List<ClaimRespDto> list = List.of(dto1, dto2);
        Page<ClaimRespDto> pageList = new PageImpl<>(list, pageable, 5);

        when(claimRepository.dynamicFilterClaims(
                ClaimType.CASHLESS,
                ClaimStatus.APPROVED,
                Instant.parse("2026-10-01T00:00:00Z"),
                Instant.parse("2026-10-31T00:00:00Z"),
                pageable))
                .thenReturn(pageList);

        claimService.dynamicFilterClaims(ClaimType.CASHLESS, ClaimStatus.APPROVED,
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31),
                0, 5);

        verify(claimRepository, times(1)).dynamicFilterClaims(
                ClaimType.CASHLESS,
                ClaimStatus.APPROVED,
                Instant.parse("2026-10-01T00:00:00Z"),
                Instant.parse("2026-10-31T00:00:00Z"),
                PageRequest.of(0, 5));

        verifyNoMoreInteractions(claimRepository);
    }

    @Test
    public void verifyPolicyApproveClaimTest() {
        Policy policy = new Policy(1L, LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), 10000, 500000, null, null);
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setAdmissionDate(LocalDate.of(2026, 6, 15));
        claim.setClaimedAmount(15000.0);
        claim.setPolicy(policy);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.verifyPolicyApproveClaim(1L);

        assertEquals(ClaimStatus.APPROVED, claim.getStatus());
        verify(claimRepository, times(1)).findById(1L);
    }


    @Test
    public void verifyPolicyApproveClaimTest_InvalidId() {
        when(claimRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> claimService.verifyPolicyApproveClaim(5L));

        verify(claimRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(claimRepository);
    }

    @Test
    public void verifyPolicyApproveClaimTest_InvalidStatus() {
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setStatus(ClaimStatus.PAID);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        assertThrows(InvalidClaimStatusException.class, () -> claimService.verifyPolicyApproveClaim(1L));

        verify(claimRepository, times(1)).findById(1L);
    }

    @Test
    public void getClaimByClaimIdTest() {
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setClaimType(ClaimType.CASHLESS);
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.getClaimByClaimId(1L);

        verify(claimRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(claimRepository);
    }

    @Test
    public void getClaimByClaimIdTest_InvalidId() {

        when(claimRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> claimService.getClaimByClaimId(1L));

        verify(claimRepository, times(1)).findById(1L);
    }

    @Test
    public void getClaimByClaimIdTest_InvalidType(){
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setClaimType(ClaimType.REIMBURSEMENT);
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        assertThrows(InvalidClaimTypeException.class,
                ()->claimService.getClaimByClaimId(1L));

        verify(claimRepository, times(1)).findById(1L);
    }

    @Test
    public void payClaimTest() {
        Policy policy = new Policy(1L, LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), 10000, 500000, null, null);
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setClaimType(ClaimType.CASHLESS);
        claim.setStatus(ClaimStatus.INVOICED);
        claim.setApprovedAmount(12000.0);
        claim.setPolicy(policy);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.payClaim(1L);

        assertEquals(ClaimStatus.PAID, claim.getStatus());
        assertEquals(488000.0, policy.getCoverageAmount());

        verify(claimRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(claimRepository);
    }

    @Test
    public void rejectClaimTest() {
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setStatus(ClaimStatus.SUBMITTED);

        ClaimRejectReqDto dto = new ClaimRejectReqDto("Treatment not covered");

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.rejectClaim(1L, dto);

        assertEquals(ClaimStatus.REJECTED, claim.getStatus());
        assertEquals("Treatment not covered", claim.getRejectedReason());

        verify(claimRepository, times(1)).findById(1L);
    }

}
