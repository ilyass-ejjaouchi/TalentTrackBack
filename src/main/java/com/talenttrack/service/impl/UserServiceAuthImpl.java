package com.talenttrack.service.impl;

import com.talenttrack.repository.UserRepository;
import com.talenttrack.service.UserServiceAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceAuthImpl implements UserServiceAuth {
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";


    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
            }
        };
    }
}
