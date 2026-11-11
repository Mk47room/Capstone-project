package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.HealthCareProviderReqDto;
import com.springboot.careassist.dto.request.HealthCareProviderUpdateReqDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.dto.response.HealthCareProviderRespDto;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.HealthCareProvider;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.HealthCareProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthCareProviderServiceTest {
    @Mock
    HealthCareProviderRepository healthCareProviderRepository;
    @Mock
    UserService userService;
    @Mock
    ClaimRepository claimRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    HealthCareProviderService healthCareProviderService;

    @Test
    public void insertHealthCareProviderTest() {
        HealthCareProviderReqDto dto = new HealthCareProviderReqDto("Apollo", "apollo", "apollo@123");
        User user = new User("apollo", "encoded", Role.HEALTHCAREPROVIDER);

        when(passwordEncoder.encode("apollo@123")).thenReturn("encoded");
        when(userService.getUserObj("apollo", "encoded", Role.HEALTHCAREPROVIDER)).thenReturn(user);

        healthCareProviderService.insertHealthCareProvider(dto);

        verify(healthCareProviderRepository, times(1)).save(any(HealthCareProvider.class));
        verify(passwordEncoder, times(1)).encode("apollo@123");
        verify(userService, times(1)).getUserObj("apollo", "encoded", Role.HEALTHCAREPROVIDER);
        verifyNoMoreInteractions(healthCareProviderRepository);
    }

    @Test
    public void updateHealthCareProviderTest() {
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);
        HealthCareProviderUpdateReqDto dto = new HealthCareProviderUpdateReqDto("Apollo Hospitals");

        when(healthCareProviderRepository.getByUsername("apollo")).thenReturn(provider);

        healthCareProviderService.updateHealthCareProvider("apollo", dto);

        assertEquals("Apollo Hospitals", provider.getName());
        verify(healthCareProviderRepository, times(1)).getByUsername("apollo");
        verify(healthCareProviderRepository, times(1)).save(provider);
        verifyNoMoreInteractions(healthCareProviderRepository);
    }

    @Test
    public void getAllTest() {
        User user1 = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        User user2 = new User(2L, "city", "city@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        User user3 = new User(3L, "gems", "gems@123", Role.HEALTHCAREPROVIDER, true, Instant.now());

        HealthCareProviderRespDto provider1 = new HealthCareProviderRespDto(1L, "Apollo", true,Instant.now());
        HealthCareProviderRespDto provider2 = new HealthCareProviderRespDto(2L, "City", true,Instant.now());
        HealthCareProviderRespDto provider3 = new HealthCareProviderRespDto(3L, "Gems",true, Instant.now());

        List<HealthCareProviderRespDto> list1 = List.of(provider1, provider2);
        List<HealthCareProviderRespDto> list2 = List.of(provider3);

        Page<HealthCareProviderRespDto> pageList1 = new PageImpl<>(list1);
        Page<HealthCareProviderRespDto> pageList2 = new PageImpl<>(list2);

        when(healthCareProviderRepository.findActiveAll(true, PageRequest.of(0, 2)))
                .thenReturn(pageList1);
        when(healthCareProviderRepository.findActiveAll(true, PageRequest.of(1, 2)))
                .thenReturn(pageList2);

        assertEquals(2, healthCareProviderService.getAll(0, 2).list().size());
        assertEquals(1, healthCareProviderService.getAll(1, 2).list().size());

        verify(healthCareProviderRepository, times(1))
                .findActiveAll(true, PageRequest.of(0, 2));
        verify(healthCareProviderRepository, times(1))
                .findActiveAll(true, PageRequest.of(1, 2));
        verifyNoMoreInteractions(healthCareProviderRepository);
    }
    @Test
    public void getByIdTest() {
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);

        when(healthCareProviderRepository.findById(1L)).thenReturn(Optional.of(provider));

        healthCareProviderService.getById(1L);

        verify(healthCareProviderRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(healthCareProviderRepository);
    }

    @Test
    public void getByIdTest_Invalid() {
        when(healthCareProviderRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> healthCareProviderService.getById(5L));

        verify(healthCareProviderRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(healthCareProviderRepository);
    }

    @Test
    public void deleteHealthCareProviderTest() {
        User user = new User(1L, "apollo", "apollo@123", Role.HEALTHCAREPROVIDER, true, Instant.now());
        HealthCareProvider provider = new HealthCareProvider(1L, "Apollo", Instant.now(), user);

        when(healthCareProviderRepository.findById(1L)).thenReturn(Optional.of(provider));

        healthCareProviderService.deleteHealthCareProvider(1L);

        assertFalse(user.isActive());
        assertFalse(provider.getUser().isActive());
        verify(healthCareProviderRepository, times(1)).findById(1L);
        verify(healthCareProviderRepository, times(1)).save(provider);
        verify(healthCareProviderRepository, times(0)).delete(any(HealthCareProvider.class));
        verifyNoMoreInteractions(healthCareProviderRepository);
    }

    @Test
    public void getClaimsByProviderTest() {
        ClaimRespDtoV2 dto1 = new ClaimRespDtoV2(1L, ClaimType.CASHLESS,0d, 0d, ClaimStatus.SUBMITTED, "Fever", Instant.now(), "Apollo");
        ClaimRespDtoV2 dto2 = new ClaimRespDtoV2(2L, ClaimType.REIMBURSEMENT, 0d,0d,ClaimStatus.APPROVED, "Fracture", Instant.now(), "Apollo");
        ClaimRespDtoV2 dto3 = new ClaimRespDtoV2(3L, ClaimType.CASHLESS, 0d,0d,ClaimStatus.PAID, "Cold", Instant.now(), "City");

        when(claimRepository.getClaimsByProvider("apollo", PageRequest.of(0, 5)))
                .thenReturn(List.of(dto1, dto2));
        when(claimRepository.getClaimsByProvider("city", PageRequest.of(0, 5)))
                .thenReturn(List.of(dto3));

        assertEquals(2, healthCareProviderService.getClaimsByProvider("apollo", 0, 5).size());
        assertEquals(1, healthCareProviderService.getClaimsByProvider("city", 0, 5).size());
        verify(claimRepository, times(1))
                .getClaimsByProvider("apollo", PageRequest.of(0, 5));
        verify(claimRepository, times(1))
                .getClaimsByProvider("city", PageRequest.of(0, 5));
        verifyNoMoreInteractions(claimRepository);
    }

}
