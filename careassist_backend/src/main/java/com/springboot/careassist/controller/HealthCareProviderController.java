package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.HealthCareProviderReqDto;
import com.springboot.careassist.dto.request.HealthCareProviderUpdateReqDto;
import com.springboot.careassist.dto.response.ClaimRespDtoV2;
import com.springboot.careassist.dto.response.HealthCareProviderRespDto;
import com.springboot.careassist.dto.response.PaginatedProviderListDto;
import com.springboot.careassist.model.HealthCareProvider;
import com.springboot.careassist.service.HealthCareProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HealthCareProviderController {

    private final HealthCareProviderService healthCareProviderService;

    @PostMapping("/api/healthcareprovider/add")
    public void insertHealthCareProvider(@Valid @RequestBody HealthCareProviderReqDto healthCareProviderReqDto){
         healthCareProviderService.insertHealthCareProvider(healthCareProviderReqDto);
    }

    @GetMapping("/api/healthcareprovider/view")
    public HealthCareProviderRespDto getHealthCareProvider(Principal principal){
        return healthCareProviderService.getHealthCareProvider(principal.getName());
    }

    @PutMapping("/api/healthcareprovider/update")
    public void updateHealthCareProvider(Principal principal,
                                         @Valid @RequestBody HealthCareProviderUpdateReqDto dto){
        healthCareProviderService.updateHealthCareProvider(principal.getName(),dto);
    }

    @GetMapping("/api/healthcareprovider/claim/all")
    public List<ClaimRespDtoV2> getClaims(Principal principal,
                                          @RequestParam(name = "page",required = false,defaultValue = "0")Integer page,
                                          @RequestParam(name = "size",required = false,defaultValue = "20") Integer size){
        return healthCareProviderService.getClaimsByProvider(principal.getName(),page,size);
    }

    //admin control
    @GetMapping("/api/healthcareprovider/all")
    public PaginatedProviderListDto getAll(@RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
                                           @RequestParam(name = "size",required = false,defaultValue = "5") Integer size){
        return healthCareProviderService.getAll(page,size);
    }

    // admin control
    @GetMapping("/api/healthcareprovider/{id}")
    public HealthCareProviderRespDto getById(@PathVariable long id){
        return healthCareProviderService.getById(id);
    }

    //admin control
    @PutMapping("/api/healthcareprovider/deactive/{providerId}")
    public void deleteHealthCareProvider(@PathVariable Long providerId){
        healthCareProviderService.deleteHealthCareProvider(providerId);
    }

}
