package com.intranet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.intranet.security.JwtUtils;

import io.jsonwebtoken.Claims;

@Controller
public class TestController {
    @GetMapping("/api/hello")
public ResponseEntity<String> hello(@RequestHeader("Authorization") String authHeader) {
    Claims claims = JwtUtils.decodeJwt(authHeader);
    String email = (String) claims.get("email"); // or claims.get("email") etc.
    return ResponseEntity.ok("Hello " + email);
}
}
