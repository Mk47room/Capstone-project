package com.main.library.service;


import com.main.library.dto.request.AdminDto;
import com.main.library.dto.request.StudentDto;
import com.main.library.enums.Role;
import com.main.library.model.User;
import com.main.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addAdmin(AdminDto adminDto) {
        //1. Prepare User using AdminDto
        User user = new User(
                adminDto.username(),
                passwordEncoder.encode(adminDto.password()),
                Role.ADMIN
        );

        //2. Save user in DB
        userRepository.save(user);
    }

    public void addStudent(StudentDto dto) {
        User user = new User(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                Role.STUDENT
        );

        // Save user in DB
        userRepository.save(user);
    }
}
