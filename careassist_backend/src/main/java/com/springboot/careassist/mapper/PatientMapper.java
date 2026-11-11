package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.request.PatientReqDto;
import com.springboot.careassist.dto.response.PatientRespDto;
import com.springboot.careassist.dto.response.PatientRespDtoV2;
import com.springboot.careassist.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public static Patient mapDtoToPatient(PatientReqDto dto){

        Patient patient = new Patient();

        patient.setName(dto.name());
        patient.setDob(dto.dob());
        patient.setGender(dto.gender());
        patient.setContact(dto.contact());
        patient.setRiskCategory(dto.riskCategory());
        patient.setChronicIllness((dto.chronicIllness()));
        patient.setPreExisting(dto.preExisting());
        patient.setAcuteSymptoms(dto.acuteSymptoms());

        return patient;
    }

    public static PatientRespDto mapPatientToDto(Patient patient){
        return new PatientRespDto(
                patient.getId(),
                patient.getName(),
                patient.getDob(),
                patient.getGender(),
                patient.getContact(),
                patient.getRiskCategory(),
                patient.getUser().isActive(),
                patient.getUser().getCreatedAt()
        );
    }

    public static PatientRespDtoV2 mapPatientToDtoV2(Patient patient){
        return new PatientRespDtoV2(
                patient.getName(),
                patient.getDob(),
                patient.getContact(),
                patient.getRiskCategory(),
                patient.getChronicIllness(),
                patient.getPreExisting(),
                patient.getAcuteSymptoms()
        );
    }
}
