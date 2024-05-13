package com.talenttrack.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceAuth {
    UserDetailsService userDetailsService();
}
