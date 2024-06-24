package com.PFE.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue
    private Integer num_service;
    @NotNull
    private Integer nombreVehicule;
    //service dans la ville ou au dehors
    private Boolean dansLaVille;
    private String villeSource;
    private String villeDest;
    private String typeVehicule;
    private Integer marchandise;
    @OneToMany(mappedBy = "service")
    private List<Vehicule> vehicule;
    @NotNull
    private Double price;
    private LocalDate dateService;
    @ManyToOne
    private ClientEntity clientEntity;
    @OneToMany(mappedBy = "service")
    private List<ConducteurEntity> conducteur;
    private Boolean active;
    @OneToMany(mappedBy = "serviceEntity")
    private List<ServiceHistorique> historique;

    //Conducteur

    public ServiceEntity(LocalDate dateService, Integer nombreVehicule, ClientEntity clientEntity
            , List<Vehicule> vehicule, String vSource , String vDest , ConducteurEntity ... conducteurEntities) {
        this(dateService, nombreVehicule, clientEntity, vehicule, vSource, vDest, null, conducteurEntities);
    }

    public ServiceEntity(LocalDate dateService, Integer nombreVehicule, ClientEntity clientEntity
            , List<Vehicule> vehicule, String vSource , String vDest, Integer marchandise, ConducteurEntity ... conducteurEntities){
        this.nombreVehicule = nombreVehicule;
        this.clientEntity = clientEntity;
        this.conducteur= new LinkedList<ConducteurEntity>();
        this.vehicule= new LinkedList<>();
        this.villeSource=vSource;
        this.villeDest=vDest;
        this.dateService=dateService;
        this.marchandise=marchandise;
        this.dansLaVille= Objects.equals(this.villeDest, this.villeSource);
        this.vehicule.addAll(vehicule);
        for (Vehicule vehicule1 : vehicule) {
            vehicule1.setActive(true);
            vehicule1.setService(this);
        }
        this.typeVehicule=vehicule.getFirst().getClass().getSimpleName();
        if(dansLaVille){
            this.price=(double) (vehicule.getFirst().getTarifInside()*nombreVehicule);
        }
        else
            this.price=(double) (vehicule.getFirst().getTarifOutside()*nombreVehicule);
        conducteur.addAll(Arrays.asList(conducteurEntities));
        Iterator<Vehicule> it=this.vehicule.iterator();
        for(int i=0 ;i< conducteurEntities.length ; i++) {
            conducteurEntities[i].setActive(true);
            conducteurEntities[i].setService(this);
            conducteurEntities[i].setVehicule(it.next());
        }
        this.active=true;
    }
    public String getFacture()
    {
        return this.toString();
    }

    public  String toString()
    {
        String var= "------------------------Facture----------------------------"
                +"\n n°Service : " + getNum_service()
                +"\n Nom : "+clientEntity.getNom()
                +"\n Prenom : "+clientEntity.getPrenom()
                +"\n nombre de vehicules : "+nombreVehicule
                +"\n Conducteurs : ";
        for(ConducteurEntity conducteur1 : conducteur) {
            var+="\n \tnom : "+conducteur1.getNom()+" - prenom : "+conducteur1.getPrenom()+" - phone : "+conducteur1.getTelephone();
        }
        var+="\nVehicules : ";
        for(Vehicule vehicule1 : vehicule) {
            var+="\n \tMatricule : "+vehicule1.getMatricule()+" - Type de vehicule : "+vehicule1.getTypeVehicule();
        }
        var+="\n Prix : "+price
             +"\n Date service : "+dateService
            +"\n ---------------------------------"
            +"\n "+LocalDate.now();

        return var;
    }

}
