package com.app.security.config;

import com.app.security.dto.AuthenticationErrorDto;
import com.app.security.filter.AppAuthenticationFilter;
import com.app.security.filter.AppAuthorizationFilter;
import com.app.security.service.TokenService;
import com.app.security.service.impl.AppUserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * Web security configuration for the application using Spring Security.
 * <p>
 * It sets up authentication and authorization filters, defines access rules,
 * handles CORS settings, and configures stateless session management.
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AppWebSecurityConfig {

    private final AppUserDetailsServiceImpl appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Configures the main security filter chain.
     *
     * @param http      the HttpSecurity object to configure
     * @param secretKey the secret key used for signing JWT tokens
     * @return the configured SecurityFilterChain
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http, SecretKey secretKey) throws Exception {
        var authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(appUserDetailsService)
                .passwordEncoder(passwordEncoder);

        var authenticationManager = authenticationManagerBuilder.build();

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/users/in/disable")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint());
                    exception.accessDeniedHandler(accessDeniedHandler());
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/swagger-resources/configuration/ui",
                                "/swagger-resources/configuration/security",
                                "/webjars/**",
                                "/favicon.ico")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/users", "/login", "/users/refresh")
                        .permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/users", "/users/lost", "/users/new")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/refresh")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/refresh")
                        .permitAll()
                        .requestMatchers("/users/in/**").hasAnyRole("WORKER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/holidays").hasAnyRole("WORKER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/holidays").hasAnyRole("WORKER", "ADMIN")
                        .requestMatchers("/**").hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .addFilter(new AppAuthenticationFilter(tokenService, authenticationManager))
                .addFilterBefore(new AppAuthorizationFilter(authenticationManager, tokenService),
                        UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .rememberMe(rememberMe -> rememberMe
                        .key(Jwts.SIG.HS512.key().build().toString()));

        return http.build();
    }

    /**
     * Defines a custom authentication entry point that returns
     * a JSON response with an authentication error message.
     *
     * @return the AuthenticationEntryPoint bean
     */
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            var ex = new AuthenticationErrorDto(authException.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    /**
     * Defines a custom access denied handler that returns
     * a JSON response with an access denied error message.
     *
     * @return the AccessDeniedHandler bean
     */
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            var ex = new AuthenticationErrorDto(accessDeniedException.getMessage());
            response.setStatus(405);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    /**
     * Configures CORS to allow requests from the frontend (e.g., localhost:3000),
     * and permits specific headers and HTTP methods.
     *
     * @return the configured CorsConfigurationSource
     */
    private CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                HttpHeaders.ORIGIN,
                "X-Requested-With"
        ));
        corsConfiguration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
