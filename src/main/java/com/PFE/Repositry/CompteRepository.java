package com.PFE.Repositry;

import com.PFE.Entity.CompteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<CompteEntity,Integer> {
    public Boolean existsByEmail(String email);
    public CompteEntity findByEmail(String email);
    public void deleteByEmailAndTypeCompte(String email, String typeCompte);
    public CompteEntity findByEmailAndTypeCompte(String email,String typeCompte);
    public LinkedList<CompteEntity> findAllByEmail(String email);

    public Integer countBydateCreationDeCompteAndTypeCompte(LocalDate date, String typeCompte);
    public List<CompteEntity> findAllBydateCreationDeCompteAndTypeCompte(LocalDate date, String typeCompte);

    public Integer countBydateCreationDeCompteAfterAndTypeCompteEquals(LocalDate date,String typeCompte);
    public List<CompteEntity> findBydateCreationDeCompteAfterAndTypeCompteEquals(LocalDate date,String typeCompte);

}
