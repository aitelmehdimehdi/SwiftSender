package com.PFE.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
public abstract class Person {
    @Id
    protected String email;
    protected String fullname;
    protected String nom;
    protected String prenom;
    protected String genre;
    protected LocalDate dateNaissance;
    protected String telephone;
    protected String password;
    @OneToMany(mappedBy = "proprietere")
    Collection<CompteEntity> compte;

    public Person(String nom, String prenom, String genre,
                  LocalDate dateNaissance, String telephone,String password , String email) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.password = passwordEncoder.encode(password);
        this.email = email;
        this.compte=new LinkedList<CompteEntity>();
        this.fullname=prenom+" "+nom;
    }
    public abstract String getType();

    public CompteEntity getCompte(String type)
    {
        for(CompteEntity cmp : compte)
        {
            if(Objects.equals(cmp.getTypeCompte(), type))
                return cmp;
        }
        return null;
    }
}
