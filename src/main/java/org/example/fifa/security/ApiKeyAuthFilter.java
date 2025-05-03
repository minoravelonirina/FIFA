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

//    @Value("${api.key}")
    private final String expectedApiKey = System.getenv("api.key");

    private final List<String> publicEndpoints = List.of(
            "/clubs",
            "/clubs/*/coach",
            "/clubs/**",
            "/players",
            "/players/**",
            "/swagger-ui",
            "/v3/api-docs",
            "/seasons",
            "/seasons/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
//        System.out.println("Request path: " + path);  // Log du chemin de la requête

        // Laisser passer les routes publiques
        if (isPublic(path)) {
//            System.out.println("Public route, skipping API key check.");  // Log pour les routes publiques
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifier l’API key sur les routes protégées
        String apiKey = request.getHeader("X-API-KEY");
//        System.out.println("API Key: " + apiKey);  // Log de la clé API

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

