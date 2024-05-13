package com.talenttrack.dto.response;

import com.talenttrack.entities.Role;
import com.talenttrack.entities.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String nom;
    private String prenom;
    private String matricule;
    private String titre;
    private String diplome;
    private String etablissement;
    private String descriptionTaches;
    private String email;
    private Role role;
    private Integer projetId;

    public static UserDto fromUser(User user) {
        UserDto.UserDtoBuilder builder = UserDto.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .matricule(user.getMatricule())
                .titre(user.getTitre())
                .diplome(user.getDiplome())
                .etablissement(user.getEtablissement())
                .descriptionTaches(user.getDescriptionTaches())
                .email(user.getEmail())
                .role(user.getRole());

        if (user.getProject() != null) {
            builder.projetId(user.getProject().getId());
        }

        return builder.build();
    }

}
