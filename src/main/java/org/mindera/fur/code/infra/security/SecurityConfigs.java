package org.mindera.fur.code.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Component;

@Component

@Configuration //just a class of configuration
@EnableWebSecurity //this annotation is used to enable the security
public class SecurityConfigs {

    @Autowired
    SecurityFilter securityFilter;

    //filters to check the authentication
    @Bean
    public SecurityFilterChain securityFilters(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //configuração padrão
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers(HttpMethod.POST, "/api/v1/person/{id}/create-shelter").hasAuthority("MANAGER")
                        //.requestMatchers(HttpMethod.GET, "/api/v1/person/all").hasAuthority("MANAGER")
                        //.requestMatchers(HttpMethod.GET, "/api/v1/person/{id}").hasAuthority("MANAGER")
                        .anyRequest().permitAll())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //usa algoritimo de hash para criptografar a password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
//autenticação statefull é utilizada para armazenar informações de usuário que está ativo
//autenticação stateless realiza autenticação via token de sessão (padrão mais utilizado na web neste momento)