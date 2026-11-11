package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.HealthCareProviderReqDto;
import com.springboot.careassist.dto.request.HealthCareProviderUpdateReqDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.dto.response.HealthCareProviderRespDto;
import com.springboot.careassist.dto.response.PaginatedProviderListDto;
import com.springboot.careassist.dto.response.PaginationInfo;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.ResourceNotFoundException;
import com.springboot.careassist.mapper.HealthCareProviderMapper;
import com.springboot.careassist.model.HealthCareProvider;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.ClaimRepository;
import com.springboot.careassist.repository.HealthCareProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class HealthCareProviderService {

    private final HealthCareProviderRepository healthCareProviderRepository;
    private final UserService userService;
    private final ClaimRepository claimRepository;
    private final PasswordEncoder passwordEncoder;

    public void insertHealthCareProvider(HealthCareProviderReqDto dto) {
        //1. Create User
        User user = userService.getUserObj(dto.username(),
                passwordEncoder.encode(dto.password()),
                Role.HEALTHCAREPROVIDER);
        //2. Mapper Dto to Provider
        HealthCareProvider healthCareProvider = HealthCareProviderMapper.convertDtoToHealthCareProvider(dto);
        //3. Attach user
        healthCareProvider.setUser(user);

        healthCareProviderRepository.save(healthCareProvider);
    }

    public void updateHealthCareProvider(String providerUsername, HealthCareProviderUpdateReqDto dto) {
        HealthCareProvider provider = healthCareProviderRepository.getByUsername(providerUsername);
        //2. Map dto to provider (Only name is avail)
        provider.setName(dto.name());
        healthCareProviderRepository.save(provider);
    }

    public List<ClaimRespDtoV2> getClaimsByProvider(String providerUsername, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        return claimRepository.getClaimsByProvider(providerUsername,pageable);
    }

    public PaginatedProviderListDto getAll(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page,size);
        //1. Retrieve All Provider from DB
        Page<HealthCareProviderRespDto> list = healthCareProviderRepository.findActiveAll(true,pageable);

        return new PaginatedProviderListDto(
                list.getContent(),
                new PaginationInfo(
                        list.getTotalPages(),list.getTotalElements(),
                        list.stream().toList().size()
                )
        );
    }

    public HealthCareProviderRespDto getById(long providerId) {
        //1. Validate id
        HealthCareProvider provider = healthCareProviderRepository.findById(providerId)
                .orElseThrow(()->new ResourceNotFoundException("Healthcare Id Invalid"));

        return HealthCareProviderMapper.convertHealthCareProviderToDto(provider);
    }

    public void deleteHealthCareProvider(Long providerId) {
        //1. Validate id
        HealthCareProvider provider = healthCareProviderRepository.findById(providerId)
                .orElseThrow(()-> new ResourceNotFoundException("HealthCareProvider not found"));
        //2. Soft delete By setting User Active status.
        provider.getUser().setActive(false);
        healthCareProviderRepository.save(provider);
    }

    public HealthCareProviderRespDto getHealthCareProvider(String username) {
        HealthCareProvider provider = healthCareProviderRepository.getByUsername(username);
        return HealthCareProviderMapper.convertHealthCareProviderToDto(provider);
    }
}
