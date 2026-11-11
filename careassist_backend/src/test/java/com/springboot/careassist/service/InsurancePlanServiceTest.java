package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.InsurancePlanReqDto;
import com.springboot.careassist.dto.response.InsurancePlanRespDto;
import com.springboot.careassist.model.InsurancePlan;
import com.springboot.careassist.repository.InsurancePlanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InsurancePlanServiceTest {
    @Mock
    private InsurancePlanRepository insurancePlanRepository;

    @InjectMocks
    private InsurancePlanService insurancePlanService;

    @Test
    public void addPlanTest(){
        InsurancePlanReqDto dto = new InsurancePlanReqDto("Basic Plan",2000d,200000d,18,20);

        when(insurancePlanRepository.save(any(InsurancePlan.class))).thenReturn(any(InsurancePlan.class));
        insurancePlanService.addPlan(dto);

        verify(insurancePlanRepository,times(1)).save(any(InsurancePlan.class));
        verifyNoMoreInteractions(insurancePlanRepository);
    }

    @Test
    public void getAllPlanTest(){
        InsurancePlanRespDto dto = new InsurancePlanRespDto(1L,"Basic Plan",2000d,200000d,18,40);
        InsurancePlanRespDto dto2 = new InsurancePlanRespDto(2L,"Premium Plan",5000d,900000d,25,60);
        List<InsurancePlanRespDto> list = List.of(dto,dto2);
        Page<InsurancePlanRespDto> pageList = new PageImpl<>(list);

        when(insurancePlanRepository.findAllPlans(0d,0d,23,PageRequest.of(0,5)))
                .thenReturn(pageList);
        insurancePlanService.getAllPlan(0d,0d,23,0,5);

        verify(insurancePlanRepository,times(1)).findAllPlans(0d,0d,23,PageRequest.of(0,5));
        verifyNoMoreInteractions(insurancePlanRepository);
    }

    @Test
    public void updatePlanTest(){
        InsurancePlanReqDto dto = new InsurancePlanReqDto("Basic Plan",2500d,200000d,18,60);
        Long planId = 1L;
        InsurancePlan plan = new InsurancePlan();
        plan.setId(planId);
        plan.setBasePremium(2000d);
        plan.setAgeMax(40);

        when(insurancePlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(insurancePlanRepository.save(any(InsurancePlan.class))).thenReturn(plan);
        insurancePlanService.updatePlan(planId, dto);

        assertEquals(2500,plan.getBasePremium());
        verify(insurancePlanRepository,times(1)).save(any(InsurancePlan.class));
        verifyNoMoreInteractions(insurancePlanRepository);
    }
}
