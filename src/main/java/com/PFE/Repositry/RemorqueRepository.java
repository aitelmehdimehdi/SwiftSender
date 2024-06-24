package com.PFE.Repositry;

import com.PFE.Entity.RemorqueEntity;
import com.PFE.Entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemorqueRepository extends JpaRepository<RemorqueEntity,String>{
    public List<Vehicule> findAllByActive(Boolean var);

}
