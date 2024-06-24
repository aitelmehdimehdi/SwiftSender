package com.PFE.Service;

import com.PFE.Entity.*;
import com.PFE.Exeption.VehiculeNotFound;
import com.PFE.Repositry.TransitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransitService {
    //private final ServiceHistoriqueService historiqueService;
    private final TransitRepository transitRepository;

    public Integer countTransit()
    {
        return (int) transitRepository.count();
    }


    public List<TransportEnCommunEntity> getAll()
    {
        return transitRepository.findAll();
    }




    public TransportEnCommunEntity findByMatricule(String matricule)
    {
        if(transitRepository.existsById(matricule))
            return transitRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        return null;
    }


    public TransportEnCommunEntity addTransit( TransportEnCommunEntity transit)
    {
        return transitRepository.save(transit);
    }


    public Boolean updateTransit(String matricule , TransportEnCommunEntity transit)
    {
        if(!transitRepository.existsById(matricule))
            return false;
        TransportEnCommunEntity transit1=transitRepository.findById(matricule).orElseThrow();
        transit1.setActive(false);
        transit1.setConsommation(transit.getConsommation());
        transit1.setReservoir(1.0);
        transit1.setCapaciteReservoir(transit.getCapaciteReservoir());
        transit1.setCapaciteACharger(transit.getCapaciteACharger());
        transit1.setDateDeFinDassurance(transit.getDateDeFinDassurance());
        transit1.setMatricule(transit.getMatricule());
        transitRepository.save(transit1);
        return true;
    }
    public Boolean updateTransit(String matricule , Vehicule transit)
    {
        if(!transitRepository.existsById(matricule))
            return false;
        TransportEnCommunEntity transit1=transitRepository.findById(matricule).orElseThrow();
        transit1.setActive(false);
        transit1.setConsommation(transit.getConsommation());
        transit1.setReservoir(1.0);
        transit1.setCapaciteReservoir(transit.getCapaciteReservoir());
        transit1.setCapaciteACharger(transit.getCapaciteACharger());
        transit1.setDateDeFinDassurance(transit.getDateDeFinDassurance());
        transit1.setNextVisite(transit.getNextVisite());
        transit1.setMatricule(transit.getMatricule());
        transit1.setCarteGrise(transit.getCarteGrise());
        transitRepository.save(transit1);
        return true;
    }


    public Boolean deleteTransit( String matricule)
    {
        if(!transitRepository.existsById(matricule))
            return false;
        if(transitRepository.getReferenceById(matricule).getActive())
            return false;
//        TransportEnCommunEntity car= transitRepository.findById(matricule).orElseThrow(
//                ()->new VehiculeNotFound(matricule)
//        );
//        historiqueService.disalowVehicule(car);
        transitRepository.deleteById(matricule);
        return true;
    }

    public Boolean addService(String matricule , ServiceEntity service)
    {
        if(!transitRepository.existsById(matricule))
            return false;
        TransportEnCommunEntity transit=transitRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        transit.setService(service);
        transitRepository.save(transit);
        return true;
    }


}
