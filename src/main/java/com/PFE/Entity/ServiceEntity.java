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
        this.conducteur= new LinkedList<>();
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
        for (ConducteurEntity conducteurEntity : conducteurEntities) {
            conducteurEntity.setActive(true);
            conducteurEntity.setService(this);
            conducteurEntity.setVehicule(it.next());
        }
        this.active=true;
    }
    public String getFacture()
    {
        return this.toString();
    }

    public  String toString()
    {
        StringBuilder var= new StringBuilder("------------------------Facture----------------------------"
                + "\n n°Service : " + getNum_service()
                + "\n Nom : " + clientEntity.getNom()
                + "\n Prenom : " + clientEntity.getPrenom()
                + "\n nombre de vehicules : " + nombreVehicule
                + "\n Conducteurs : ");
        for(ConducteurEntity conducteur1 : conducteur) {
            var.append("\n \tnom : ").append(conducteur1.getNom()).append(" - prenom : ").append(conducteur1.getPrenom()).append(" - phone : ").append(conducteur1.getTelephone());
        }
        var.append("\nVehicules : ");
        for(Vehicule vehicule1 : vehicule) {
            var.append("\n \tMatricule : ").append(vehicule1.getMatricule()).append(" - Type de vehicule : ").append(vehicule1.getTypeVehicule());
        }
        var.append("\n Prix : ").append(price).append("\n Date service : ").append(dateService).append("\n ---------------------------------").append("\n ").append(LocalDate.now());

        return var.toString();
    }

}
