package com.talenttrack.controller;

import com.talenttrack.dto.request.SignUpRequest;
import com.talenttrack.dto.request.SigninRequest;
import com.talenttrack.dto.response.JwtAuthenticationResponse;
import com.talenttrack.dto.response.UserDto;
import com.talenttrack.service.AuthenticationService;
import com.talenttrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/password/reset")
    public void resetPassword(@RequestParam String email,
                              @RequestParam String newPassword) {
        authenticationService.resetPassword(email, newPassword);
    }

    @PostMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
