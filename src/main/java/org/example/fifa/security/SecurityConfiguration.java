package org.example.fifa.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .build();


        return httpSecurity

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                "/v2/api-docs",
                                                "/v3/api-docs",
                                                "/v3/api-docs/**",
                                                "/swagger-resources",
                                                "/swagger-resources/**",
                                                "/configuration/ui",
                                                "/configuration/security",
                                                "/swagger-ui/**",
                                                "/webjars/**",
                                                "/swagger-ui.html"
                                        )
                                        .permitAll()
                                        .requestMatchers(GET, "/clubs").permitAll()
                                        .requestMatchers(GET, "/clubs/**").permitAll()
                                        .requestMatchers(GET, "/clubs/statistics/**").permitAll()
                                        .requestMatchers(POST, "/clubs").permitAll()
                                        .requestMatchers(GET, "/players").permitAll()
                                        .requestMatchers(PUT, "/players").permitAll()
                                        .requestMatchers(POST, "/synchronization").permitAll()
                                        .requestMatchers(GET, "/synchronization/**").permitAll()
                                        .requestMatchers(GET, "/matches/**").permitAll()
                                        .requestMatchers(POST, "/matches/**").permitAll()
                                        .requestMatchers(GET, "/players/**").permitAll()
                                        .requestMatchers(PUT, "/players/**").permitAll()
                                        .requestMatchers(GET, "/").permitAll()
                                        .requestMatchers(POST, "/matchMaker/**").permitAll()
                                        .requestMatchers(GET, "/ping").permitAll()
                                        .requestMatchers(GET, "/pong").permitAll()
                                        .requestMatchers(GET, "/players/*/statistics/**").permitAll()
                                        .requestMatchers(PUT, "/matches/**").permitAll()
                                        .requestMatchers(PUT, "/seasons/**").permitAll()
                                        .requestMatchers(POST, "/seasons/**").permitAll()
                                        .requestMatchers(GET, "/seasons/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .addFilterBefore(new ApiKeyAuthFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "*"));
        configuration.setExposedHeaders(List.of("X-Total-Count"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}