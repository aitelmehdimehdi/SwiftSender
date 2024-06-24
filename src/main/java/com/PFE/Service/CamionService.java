package com.PFE.Service;

import com.PFE.Assembler.CamionAssembler;
import com.PFE.Entity.CamionEntity;
import com.PFE.Entity.ConducteurEntity;
import com.PFE.Entity.ServiceEntity;
import com.PFE.Entity.Vehicule;
import com.PFE.Exeption.ConducteurNotFound;
import com.PFE.Exeption.VehiculeNotFound;
import com.PFE.Repositry.CamionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class CamionService {
    //private final ServiceHistoriqueService historiqueService;
    private final CamionRepository camionRepository;

    public Integer countCamions()
    {
        return (int) camionRepository.count();
    }


    public List<CamionEntity> getAll()
    {
        return camionRepository.findAll();
    }

    public CamionEntity findByMatricule(String matricule)
    {
        if(camionRepository.existsById(matricule))
            return  camionRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        return null;
    }

    public CamionEntity addCamion(CamionEntity camion)
    {
        return camionRepository.save(camion);
    }


    public Boolean updateCamion(String matricule,CamionEntity camionEntity)
    {
        if(!camionRepository.existsById(matricule))
        {
            return false;
        }
        CamionEntity camion=camionRepository.findById(matricule).orElseThrow();
        camion.setActive(false);
        camion.setConsommation(camionEntity.getConsommation());
        camion.setReservoir(1.0);
        camion.setCapaciteReservoir(camionEntity.getCapaciteReservoir());
        camion.setCapaciteACharger(camionEntity.getCapaciteACharger());
        camion.setDateDeFinDassurance(camionEntity.getDateDeFinDassurance());
        camion.setMatricule(camionEntity.getMatricule());
        camionRepository.save(camion);
        return true;
    }
    public Boolean updateCamion(String matricule, Vehicule camionEntity)
    {
        if(!camionRepository.existsById(matricule))
        {
            return false;
        }
        CamionEntity camion=camionRepository.findById(matricule).orElseThrow();
        camion.setActive(false);
        camion.setConsommation(camionEntity.getConsommation());
        camion.setReservoir(1.0);
        camion.setCapaciteReservoir(camionEntity.getCapaciteReservoir());
        camion.setCapaciteACharger(camionEntity.getCapaciteACharger());
        camion.setDateDeFinDassurance(camionEntity.getDateDeFinDassurance());
        camion.setNextVisite(camionEntity.getNextVisite());
        camion.setCarteGrise(camionEntity.getCarteGrise());
        camionRepository.save(camion);
        return true;
    }


    public Boolean deleteCamion(String matricule)
    {
        if(!camionRepository.existsById(matricule)){
            return false;
        }
        if(camionRepository.getById(matricule).getActive())
            return false;
//        CamionEntity camion=camionRepository.findById(
//                matricule).orElseThrow(() -> new VehiculeNotFound(matricule));
//        historiqueService.disalowVehicule(camion);
        camionRepository.deleteById(matricule);
        return true;
    }

    public Boolean addService(String matricule , ServiceEntity service)
    {
        if(!camionRepository.existsById(matricule))
            return false;
        CamionEntity camion=camionRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        camion.setService(service);
        if(camionRepository.save(camion) != null)
            return true;
        return false;
    }
}
