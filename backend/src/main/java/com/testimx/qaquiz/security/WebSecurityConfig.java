package com.testimx.qaquiz.security;

import com.testimx.qaquiz.security.jwt.AuthEntryPointJwt;
import com.testimx.qaquiz.security.jwt.AuthTokenFilter;
import com.testimx.qaquiz.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for the application.  Configures JWT based
 * stateless authentication and authorisation rules for the REST API.  Public
 * endpoints are permitted while all other endpoints require authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Creates the JWT authentication filter bean.  This filter extracts the
     * bearer token from incoming requests and populates the security context
     * when a valid token is provided.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Password encoder bean using BCrypt.  Used by the authentication
     * provider to verify password hashes.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures a {@link DaoAuthenticationProvider} to load user details
     * from the database via {@link UserDetailsServiceImpl} and validate
     * passwords using the configured encoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} built from the
     * {@link AuthenticationConfiguration}.  This is required for the
     * authentication filter and can also be injected elsewhere.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Defines the security filter chain.  Disables CSRF for API usage,
     * configures exception handling, configures the session to be stateless,
     * defines public endpoints, and registers the JWT filter.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF because our API is stateless and uses JWT tokens
                .csrf(csrf -> csrf.disable())
                // Handle unauthorised requests with our custom entry point
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Disable sessions – we don't need them for token based authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Authorise request mappings
                .authorizeHttpRequests(auth -> auth
                        // Permit authentication and signup endpoints without a token
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permit static assets and the root path (if served)
                        .requestMatchers("/", "/index.html", "/static/**").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated())
                // Register the JWT filter before username/password auth filter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // Set our custom authentication provider
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
}