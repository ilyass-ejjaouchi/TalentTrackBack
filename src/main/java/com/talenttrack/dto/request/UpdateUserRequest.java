package com.talenttrack.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String nom;
    private String prenom;
    private String matricule;
    private String titre;
    private String diplome;
    private String etablissement;
    private String descriptionTaches;
    private Integer projectId;
}
