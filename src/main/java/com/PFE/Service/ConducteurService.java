package com.PFE.Service;


import com.PFE.Entity.*;
import com.PFE.Exeption.ConducteurNotFound;
import com.PFE.Repositry.CompteRepository;
import com.PFE.Repositry.ConducteurRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class ConducteurService {
    private final ConducteurRepository conducteurRepository;
    private final CompteRepository compteRepository;
    ///private final ServiceService serviceService;
    private final ServiceHistoriqueService historiqueService;


    public Integer countConducteurs()
    {
        return (int)conducteurRepository.count();
    }

    public ConducteurEntity addConducteur( ConducteurEntity conducteur)
    {
         ConducteurEntity conducteur1=conducteurRepository.save(conducteur);
        if(conducteur.getCompte(conducteur.getClass().getSimpleName()) != null)
        {
            compteRepository.save(conducteur.getCompte(conducteur.getClass().getSimpleName()));
        }
        return conducteur1;
    }



    public ConducteurEntity getByEmail(String mail)
    {
        return conducteurRepository.findById(mail).orElseThrow(()->new ConducteurNotFound(mail));
    }


    public List<ConducteurEntity> getAll()
    {
        return  conducteurRepository.findAll();
    }



    public Boolean editConducteur(String email, ConducteurEntity conducteur)
    {
        if(!conducteurRepository.existsById(email))
            return false;

        ConducteurEntity conducteur1=conducteurRepository.findById(email).orElseThrow(
                ()->new ConducteurNotFound(email));
        conducteur1.setPrenom(conducteur.getPrenom());
        conducteur1.setEmail(conducteur.getEmail());
        conducteur1.setNom(conducteur.getNom());
        conducteur1.setTelephone(conducteur.getTelephone());
        conducteur1.setDateNaissance(conducteur.getDateNaissance());
        conducteur1.setFullname(conducteur1.getPrenom()+" "+conducteur1.getNom());
        conducteurRepository.save(conducteur1);
        return true;
    }



    public Boolean deleteByEmail( String email)
    {
        if(conducteurRepository.existsById(email))
        {
            ConducteurEntity conducteur=conducteurRepository.findById(email).orElseThrow();
            if(conducteur.getActive())
                return false;
            historiqueService.disalowConducteur(conducteur);
            compteRepository.deleteByEmailAndTypeCompte(conducteur.getEmail(),conducteur.getType());
            conducteurRepository.deleteById(email);
            return  true;
        }
        return false;
    }

    public String conducteurlogin(CompteEntity compte, HttpSession session)
    {
        session.setAttribute("conducteurLoggedIn" , conducteurRepository.findById(compte.getEmail()).
                orElseThrow(()->new ConducteurNotFound(compte.getEmail())));
        return "redirect:conducteur";
    }

//    public Integer countService(ConducteurEntity conducteur)
//    {
//        return serviceService.countServiceByConducteur(conducteur);
//    }
    public Integer countAllServices(ConducteurEntity conducteur)
    {
        return historiqueService.countServiceByConducteur(conducteur);
    }

    public Integer countServiceInVilleParMois(ConducteurEntity conducteur)
    {
        return historiqueService.countServiceByConducteurInOrOutVilleParPeriode(conducteur, true,LocalDate.now().minusMonths(1));
    }

    public Integer countServiceOutVilleParMois(ConducteurEntity conducteur)
    {
        return historiqueService.countServiceByConducteurInOrOutVilleParPeriode(conducteur, false,LocalDate.now().minusMonths(1));
    }

    public List<ServiceHistorique> getAllServiceInVille(ConducteurEntity conducteur)
    {
        return historiqueService.findAllByConducteurAndInOrOutVille(conducteur,true);
    }
    public List<ServiceHistorique> getAllServiceOutVille(ConducteurEntity conducteur)
    {
        return historiqueService.findAllByConducteurAndInOrOutVille(conducteur,false);
    }
    public List<ServiceHistorique> getAllService(ConducteurEntity conducteur)
    {
        return historiqueService.findAllByConducteur(conducteur);
    }


    public Boolean updateClientPassword(String email , ConducteurEntity conducteur)
    {
        if(!conducteurRepository.existsById(email))
        {
            return false;
        }
        else {
            ConducteurEntity conducteur1 = conducteurRepository.findById(email).orElseThrow(
                    () -> new ConducteurNotFound(email));
            conducteur1.setPassword(conducteur.getPassword());
            for(CompteEntity compte : compteRepository.findAllByEmail(email))
            {
                if(Objects.equals(compte.getTypeCompte(), ConducteurEntity.class.getSimpleName())) {
                    compte.setPassword(conducteur.getPassword());
                    compteRepository.save(compte);
                }
            }
            conducteurRepository.save(conducteur1);
            return true;
        }
    }

}
