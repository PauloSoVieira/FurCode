package org.mindera.fur.code.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@Schema(description = "Security configuration")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    /**
     * Security configuration
     *
     * @param httpSecurity The http security
     * @return The security filter chain
     * @throws Exception The exception
     */

    //filters to check the authentication
    @Bean
    public SecurityFilterChain securityFilters(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        //Person authorizations
                        //anyone can create a person
                        .requestMatchers(HttpMethod.POST, "/api/v1/person/{id}/create-shelter").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/person/all").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/person/{id}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/person/update/{id}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/person/set-person-role/{id}").hasAnyAuthority("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/person/delete/{id}").hasAnyAuthority("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/person/{id}/add-person-to-shelter").hasAnyAuthority("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/person/{id}/get-all-donations").hasAnyAuthority("ADMIN")

                        //Pet authorizations
                        .requestMatchers(HttpMethod.POST, "/api/v1/pet").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/pet/update/{id}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pet/delete/{id}").hasAnyAuthority("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/pet/{id}/create-record").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/pet/{id}/record").hasAnyAuthority("ADMIN")

                        //Shelter authorizations
                        .requestMatchers(HttpMethod.POST, "/api/v1/shelter").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/shelter/all").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/shelter/{id}").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/shelter/delete/{id}").hasAnyAuthority("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/shelter/update/{id}").hasAnyAuthority("MANAGER")

                        //Adoption Request authorizations
                        .requestMatchers(HttpMethod.POST, "/api/v1/adoption-request").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/adoption-request/update/{id}").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/adoption-request/all").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/adoption-request/{id}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/adoption-request/delete/{id}").hasAnyAuthority("MANAGER")


                        .anyRequest().permitAll())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Authentication manager
     *
     * @param authenticationConfiguration The authentication configuration
     * @return The authentication manager
     * @throws Exception The exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Password encoder
     *
     * @return The password encoder
     */
    //usa algoritimo de hash para criptografar a password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
//autenticação statefull é utilizada para armazenar informações de usuário que está ativo
//autenticação stateless realiza autenticação via token de sessão (padrão mais utilizado na web neste momento)