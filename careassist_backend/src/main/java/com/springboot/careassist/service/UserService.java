package com.springboot.careassist.service;

import com.springboot.careassist.dto.request.AdminDto;
import com.springboot.careassist.dto.request.InsurerDto;
import com.springboot.careassist.dto.response.DetailsDto;
import com.springboot.careassist.enums.Role;
import com.springboot.careassist.exception.InactiveUserException;
import com.springboot.careassist.model.User;
import com.springboot.careassist.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserObj(String username, String password, Role role){
        User user =  new User(username,password,role);
        return userRepository.save(user);
    }

    public void addAdmin(AdminDto adminDto) {
        getUserObj(
                adminDto.username(),
                passwordEncoder.encode(adminDto.password()),
                Role.ADMIN);
    }

    public void addInsurer(@Valid InsurerDto insurerDto) {
        getUserObj(
                insurerDto.username(),
                passwordEncoder.encode(insurerDto.password()),
                Role.INSURER
        );
    }

    public DetailsDto getUserDetails(String loggedInUsername) {
        DetailsDto detailsDto = userRepository.getUserDetails(loggedInUsername);
        if(! detailsDto.isActive()){
            throw new InactiveUserException("User is Disabled");
        }
        return detailsDto;
    }
}
