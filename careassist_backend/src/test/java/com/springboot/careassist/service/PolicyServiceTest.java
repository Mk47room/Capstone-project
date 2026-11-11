package com.springboot.careassist.service;

import com.springboot.careassist.dto.response.PolicyRespDto;
import com.springboot.careassist.enums.Gender;
import com.springboot.careassist.enums.RiskCategory;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.AgeInvalidException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.InsurancePlan;
import com.springboot.careassist.model.Patient;
import com.springboot.careassist.model.Policy;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.InsurancePlanRepository;
import com.springboot.careassist.repository.PatientRepository;
import com.springboot.careassist.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {

    @Mock
    PatientRepository patientRepository;
    @Mock
    InsurancePlanRepository insurancePlanRepository;
    @Mock
    PolicyRepository policyRepository;

    @InjectMocks
    PolicyService policyService;


    @Test
    public void buyPolicyTest() {
        InsurancePlan plan = new InsurancePlan(1L, "Gold", 10000, 500000, 18, 60);
        User user = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient =  new Patient(1L, "john", LocalDate.now().minusYears(25), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user);

        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(patientRepository.getByUsername("john")).thenReturn(patient);

        policyService.buyPolicy("john", 1L, 5);

        verify(policyRepository, times(1)).save(any(Policy.class));
        verify(insurancePlanRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).getByUsername("john");

        verifyNoMoreInteractions(insurancePlanRepository);
        verifyNoMoreInteractions(patientRepository);
        verifyNoMoreInteractions(policyRepository);
    }

    @Test
    public void buyPolicyTest_InvalidPlan() {
        when(insurancePlanRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> policyService.buyPolicy("john", 5L, 5));

        verify(insurancePlanRepository, times(1)).findById(5L);
        verifyNoInteractions(patientRepository);
        verifyNoInteractions(policyRepository);
    }

    @Test
    public void buyPolicyTest_InvalidAge() {
        User user = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient =  new Patient(1L, "john", LocalDate.now().minusYears(61), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user);
        InsurancePlan plan = new InsurancePlan(1L, "Gold", 10000, 500000, 18, 60);

        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(patientRepository.getByUsername("john")).thenReturn(patient);

        assertThrows(AgeInvalidException.class,
                () -> policyService.buyPolicy("john", 1L, 5));

        verify(policyRepository, times(0)).save(any(Policy.class));
    }

    @Test
    public void getAllPolicyByPatientTest() {
        String username = "john";
        PolicyRespDto dto = new PolicyRespDto(1L,LocalDate.of(1999,10,3),LocalDate.of(2024,10,19),2300,300000,1L,"Primium");
        PolicyRespDto dto2 = new PolicyRespDto(2L,LocalDate.of(1998,10,13),LocalDate.of(2026,10,29),3300,350000,2L,"Primium");

        Page<PolicyRespDto> policyPage = new PageImpl<>(Arrays.asList(dto, dto2), PageRequest.of(0,5), 2);

        when(policyRepository.getAllPolicyByPatient(username, PageRequest.of(0,5))).thenReturn(policyPage);

        assertEquals(2,policyRepository.getAllPolicyByPatient(username, PageRequest.of(0,5)).getTotalElements());
        verify(policyRepository, times(1)).getAllPolicyByPatient(username, PageRequest.of(0,5));
        verifyNoMoreInteractions(policyRepository);
    }

}
