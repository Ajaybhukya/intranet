package com.intranet.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.UserDTO;
import com.intranet.dto.UserSDTO;
import com.intranet.security.CurrentUser;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @GetMapping("/me")
    public UserDTO getUserInfo(@CurrentUser UserDTO user) {
        return user;
    }

    @GetMapping("/users")
    public List<UserSDTO> getAllMockUsers() {
        return Arrays.asList(
            new UserSDTO(1L, "Ajay Kumar", "ajay@example.com"),
        new UserSDTO(2L, "Sonal Mehta", "sonal@example.com"),
        new UserSDTO(3L, "Rahul Sharma", "rahul@example.com"),
        new UserSDTO(4L, "Nikita Das", "nikita@example.com"),
        new UserSDTO(101L, "Pankaj Kumar", "pankaj@example.com"),
        new UserSDTO(102L, "Amit Kumar", "amit@example.com"),
        new UserSDTO(103L, "Rohit Sharma", "rohit@example.com")
        );
    }
}
