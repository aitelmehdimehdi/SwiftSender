package com.PFE.Service;

import com.PFE.Assembler.PickUpVoitureAssembler;
import com.PFE.Entity.CamionEntity;
import com.PFE.Entity.PickUpVoitureEntity;
import com.PFE.Entity.ServiceEntity;
import com.PFE.Entity.Vehicule;
import com.PFE.Exeption.VehiculeNotFound;
import com.PFE.Repositry.PickUpVoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Service
@RequiredArgsConstructor
public class PickUpVoitureService {
        //private final ServiceHistoriqueService historiqueService;
        private final PickUpVoitureRepository pickupvoitureRepository;


        public Integer countPickUps()
        {
            return (int) pickupvoitureRepository.count();
        }


        public List<PickUpVoitureEntity> getAll()
        {
            return pickupvoitureRepository.findAll();
        }


        public PickUpVoitureEntity findByMatricule( String matricule)
        {
            if(!pickupvoitureRepository.existsById(matricule))
                return null;
            return pickupvoitureRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        }


        public PickUpVoitureEntity addPickUpVoiture( PickUpVoitureEntity pickupvoiture)
        {
            return pickupvoitureRepository.save(pickupvoiture);
        }


        public Boolean updatePickUpVoiture(String matricule,PickUpVoitureEntity pickupvoitureEntity)
        {
            if(!pickupvoitureRepository.existsById(matricule))
                return false;
            PickUpVoitureEntity pickupvoiture=pickupvoitureRepository.findById(matricule).orElseThrow();
            pickupvoiture.setActive(false);
            pickupvoiture.setConsommation(pickupvoitureEntity.getConsommation());
            pickupvoiture.setReservoir(1.0);
            pickupvoiture.setCapaciteReservoir(pickupvoitureEntity.getCapaciteReservoir());
            pickupvoiture.setCapaciteACharger(pickupvoitureEntity.getCapaciteACharger());
            pickupvoiture.setDateDeFinDassurance(pickupvoitureEntity.getDateDeFinDassurance());
            pickupvoiture.setMatricule(pickupvoitureEntity.getMatricule());
            pickupvoitureRepository.save(pickupvoiture);
            return true;
        }
    public Boolean updatePickUpVoiture(String matricule, Vehicule pickupvoitureEntity)
    {
        if(!pickupvoitureRepository.existsById(matricule))
            return false;
        PickUpVoitureEntity pickupvoiture=pickupvoitureRepository.findById(matricule).orElseThrow();
        pickupvoiture.setActive(false);
        pickupvoiture.setConsommation(pickupvoitureEntity.getConsommation());
        pickupvoiture.setReservoir(1.0);
        pickupvoiture.setCapaciteReservoir(pickupvoitureEntity.getCapaciteReservoir());
        pickupvoiture.setCapaciteACharger(pickupvoitureEntity.getCapaciteACharger());
        pickupvoiture.setDateDeFinDassurance(pickupvoitureEntity.getDateDeFinDassurance());
        pickupvoiture.setNextVisite(pickupvoitureEntity.getNextVisite());
        pickupvoiture.setMatricule(pickupvoitureEntity.getMatricule());
        pickupvoiture.setCarteGrise(pickupvoitureEntity.getCarteGrise());
        pickupvoitureRepository.save(pickupvoiture);
        return true;
    }


    public Boolean deletePickUp(String matricule)
    {
        if(!pickupvoitureRepository.existsById(matricule))
            return false;
        if(pickupvoitureRepository.getById(matricule).getActive())
            return false;
//        PickUpVoitureEntity pick= pickupvoitureRepository.findById(matricule).orElseThrow(
//                ()->new VehiculeNotFound(matricule)
//        );
//        historiqueService.disalowVehicule(pick);
        pickupvoitureRepository.deleteById(matricule);
        return true;
    }

    public Boolean addService(String matricule , ServiceEntity service)
    {
        if(!pickupvoitureRepository.existsById(matricule))
            return false;
        PickUpVoitureEntity pick=pickupvoitureRepository.findById(matricule).orElseThrow(()->new VehiculeNotFound(matricule));
        pick.setService(service);
        if(pickupvoitureRepository.save(pick) != null)
            return true;
        return false;
    }

}
