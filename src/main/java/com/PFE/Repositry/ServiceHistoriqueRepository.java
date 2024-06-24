package com.PFE.Repositry;

import com.PFE.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ServiceHistoriqueRepository extends JpaRepository<ServiceHistorique , Integer> {

    public List<ServiceHistorique> findAllByServiceEntity(ServiceEntity service);
    public List<ServiceHistorique> findAllByConducteurEntity(ConducteurEntity conducteur);
    public List<ServiceHistorique> findAllByClientEntity(ClientEntity client);
    public List<ServiceHistorique> findAllByVehicule(Vehicule vehicule);
    public List<ServiceHistorique> findAllByConducteurEntityAndAndServiceEntity_DansLaVille(ConducteurEntity conducteur,Boolean bol);
    public Integer countByConducteurEntity(ConducteurEntity conducteur);
    public Integer countByConducteurEntityEqualsAndServiceEntity_DansLaVilleEqualsAndServiceEntity_DateServiceAfter(
            ConducteurEntity conducteur , Boolean dansLaVille , LocalDate dateService);

}
