package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.PatientReqDto;
import com.springboot.careassist.dto.request.PatientUpdateDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.dto.response.PatientRespDto;
import com.springboot.careassist.dto.response.PatientRespDtoV2;
import com.springboot.careassist.enums.*;
import com.springboot.careassist.exception.InvalidCallException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.Patient;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.PatientRepository;
import org.hibernate.annotations.CreationTimestamp;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    PatientRepository patientRepository;
    @Mock
    ClaimRepository claimRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserService userService;

    @InjectMocks
    PatientService patientService;

    @Test
    public void addPatientTest(){

        PatientReqDto dto = new PatientReqDto("John",LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.CHRONIC_ILLNESS, "Diabetes", null, null, "john", "john@123");
        User user = new User("john", "encoded", Role.PATIENT);

        when(userService.getUserObj("john", "encoded", Role.PATIENT)).thenReturn(user);
        when(passwordEncoder.encode("john@123")).thenReturn("encoded");
        when(patientRepository.save(any(Patient.class))).thenReturn(any(Patient.class));

        patientService.addPatient(dto);
        verify(patientRepository,times(1)).save(any(Patient.class));
    }

    @Test
    public void getAllPatientTest() {
        User user1 = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        User user2 = new User(2L, "harry", "harry@123", Role.PATIENT, true, Instant.now());
        User user3 = new User(3L, "potter", "potter@123", Role.PATIENT, true, Instant.now());

        PatientRespDto patient1 = new PatientRespDto(1L, "john", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, true,  Instant.now());
        PatientRespDto patient2 = new PatientRespDto(2L, "harry", LocalDate.of(1998, 3, 10), Gender.MALE, "9876543211", RiskCategory.CHRONIC_ILLNESS, true, Instant.now());
        PatientRespDto patient3 = new PatientRespDto(3L, "potter", LocalDate.of(1990, 1, 1), Gender.OTHERS, "9876543212", RiskCategory.PRE_EXISTING, true, Instant.now());

        List<PatientRespDto> list1 = List.of(patient1, patient2);
        List<PatientRespDto> list2 = List.of(patient3);
        Page<PatientRespDto> pageList1 = new PageImpl<>(list1);
        Page<PatientRespDto> pageList2 = new PageImpl<>(list2);

        when(patientRepository.findAllActive(true, PageRequest.of(0, 2)))
                .thenReturn(pageList1);
        when(patientRepository.findAllActive(true, PageRequest.of(1, 2)))
                .thenReturn(pageList2);

        assertEquals(2, (patientService.getAllPatient(0, 2)).list().size());
        assertEquals(1, patientService.getAllPatient(1, 2).list().size());

        verify(patientRepository, times(1))
                .findAllActive(true, PageRequest.of(0, 2));
        verify(patientRepository, times(1))
                .findAllActive(true, PageRequest.of(1, 2));
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void updatePatientTest() {
        User user1 = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(1L, "john", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user1);

        PatientUpdateDto dto = new PatientUpdateDto("john","1111111111", RiskCategory.CHRONIC_ILLNESS, "Diabetes",null,null);

        when(patientRepository.getByUsername("john")).thenReturn(patient);

        patientService.updatePatient("john", dto);

        assertEquals("1111111111", patient.getContact());
        assertEquals(RiskCategory.CHRONIC_ILLNESS, patient.getRiskCategory());
        assertEquals("Diabetes", patient.getChronicIllness());

        verify(patientRepository, times(1)).getByUsername("john");
        verify(patientRepository, times(1)).save(patient);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void updatePatientTest_Invalid() {
        User user = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(1L, "test1", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user);

        PatientUpdateDto dto = new PatientUpdateDto(null,null, null, null, null, null);

        when(patientRepository.getByUsername("john")).thenReturn(patient);

        assertThrows(InvalidCallException.class,
                () -> patientService.updatePatient("john", dto));

        verify(patientRepository, times(1)).getByUsername("john");
        verify(patientRepository, times(0)).save(any(Patient.class));
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void getPatientTest() {
        User user1 = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(1L, "john", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user1);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientRespDtoV2 test = patientService.getPatient(1L);
        assertEquals(patient.getContact(),test.contact());
        assertEquals(patient.getRiskCategory(),test.riskCategory());
        assertEquals(patient.getName(),test.name());

        verify(patientRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void getPatientTest_Invalid() {
        when(patientRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatient(5L));

        verify(patientRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void deletePatientTest() {
        User user = new User(1L, "john", "john@123", Role.PATIENT, true, Instant.now());
        Patient patient = new Patient(1L, "john", LocalDate.of(1995, 5, 20), Gender.MALE, "9876543210", RiskCategory.NONE, null, null, null, Instant.now(), user);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deletePatient(1L);

        assertFalse(user.isActive());
        assertFalse(patient.getUser().isActive());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, never()).delete(any(Patient.class));
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    public void getClaimsTest() {
        ClaimRespDtoV2 dto1 = new ClaimRespDtoV2(1L, ClaimType.CASHLESS, 0d,0d,ClaimStatus.SUBMITTED, "Fever", Instant.now(),"city");
        ClaimRespDtoV2 dto2 = new ClaimRespDtoV2(2L, ClaimType.REIMBURSEMENT, 0d, 0d,ClaimStatus.APPROVED, "Fracture", Instant.now(), "city");
        ClaimRespDtoV2 dto3 = new ClaimRespDtoV2(3L, ClaimType.CASHLESS,0d,0d, ClaimStatus.PAID, "Cold", Instant.now(), "gems");

        List<ClaimRespDtoV2> list1 = List.of(dto1, dto2);
        List<ClaimRespDtoV2> list2 = List.of(dto3);
        Page<ClaimRespDtoV2> pageList1 = new PageImpl<>(list1);
        Page<ClaimRespDtoV2> pageList2 = new PageImpl<>(list2);

        when(claimRepository.getClaims("john",null,null,null,null,PageRequest.of(0, 5)))
                .thenReturn(pageList1);
        when(claimRepository.getClaims("potter",null,null,null,null,PageRequest.of(0, 5)))
                .thenReturn(pageList2);

        assertEquals(2, patientService.getClaims("john", null,null,null,null,0, 5).list().size());
        assertEquals(1, patientService.getClaims("potter", null,null,null,null,0, 5).list().size());

        verify(claimRepository, times(1))
                .getClaims("john",null,null,null,null, PageRequest.of(0, 5));
        verify(claimRepository, times(1))
                .getClaims("potter",null,null,null,null, PageRequest.of(0, 5));

        verifyNoMoreInteractions(claimRepository);
    }
}
