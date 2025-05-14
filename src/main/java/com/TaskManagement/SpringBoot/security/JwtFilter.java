package com.TaskManagement.SpringBoot.security;

import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.Users.UserAdminRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserClientRepository clientRepo;
    private final UserAdminRepository adminRepo;
    private final UserEmployeeRepository employeeRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Object user = findUserByEmail(email);

            if (user != null) {
                UserDetails userDetails = convertToUserDetails(user);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Object findUserByEmail(String email) {
        return clientRepo.findByEmail(email).<Object>map(c -> c)
                .orElseGet(() ->
                        adminRepo.findByEmail(email).<Object>map(a -> a)
                                .orElseGet(() ->
                                        employeeRepo.findByEmail(email).orElse(null)
                                )
                );
    }

    private UserDetails convertToUserDetails(Object user) {
        if (user instanceof UserClient client) {
            return new User(client.getEmail(), client.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        } else if (user instanceof UserAdmin admin) {
            return new User(admin.getEmail(), admin.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        } else if (user instanceof UserEmployee emp) {
            return new User(emp.getEmail(), emp.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        }

        throw new RuntimeException("Unknown user type");
    }
}
