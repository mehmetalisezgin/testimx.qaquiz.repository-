package com.testimx.qaquiz.security.services;

import com.testimx.qaquiz.model.User;
import com.testimx.qaquiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service used by Spring Security to look up users from the
 * database.  Delegates to the {@link UserRepository} and wraps
 * {@link com.testimx.qaquiz.model.User} entities in
 * {@link UserDetailsImpl}.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Allow login by username or email for convenience
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
        return UserDetailsImpl.build(user);
    }
}