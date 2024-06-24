package com.PFE.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historiqueServices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceHistorique {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private ServiceEntity serviceEntity;
    @ManyToOne
    private ConducteurEntity conducteurEntity;
    @ManyToOne
    private ClientEntity clientEntity;
    @ManyToOne
    private Vehicule vehicule;

    public ServiceHistorique(ServiceEntity serviceEntity, ConducteurEntity conducteurEntity, ClientEntity clientEntity, Vehicule vehicule) {
        this.serviceEntity = serviceEntity;
        this.conducteurEntity = conducteurEntity;
        this.clientEntity = clientEntity;
        this.vehicule = vehicule;
    }
}
