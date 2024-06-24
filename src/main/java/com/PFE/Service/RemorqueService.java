package com.PFE.Service;


import com.PFE.Assembler.RemorqueAssembler;
import com.PFE.Entity.*;
import com.PFE.Exeption.VehiculeNotFound;
import com.PFE.Repositry.RemorqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Service
@RequiredArgsConstructor
public class RemorqueService {
     //private final ServiceHistoriqueService historiqueService;
     private final RemorqueRepository remorqueRepository;

    public Integer countRemorques()
    {
        return (int) remorqueRepository.count();
    }


    public List<RemorqueEntity> getAll()
    {
        return remorqueRepository.findAll();
     }


    public RemorqueEntity findByMatricule(String matricule)
    {
        if(remorqueRepository.existsById(matricule))
            return remorqueRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        return null;
    }


    public RemorqueEntity addRemorque(RemorqueEntity remorque)
    {
        return remorqueRepository.save(remorque);
    }


    public Boolean updateRemorque(String matricule, RemorqueEntity remorqueEntity)
    {
        if(!remorqueRepository.existsById(matricule))
            return false;
        RemorqueEntity remorque=remorqueRepository.findById(matricule).orElseThrow();
        remorque.setActive(false);
        remorque.setConsommation(remorqueEntity.getConsommation());
        remorque.setReservoir(1.0);
        remorque.setCapaciteReservoir(remorqueEntity.getCapaciteReservoir());
        remorque.setCapaciteACharger(remorqueEntity.getCapaciteACharger());
        remorque.setDateDeFinDassurance(remorqueEntity.getDateDeFinDassurance());
        remorque.setMatricule(remorqueEntity.getMatricule());
        remorqueRepository.save(remorque);
        return true;
    }

    public Boolean updateRemorque(String matricule, Vehicule remorqueEntity)
    {
        if(!remorqueRepository.existsById(matricule))
            return false;
        RemorqueEntity remorque=remorqueRepository.findById(matricule).orElseThrow();
        remorque.setActive(false);
        remorque.setConsommation(remorqueEntity.getConsommation());
        remorque.setReservoir(1.0);
        remorque.setCapaciteReservoir(remorqueEntity.getCapaciteReservoir());
        remorque.setCapaciteACharger(remorqueEntity.getCapaciteACharger());
        remorque.setDateDeFinDassurance(remorqueEntity.getDateDeFinDassurance());
        remorque.setNextVisite(remorqueEntity.getNextVisite());
        remorque.setMatricule(remorqueEntity.getMatricule());
        remorque.setCarteGrise(remorqueEntity.getCarteGrise());
        remorqueRepository.save(remorque);
        return true;
    }


    public Boolean deleteRemorque( String matricule)
    {
        if(!remorqueRepository.existsById(matricule))
            return false;
        if(remorqueRepository.getById(matricule).getActive())
            return false;
//        RemorqueEntity car= remorqueRepository.findById(matricule).orElseThrow(
//                ()->new VehiculeNotFound(matricule)
//        );
//        historiqueService.disalowVehicule(car);
        remorqueRepository.deleteById(matricule);
        return true;
    }

    public Boolean addService(String matricule , ServiceEntity service)
    {
        if(!remorqueRepository.existsById(matricule))
            return false;
        RemorqueEntity remorque=remorqueRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        remorque.setService(service);
        if(remorqueRepository.save(remorque) != null)
            return true;
        return false;
    }
}
