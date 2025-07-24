package com.intranet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

import java.util.Base64;

public class JwtUtils {

    public static Claims decodeJwt(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String[] parts = token.split("\\.");
        String payload = new String(Base64.getDecoder().decode(parts[1]));
        return Jwts.parserBuilder().build().parseClaimsJwt(parts[0] + "." + parts[1] + ".").getBody();
    }

}
