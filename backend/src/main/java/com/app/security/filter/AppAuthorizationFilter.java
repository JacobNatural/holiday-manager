package com.app.security.filter;

import com.app.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;
import java.io.IOException;
import java.util.Arrays;

/**
 * Custom filter to handle authorization by parsing the access token from the request.
 * <p>
 * This filter checks the HTTP request for an Authorization header containing a Bearer token,
 * parses the token using {@link TokenService}, and sets the authentication in the security context
 * if the token is valid. This allows the application to identify the authenticated user and their roles.
 * </p>
 */
public class AppAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenService tokenService;

    /**
     * Constructs a new {@link AppAuthorizationFilter} with the specified {@link AuthenticationManager}
     * and {@link TokenService}.
     *
     * @param authenticationManager the authentication manager used to authenticate the user
     * @param tokenService          the service responsible for parsing and validating the access token
     */
    public AppAuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    /**
     * Filters the incoming request to parse the Authorization header and extract the access token.
     * <p>
     * If the Authorization header is present and contains a valid Bearer token, this method will
     * parse the token using the {@link TokenService} and set the authentication in the security context.
     * </p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();

        System.out.println(uri);
        var permitAllUris = List.of(
                "/users", "/login", "/users/refresh", "/users/lost", "/users/new");

        var permitAllPrefixes = List.of(
                "/swagger-ui",
                "/.well-known",
                "/v3/api-docs"
        );

        boolean isPermittedUri = permitAllUris.contains(uri) ||
                permitAllPrefixes.stream().anyMatch(uri::startsWith);

        if (!isPermittedUri) {

            var cookies = request.getCookies();
            if (cookies != null) {

                var accessToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("AccessToken"))
                        .findFirst()
                        .map(cookie -> cookie.getValue())
                        .orElse(null);

                if (accessToken != null) {
                    try {
                        // Parse the access token and set the authentication context
                        var authorizedUser = tokenService.parseAccessToken("Bearer " + accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authorizedUser);
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                    }
                }
            }
        }
        // Proceed with the next filter in the chain
        chain.doFilter(request, response);
    }
}
