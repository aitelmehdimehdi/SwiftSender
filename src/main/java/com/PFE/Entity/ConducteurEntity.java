package com.PFE.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "conducteur")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConducteurEntity extends Person{
    private Double salary;
    private Boolean active;

    @OneToOne
    private Vehicule vehicule;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ServiceHistorique> serviceHistoriques;
    @ManyToOne(cascade = CascadeType.ALL)
    private ServiceEntity service;

    public ConducteurEntity(String email, String nom , String prenom , String genre
            , String telephone, LocalDate dateNaissance, String password,Double salary){
        super(nom,
                prenom,
                genre,
                dateNaissance,
                telephone,
                password,
                email);
        this.compte=new LinkedList<CompteEntity>();
        this.compte.add(new CompteEntity(this,LocalDate.now()));
        this.active=false;
        this.vehicule=null;
        this.service=null;
        this.salary=salary;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void endTask() {
        this.active = false;
        this.vehicule=null;
        this.service=null;
    }
}
