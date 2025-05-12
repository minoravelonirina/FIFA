package org.example.fifa.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String expectedApiKey = System.getenv("api.key");

    private final List<String> publicEndpoints = List.of(
            "/clubs",
            "/clubs/*/test",
            "/clubs/*/coach",
            "/clubs/**",
            "/players",
            "/players/**",
            "/swagger-ui",
            "/v3/api-docs",
            "/seasons",
            "/seasons/**",
            "/matches/**",
            "/matches",
            "/synchronization",
            "/synchronization/**",
            "/bestPlayers",
            "/bestClubs",
            "/championshipRankings",
            "/matchMaker",
            "/",
            "/transfer"

    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublic(path)) {
            System.out.println("Public route, skipping API key check.");
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return publicEndpoints.stream().anyMatch(path::startsWith);
    }
}

