package com.gm.security;

import com.gm.repository.UserRepository;
import com.gm.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Slf4j
@AllArgsConstructor
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);
        // Get user info
        Optional<User> userFromDatabase = userRepository.findOneByEmail(username);

        return userFromDatabase.map(user -> {
            // Check if user available
            if (!user.getEnabled()) {
                throw new UserNotActivatedException("User " + username + " was not enabled");
            }
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            // Return user authentication information
            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the " + "database"));
    }
}
