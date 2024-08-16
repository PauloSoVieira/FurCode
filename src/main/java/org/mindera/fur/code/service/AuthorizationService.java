package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service class for handling authorization.
 */
@Schema(description = "The authorization service")
public class AuthorizationService implements UserDetailsService {

    /**
     * Constructor for the AuthorizationService.
     *
     * @param personRepository the personRepository
     */
    @Autowired
    PersonRepository personRepository;

    /**
     * Loads a user by their username.
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByEmail(username);
    }

}
