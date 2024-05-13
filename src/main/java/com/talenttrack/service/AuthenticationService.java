package com.talenttrack.service;

import com.talenttrack.dto.request.SignUpRequest;
import com.talenttrack.dto.request.SigninRequest;
import com.talenttrack.dto.response.JwtAuthenticationResponse;
import com.talenttrack.dto.response.UserDto;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    void resetPassword(String email, String newPassword);
}
