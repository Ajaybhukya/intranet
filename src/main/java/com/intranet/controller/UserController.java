package com.intranet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.UserDTO;
import com.intranet.security.CurrentUser;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/me")
    public UserDTO getUserInfo(@CurrentUser UserDTO user) {
        return user;
    }
}
