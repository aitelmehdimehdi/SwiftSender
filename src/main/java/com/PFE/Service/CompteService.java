package com.PFE.Service;

import com.PFE.Assembler.CompteAssembler;
import com.PFE.Entity.CompteEntity;
import com.PFE.Exeption.CompteNotFound;
import com.PFE.Repositry.CompteRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class CompteService {

    private final CompteAssembler compteAssembler;
    private final CompteRepository compteRepository;


    public List<CompteEntity> getAll()
    {
        return  compteRepository.findAll();
    }


    public CompteEntity findById(Integer id)
    {
        return compteRepository.findById(id).orElseThrow(()-> new CompteNotFound(id));
    }


    public CompteEntity createCompte(CompteEntity compte)
    {
      return compteRepository.save(compte);
    }


    public Boolean updateCompte( Integer id, CompteEntity compte)
    {
        if(!compteRepository.existsById(id))
            return false;

        CompteEntity compteEntity =  compteRepository.findById(id).orElseThrow();
        compteEntity.setEmail(compte.getEmail());
        compteEntity.setPassword(compte.getPassword());
        compteEntity.setTypeCompte(compte.getTypeCompte());
        compteEntity.setProprietere(compte.getProprietere());
        compteRepository.save(compteEntity);
        return true;

    }

    public Integer countCompteByDAte(LocalDate date , String typeCompte){
        return compteRepository.countBydateCreationDeCompteAndTypeCompte(date,typeCompte);
    }

    public List<CompteEntity> findCompteByDAte(LocalDate date , String typeCompte){
        return compteRepository.findAllBydateCreationDeCompteAndTypeCompte(date,typeCompte);
    }

    public Integer countCompteByTimeAfter(LocalDate date , String typeComte)
    {
        return compteRepository.countBydateCreationDeCompteAfterAndTypeCompteEquals(date,typeComte);
    }


    public Boolean deleteById( Integer id)
    {
        if (!compteRepository.existsById(id))
            return false;
        compteRepository.deleteById(id);
        return true;
    }

    public CompteEntity findByEmail(String email)
    {
        if(compteRepository.existsByEmail(email))
        {
            return compteRepository.findByEmail(email);
        }
        return null;

    }

    public Boolean existsByEmail(String email)
    {
        return  compteRepository.existsByEmail(email);

    }

    public List<CompteEntity> getAllByEmail(String email)
    {
        return compteRepository.findAllByEmail(email);
    }
}
