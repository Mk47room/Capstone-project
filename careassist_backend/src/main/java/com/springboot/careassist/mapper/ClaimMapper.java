package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.request.ClaimReqDto;
import com.springboot.careassist.dto.request.ClaimReqDtoV2;
import com.springboot.careassist.dto.response.ClaimAddRespDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.model.Claim;

public class ClaimMapper {

    public static Claim convertDtoToClaim(ClaimReqDto dto){
        Claim claim = new Claim();
        claim.setDiagnosis(dto.diagnosis());
        claim.setTreatment(dto.treatment());
        claim.setClaimedAmount(dto.claimedAmount());
        claim.setAdmissionDate(dto.admissionDate());

        return claim;
    }

    public static Claim convertDtoToClaimV2(ClaimReqDtoV2 dto){
        Claim claim = new Claim();
        claim.setHospitalName(dto.hospitalName());
        claim.setDiagnosis(dto.diagnosis());
        claim.setTreatment(dto.treatment());
        claim.setAdmissionDate(dto.admissionDate());
        claim.setDischargeDate(dto.dischargeDate());
        claim.setClaimedAmount(dto.claimedAmount());

        return claim;
    }

    public static ClaimAddRespDto convertClaimToDto(Claim claim){
        return new ClaimAddRespDto(
                claim.getId(),
                claim.getStatus()
        );
    }

    public static ClaimRespDtoV2 convertClaimToClaimRespDtoV2(Claim claim){
        return new ClaimRespDtoV2(
                claim.getId(),
                claim.getClaimType(),
                claim.getClaimedAmount(),
                claim.getApprovedAmount(),
                claim.getStatus(),
                claim.getDiagnosis(),
                claim.getSubmittedDate(),
                claim.getHealthCareProvider().getName()
        );

    }
}
