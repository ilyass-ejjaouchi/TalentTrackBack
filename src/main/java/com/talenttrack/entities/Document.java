package com.talenttrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}
