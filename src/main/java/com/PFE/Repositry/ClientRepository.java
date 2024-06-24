package com.PFE.Repositry;

import com.PFE.Entity.ClientEntity;
import com.PFE.Entity.CompteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,String>{
    Boolean existsByEmail(String email);
    ClientEntity findByEmail(String email);
    ClientEntity findByCompte(CompteEntity compte);
}
