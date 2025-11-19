package com.cnotar7.projects.aigolfcompanion.config;

import com.cnotar7.projects.aigolfcompanion.service.UserDetailsServiceImpl;
import com.cnotar7.projects.aigolfcompanion.util.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    //private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // H2 console

                // Allow all requests without authentication
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
                /*
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // For H2 Database Debugging
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // For H2 Database Debugging

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT is stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/golfcompanion/auth/**").permitAll() // public endpoints
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated() // all other endpoints require authentication
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
*/
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
