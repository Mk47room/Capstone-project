package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.InvoiceReqDto;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.InvalidClaimStatusException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.Claim;
import com.springboot.careassist.model.HealthCareProvider;
import com.springboot.careassist.model.Invoice;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    ClaimRepository claimRepository;

    @InjectMocks
    InvoiceService invoiceService;

    @Test
    public void insertInvoiceTest() {
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setClaimType(ClaimType.CASHLESS);
        claim.setStatus(ClaimStatus.APPROVED);
        claim.setHealthCareProvider(provider);
        InvoiceReqDto dto = new InvoiceReqDto(500, 2000, 1500, 4000);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        invoiceService.insertInvoice("apollo", 1L, dto);

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(claimRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(claimRepository);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    public void insertInvoiceTest_InvalidId() {
        InvoiceReqDto dto = new InvoiceReqDto(500, 2000, 1500, 4000);

        when(claimRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> invoiceService.insertInvoice("apollo", 5L, dto));

        verify(claimRepository, times(1)).findById(5L);
        verifyNoInteractions(invoiceRepository);
    }

    @Test
    public void insertInvoiceTest_InvalidStatus(){
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setClaimType(ClaimType.CASHLESS);
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setHealthCareProvider(provider);
        InvoiceReqDto dto = new InvoiceReqDto(500, 2000, 1500, 4000);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        assertThrows(InvalidClaimStatusException.class,
                ()->invoiceService.insertInvoice("apollo", 1L, dto));

        verify(claimRepository, times(1)).findById(1L);
        verifyNoInteractions(invoiceRepository);
    }

}
