package com.talenttrack.dto.request;

import com.talenttrack.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String nom;
    private String prenom;
    private String matricule;
    private String titre;
    private String diplome;
    private String etablissement;
    private String descriptionTaches;
    private String email;
    private String adresse;
    private String password;
    private Integer projectId;
    private Role role;
}

