package com.PFE.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "compte",uniqueConstraints = @UniqueConstraint(columnNames = {"email", "password","type"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompteEntity{
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @NotNull
    private LocalDate dateCreationDeCompte;

    @Column(name = "type")
    @NotNull
    private String typeCompte;

    @ManyToOne()
    private Person proprietere;

    public CompteEntity(Person proprietere , LocalDate date) {
        this.email=proprietere.getEmail();
        this.password=proprietere.getPassword();
        this.typeCompte=proprietere.getClass().getSimpleName();
        this.proprietere = proprietere;
        this.dateCreationDeCompte=date;
    }
}
