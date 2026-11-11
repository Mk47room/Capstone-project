package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.request.HealthCareProviderReqDto;
import com.springboot.careassist.dto.response.HealthCareProviderRespDto;
import com.springboot.careassist.model.HealthCareProvider;
import org.springframework.stereotype.Component;

@Component
public class HealthCareProviderMapper {

    public static HealthCareProvider convertDtoToHealthCareProvider(HealthCareProviderReqDto healthCareProviderReqDto){
        HealthCareProvider healthCareProvider = new HealthCareProvider();
        healthCareProvider.setName(healthCareProviderReqDto.name());
        return healthCareProvider;
    }

    public static HealthCareProviderRespDto convertHealthCareProviderToDto(HealthCareProvider provider){
        return new HealthCareProviderRespDto(
                provider.getId(),
                provider.getName(),
                provider.getUser().isActive(),
                provider.getUser().getCreatedAt()
        );
    }
}
