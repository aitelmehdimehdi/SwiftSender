package com.PFE.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Setter
public class Vehicule{
    @Id
    protected String matricule;
    protected Boolean active;
    protected Integer capaciteACharger;
    protected Integer capaciteReservoir;
    protected Double reservoir;
    protected Double consommation;
    protected LocalDate carteGrise;
    protected LocalDate dateDeFinDassurance;
    protected LocalDate nextVisite;
    @ManyToOne
    protected ServiceEntity service;
    @OneToOne(mappedBy = "vehicule")
    protected ConducteurEntity conducteur;
    protected String typeVehicule;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ServiceHistorique> serviceHistoriques;



    public Vehicule(
                    String matricule,
                    Integer capaciteACharger,
                    Integer capaciteReservoir,
                    Double consommation ,
                    LocalDate dateDeFinDassurance,
                    LocalDate nextVisite,
                    LocalDate carteGrise
    )
    {
        this.matricule=matricule;
        this.active = false;
        this.conducteur=null;
        this.capaciteACharger = capaciteACharger;
        this.capaciteReservoir = capaciteReservoir;
        this.reservoir = 1.0;
        this.consommation = consommation;
        this.dateDeFinDassurance = dateDeFinDassurance;
        this.nextVisite=nextVisite;
        this.carteGrise=carteGrise;
    }

    public Integer getTarifInside(){
        return null;
    }
    public Integer getTarifOutside(){
        return null;
    }
    public String getSimpleType(){return null;}

}
