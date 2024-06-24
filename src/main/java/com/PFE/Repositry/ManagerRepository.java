package com.PFE.Repositry;

import com.PFE.Entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity,String> {
}
