package com.main.library.controller;

import com.main.library.dto.request.AdminDto;
import com.main.library.dto.request.StudentDto;
import com.main.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final UserService userService;

    @PostMapping("/api/student/add")
    public void addStudent(@RequestBody StudentDto dto){
        userService.addStudent(dto);
    }
}
