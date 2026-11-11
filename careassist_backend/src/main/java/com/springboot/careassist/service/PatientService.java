package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.PatientReqDto;
import com.springboot.careassist.dto.request.PatientUpdateDto;
import com.springboot.careassist.dto.response.*;
import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
import com.springboot.careassist.enums.RiskCategory;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.InvalidCallException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.mapper.PatientMapper;
import com.springboot.careassist.model.Patient;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.PatientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService ;
    private final ClaimRepository claimRepository;
    private final PasswordEncoder passwordEncoder;

    public void addPatient(@Valid PatientReqDto dto) {
        //1. Create User
        User user = userService.getUserObj(dto.username(),
                passwordEncoder.encode(dto.password()),
                Role.PATIENT);
        //2. Map dto to Patient
        Patient patient = PatientMapper.mapDtoToPatient(dto);
        //3. Attach User
        patient.setUser(user);

        patientRepository.save(patient);
    }

    public PaginatedPatientListDto getAllPatient(int page,int size) {
        Pageable pageable =  PageRequest.of(page,size);
        Page<PatientRespDto> list = patientRepository.findAllActive(true,pageable);

        return new PaginatedPatientListDto(
                list.getContent(),
                new PaginationInfo(
                        list.getTotalPages(),list.getTotalElements(),
                        list.stream().toList().size()
                )
        );
    }

    public void updatePatient(String patientUsername, PatientUpdateDto dto) {
        Patient patient = patientRepository.getByUsername(patientUsername);

        boolean flag = false;
        if(!(dto.name()  == null) && !(dto.name().trim().isEmpty())) {
            patient.setName(dto.name());
            flag = true;
        }
        if(!(dto.contact()  == null) && !(dto.contact().trim().isEmpty())) {
            patient.setContact(dto.contact());
            flag = true;
        }
        if(!(dto.riskCategory()  == null)) {
            patient.setRiskCategory(dto.riskCategory());
            flag = true;
        }
        if(!(dto.chronicIllness()  == null) && !(dto.chronicIllness().trim().isEmpty())) {
            patient.setChronicIllness(dto.chronicIllness());
            flag = true;
        }
        if(!(dto.preExisting()  == null) && !(dto.preExisting().trim().isEmpty())) {
            patient.setPreExisting(dto.preExisting());
            flag = true;
        }
        if(!(dto.acuteSymptoms()  == null) && !(dto.acuteSymptoms().trim().isEmpty())) {
            patient.setAcuteSymptoms(dto.acuteSymptoms());
            flag = true;
        }

        if(!flag)
            throw new InvalidCallException("Update op terminated");
        patientRepository.save(patient);
    }

    public PatientRespDtoV2 getPatient(Long patientId) {
        //1. Validate patient id
        Patient patient = patientRepository.findById(patientId)
                        .orElseThrow(()-> new ResourceNotFoundException("Patient not found"));

        return PatientMapper.mapPatientToDtoV2(patient);
    }

    public void deletePatient(Long patientId) {
        //1. Validate patient id
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()-> new ResourceNotFoundException("Patient not found"));

        patient.getUser().setActive(false);
    }

    public PaginatedClaimListDto getClaims(String patientUsername, ClaimType claimType, ClaimStatus claimStatus, LocalDate fromDate,LocalDate toDate, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ClaimRespDtoV2> list = claimRepository.getClaims(patientUsername,claimType,claimStatus,
                fromDate == null? null: fromDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                toDate == null? null: toDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                pageable);

        return new PaginatedClaimListDto(
                list.getContent(),
                new PaginationInfo(
                        list.getTotalPages(),list.getTotalElements(),
                        list.stream().toList().size()
                )
        );
    }

    public PatientRespDtoV2 viewPatient(String patientUsername) {
        Patient patient = patientRepository.getByUsername(patientUsername);
        return PatientMapper.mapPatientToDtoV2(patient);
    }
}
