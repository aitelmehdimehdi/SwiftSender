package com.PFE.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity extends Person{
    @OneToMany(cascade = CascadeType.ALL)
    private List<ServiceHistorique> serviceHistoriques;
    //Services
    @OneToMany(mappedBy = "clientEntity" , cascade = CascadeType.ALL)
    private List<ServiceEntity> listServices;
    private Boolean active;


    public ClientEntity(String email, String nom , String prenom , String genre
            , String telephone, LocalDate dateNaissance, String password){
        super(nom,
                prenom,
                genre,
                dateNaissance,
                telephone,
                password,
                email);
        this.compte.add(new CompteEntity(this,LocalDate.now()));
        listServices=new LinkedList<>();
        this.active=false;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}

