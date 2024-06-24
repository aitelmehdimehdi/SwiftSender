package com.PFE.Repositry;

import com.PFE.Entity.ConducteurEntity;
import com.PFE.Entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConducteurRepository extends JpaRepository<ConducteurEntity,String> {
    public List<ConducteurEntity> findAllByActive(Boolean var);
    public List<ConducteurEntity> findAllByService(ServiceEntity service);
}
