package com.talenttrack.controller;

import com.talenttrack.dto.request.SignUpRequest;
import com.talenttrack.dto.request.UpdateUserRequest;
import com.talenttrack.dto.response.UserDto;
import com.talenttrack.entities.Role;
import com.talenttrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody SignUpRequest request) {
        UserDto newUser = userService.addUser(request);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer userId, @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/byRole/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable Role role) {
        List<UserDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/selectByRoles")
    public ResponseEntity<List<UserDto>> getUsersByRoles(@RequestParam List<Role> roles) {
        List<UserDto> users = userService.getUsersByRoles(roles);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsersByName(@RequestParam String searchTerm) {
        List<UserDto> users = userService.searchUsersByName(searchTerm);
        return ResponseEntity.ok(users);
    }

}

