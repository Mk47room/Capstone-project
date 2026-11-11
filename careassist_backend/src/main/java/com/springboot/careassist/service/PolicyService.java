package com.springboot.careassist.service;

import com.springboot.careassist.dto.response.PaginatedPolicyListDto;
import com.springboot.careassist.dto.response.PaginationInfo;
import com.springboot.careassist.dto.response.PolicyRespDto;
import com.springboot.careassist.exception.AgeInvalidException;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.model.InsurancePlan;
import com.springboot.careassist.model.Patient;
import com.springboot.careassist.model.Policy;
import com.springboot.careassist.repository.InsurancePlanRepository;
import com.springboot.careassist.repository.PatientRepository;
import com.springboot.careassist.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PolicyService {
    private final PatientRepository patientRepository;
    private final InsurancePlanRepository insurancePlanRepository;
    private final PolicyRepository policyRepository;

    public void buyPolicy(String patientUsername, Long planId, int policyYears) {
        //1. Validate id
        InsurancePlan plan = insurancePlanRepository.findById(planId)
                .orElseThrow(()-> new ResourceNotFoundException("Plan Not found"));

        Patient patient = patientRepository.getByUsername(patientUsername);
        Policy policy = new Policy();

        /*2. Assign policy values (PremiumAmount, CoverageAmount, StartingDate, EndingDate)
              2a. Calculate Premium Amount in separate method and set premium amount
         */
        policy.setPremiumAmount(getPremiumAmount(plan, patient));

            //2b. set coverage amount
        policy.setCoverageAmount(plan.getCoverageAmount());

            //2c. set Starting and Ending date
        policy.setStartingDate(LocalDate.now());
        policy.setEndingDate(LocalDate.of(LocalDate.now().getYear()+policyYears,
                                                LocalDate.now().getMonth(),
                                                LocalDate.now().getDayOfMonth()));
        //3. Attach Patient and Plan to Policy
        policy.setPatient(patient);
        policy.setInsurancePlan(plan);
        log.info("Policy bought successfully. Policy ID: "+ policy.getId());
        //4. save in db
        policyRepository.save(policy);

    }

    private static double getPremiumAmount(InsurancePlan plan, Patient patient) {
        // Premium calculation: required(ageFactor, riskFactor)
        //1a. Calculate Age
        int age = Period.between(patient.getDob(),
                                LocalDate.now()).getYears();

        //1b. Validate Age with plan
        if (age < plan.getAgeMin() || age > plan.getAgeMax())
            throw new AgeInvalidException("Plan is not available for age " + age);

        //1c. Assigning AgeFactor.
        double ageFactor = age < 30 ? 1 : age <= 50 ? 1.25 : 1.5;

        //2. Calculating RiskFactor.
        double riskFactor = switch (patient.getRiskCategory()) {
                case NONE -> 1.0;
                case ACUTE_SYMPTOMS -> 1.25;
                case PRE_EXISTING -> 1.5;
                case CHRONIC_ILLNESS -> 1.75;
                };

        return Math.round(plan.getBasePremium() * ageFactor * riskFactor);
    }

    public PaginatedPolicyListDto getAllPolicyByPatient(String patientUsername, int page, int size) {

        Pageable pageable = PageRequest.of(page,size);
        Page<PolicyRespDto> list = policyRepository.getAllPolicyByPatient(patientUsername,pageable);

        return new PaginatedPolicyListDto(
                list.getContent(),
                new PaginationInfo(
                        list.getTotalPages(),list.getTotalElements(),
                        list.stream().toList().size()
                )
        );
    }
}
