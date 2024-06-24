package com.PFE.Repositry;

import com.PFE.Entity.CamionEntity;
import com.PFE.Entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<CamionEntity,String> {
    public List<Vehicule> findAllByActive(Boolean var);
}
