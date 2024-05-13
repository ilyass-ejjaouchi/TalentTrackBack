package com.talenttrack.service.impl;

import com.talenttrack.dto.request.SignUpRequest;
import com.talenttrack.dto.request.SigninRequest;
import com.talenttrack.dto.response.JwtAuthenticationResponse;
import com.talenttrack.dto.response.UserDto;
import com.talenttrack.entities.Role;
import com.talenttrack.entities.User;
import com.talenttrack.repository.UserRepository;
import com.talenttrack.service.AuthenticationService;
import com.talenttrack.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .matricule(request.getMatricule())
                .titre(request.getTitre())
                .diplome(request.getDiplome())
                .etablissement(request.getEtablissement())
                .descriptionTaches(request.getDescriptionTaches())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

}
