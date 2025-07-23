package com.intranet.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Parse claims WITHOUT verifying signature
                Claims claims = Jwts.parserBuilder()
                        .build()
                        .parseClaimsJws(token)  
                        .getBody();

                String userId = claims.get("user_id", String.class);
                String email = claims.get("email", String.class);

                List<String> roles = claims.get("roles", List.class);
                List<String> permissions = claims.get("permissions", List.class);

                // Combine roles and permissions into authorities
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) {
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }
                if (permissions != null) {
                    permissions.forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));
                }

                JwtAuthenticationToken authentication =
                        new JwtAuthenticationToken(userId, email, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                // Log or ignore as needed (optional)
            }
        }

        filterChain.doFilter(request, response);
    }
}
