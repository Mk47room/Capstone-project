package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.InsurerDto;
import com.springboot.careassist.repository.UserRepository;
import com.springboot.careassist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InsurerController {
    private final UserService userService;

    @PostMapping("/api/insurer/add")
    public void addInsurer(@Valid @RequestBody InsurerDto insurerDto){
        userService.addInsurer(insurerDto);
    }

}
