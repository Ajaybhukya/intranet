package com.intranet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.UserDTO;
import com.intranet.security.CurrentUser;

@RestController
public class UserController {

    @GetMapping("/me")
    public UserDTO getUserInfo(@CurrentUser UserDTO user) {
        return user;
    }
}
