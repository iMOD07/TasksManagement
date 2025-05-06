package com.TaskManagement.SpringBoot.security;

import com.TaskManagement.SpringBoot.model.AdminUser;
import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.AdminUserRepository;
import com.TaskManagement.SpringBoot.repository.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.UserEmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserEmployeeRepository employeeRepository;

    @Autowired
    private UserClientRepository clientRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 🛡️ AdminUser
                Optional<AdminUser> adminOpt = adminUserRepository.findByEmail(email);
                if (adminOpt.isPresent() && jwtUtil.validateToken(token)) {
                    AdminUser admin = adminOpt.get();

                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_ADMIN")
                    );

                    var auth = new UsernamePasswordAuthenticationToken(admin, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("✅ Authenticated as: " + email + " with role: ADMIN");
                    chain.doFilter(request, response);
                    return;
                }

                // 🧑‍💼 UserEmployee
                Optional<UserEmployee> empOpt = employeeRepository.findByEmail(email);
                if (empOpt.isPresent() && jwtUtil.validateToken(token)) {
                    UserEmployee emp = empOpt.get();

                    var auth = new UsernamePasswordAuthenticationToken(
                            emp,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + emp.getRole().name()))
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("✅ Authenticated as: " + email + " with role: " + emp.getRole());
                    chain.doFilter(request, response);
                    return;
                }

                // 👤 UserClient
                Optional<UserClient> cliOpt = clientRepository.findByEmail(email);
                if (cliOpt.isPresent() && jwtUtil.validateToken(token)) {
                    UserClient cli = cliOpt.get();

                    var auth = new UsernamePasswordAuthenticationToken(
                            cli,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + cli.getRole().name()))
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("✅ Authenticated as: " + email + " with role: " + cli.getRole());
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // ⛔ لم يتم التوثيق
        chain.doFilter(request, response);
    }
}
