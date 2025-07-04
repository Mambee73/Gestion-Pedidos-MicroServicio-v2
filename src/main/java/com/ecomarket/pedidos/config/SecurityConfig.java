package com.ecomarket.pedidos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos la protección CSRF, que no es necesaria para nuestra API
            .csrf(AbstractHttpConfigurer::disable)

            // Aquí viene la regla clave
            .authorizeHttpRequests(authorize -> authorize
                // Le decimos que CUALQUIER petición a CUALQUIER URL sea permitida
                .anyRequest().permitAll()
            );

        return http.build();
    }
}