package com.talenttrack.service;

import com.talenttrack.dto.request.SignUpRequest;
import com.talenttrack.dto.request.UpdateUserRequest;
import com.talenttrack.dto.response.UserDto;
import com.talenttrack.entities.Project;
import com.talenttrack.entities.Role;
import com.talenttrack.entities.User;
import com.talenttrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectService projectService;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";


    public UserDto addUser(SignUpRequest request) {
        User user = User.builder()
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

        Optional<Project> savedProjectOpt = projectService.getProjectById(request.getProjectId());
        if (savedProjectOpt.isPresent()) {
            Project savedProject = savedProjectOpt.get();
            user.setProject(savedProject);
        } else {
            throw new IllegalArgumentException("Project not found for ID: " + request.getProjectId());
        }

        User savedUser = userRepository.save(user);

        return mapToUserDto(savedUser);
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MESSAGE);
        }

        userRepository.deleteById(userId);
    }

    public UserDto updateUser(Integer userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        if (request.getNom() != null) {
            user.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            user.setPrenom(request.getPrenom());
        }
        if (request.getMatricule() != null) {
            user.setMatricule(request.getMatricule());
        }
        if (request.getTitre() != null) {
            user.setTitre(request.getTitre());
        }
        if (request.getDiplome() != null) {
            user.setDiplome(request.getDiplome());
        }
        if (request.getEtablissement() != null) {
            user.setEtablissement(request.getEtablissement());
        }
        if (request.getDescriptionTaches() != null) {
            user.setDescriptionTaches(request.getDescriptionTaches());
        }

        if (request.getProjectId() != null){
            projectService.getProjectById(request.getProjectId()).ifPresent(user::setProject);
        }

        User updatedUser = userRepository.save(user);

        return mapToUserDto(updatedUser);
    }



    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    public List<UserDto> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);

        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .matricule(user.getMatricule())
                .titre(user.getTitre())
                .diplome(user.getDiplome())
                .etablissement(user.getEtablissement())
                .descriptionTaches(user.getDescriptionTaches())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        return UserDto.fromUser(user);
    }

    public List<UserDto> getUsersByRoles(List<Role> roles) {
        List<User> users = userRepository.findByRoleIn(roles);
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    public List<UserDto> searchUsersByName(String searchTerm) {
        List<User> users = userRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchTerm, searchTerm);
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

}
