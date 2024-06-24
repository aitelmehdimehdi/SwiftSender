package com.PFE.Repositry;

import com.PFE.Entity.ClientEntity;
import com.PFE.Entity.ConducteurEntity;
import com.PFE.Entity.ServiceEntity;
import com.PFE.Entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity,Integer> {
    public List<ServiceEntity> getAllByActive(Boolean var);
    public Integer countByActive(Boolean var);
    public List<ServiceEntity> findAllByTypeVehicule(String typeVehicule);
    public List<ServiceEntity> findAllByClientEntity(ClientEntity client);
    public Integer countByConducteur(ConducteurEntity conducteur);
    public Integer countByConducteurAndActive(ConducteurEntity conducteur,Boolean var);
    public Integer countByConducteurEqualsAndDateServiceAfter(ConducteurEntity conducteur,LocalDate date);
    public Integer countByConducteurEqualsAndDansLaVilleEqualsAndDateServiceAfter(ConducteurEntity conducteur, Boolean var,LocalDate date);
    public List<ServiceEntity> findAllByConducteur(ConducteurEntity conducteur);
    public List<ServiceEntity> findAllByConducteurAndDansLaVille(ConducteurEntity conducteur,Boolean var);
}
