package com.PFE.Service;

import com.PFE.Entity.*;
import com.PFE.Exeption.ClientNotFound;
import com.PFE.Repositry.ClientRepository;
import com.PFE.Repositry.CompteRepository;
import com.PFE.Repositry.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Controller
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final ConducteurService conducteurService;
    private final TransitService transitService;
    private final CamionService camionService;
    private final PickUpVoitureService pickUpVoitureService;
    private final RemorqueService remorqueService;
    private final CompteRepository compteRepository;
    private final ServiceService serviceService;
    private final CompteService compteService;
    private final ServiceHistoriqueService historiqueService;
    //private final EmailSender mailSenderService;

    public String managerLogin(CompteEntity compte , HttpSession session)
    {
        session.setAttribute("managerLoggedIn",managerRepository.findById(compte.getEmail())
                .orElseThrow(() -> new ClientNotFound("Manager not found")));
        return "redirect:/manager";
    }

    public ManagerEntity getById(String email)
    {
        if(!managerRepository.existsById(email))
            return null;

        return managerRepository.findById(email).orElseThrow();
    }

    public  Boolean updateManager(String email , ManagerEntity manager)
    {
        if(!managerRepository.existsById(email))
        {
            return false;
        }
        ManagerEntity manager1 = managerRepository.findById(email).orElseThrow();
        manager1.setNom(manager.getNom());
        manager1.setPrenom(manager.getPrenom());
        manager1.setTelephone(manager.getTelephone());
        manager1.setDateNaissance(manager.getDateNaissance());
        managerRepository.save(manager1);
        return true;
    }




    public Boolean updateManagerPassword(String email , ManagerEntity manager)
    {
        if(!managerRepository.existsById(email))
        {
            return false;
        }
        else {
            ManagerEntity manager1 = managerRepository.findById(email).orElseThrow();
            manager1.setPassword(manager.getPassword());
            for(CompteEntity compte : compteRepository.findAllByEmail(email))
            {
                if(Objects.equals(compte.getTypeCompte(), ManagerEntity.class.getSimpleName())) {
                    compte.setPassword(manager.getPassword());
                    compteRepository.save(compte);
                }
            }
            managerRepository.save(manager1);
            return true;
        }
    }




    public List<ClientEntity> getAllClients()
    {
        return clientRepository.findAll();
    }

    public  ClientEntity getClientByEmail(String email)
    {
        return  clientRepository.findById(email).orElseThrow();
    }

    @Transactional
    public Boolean deleteClient(String email)
    {
        if(!clientRepository.existsById(email))
            return false;
        if(clientRepository.getById(email).getActive())
            return false;
        clientRepository.deleteById(email);
        return true;
    }




    public List<ConducteurEntity> getAllConducteurs()
    {
        return conducteurService.getAll();
    }

    public ConducteurEntity getConducteurByEmail(String email)
    {
        return conducteurService.getByEmail(email);
    }

    public ConducteurEntity addConducteur(ConducteurEntity conducteur)
    {
        return conducteurService.addConducteur(conducteur);
    }

    public Boolean updateConducteur(String email, ConducteurEntity conducteur)
    {
        return conducteurService.editConducteur(email,conducteur);
    }

    @Transactional
    public Boolean deleteConducteur(String email){
        return conducteurService.deleteByEmail(email);
    }



    public List<CamionEntity> getAllCamions()
    {
        return camionService.getAll();
    }
    public CamionEntity getCamion(String matricule)
    {
        return camionService.findByMatricule(matricule);
    }
    public CamionEntity addCamion(CamionEntity camion)
    {
        return camionService.addCamion(camion);
    }
    public  Boolean updateCamion(String matricule,CamionEntity camion)
    {
        return camionService.updateCamion(matricule,camion);
    }
    public  Boolean updateCamion(String matricule,Vehicule camion)
    {
        return camionService.updateCamion(matricule,camion);
    }
    @Transactional
    public Boolean deleteCamion(String matricule)
    {
        return camionService.deleteCamion(matricule);
    }


    public void finService(ServiceEntity service)
    {
        serviceService.finService(service);
    }


    public List<RemorqueEntity> getAllRemorque()
    {
        return remorqueService.getAll();
    }
    public RemorqueEntity getRemorque(String matricule)
    {
        return remorqueService.findByMatricule(matricule);
    }
    public RemorqueEntity addRemorque(RemorqueEntity camion)
    {
        return remorqueService.addRemorque(camion);
    }
    public  Boolean updateRemorque(String matricule,RemorqueEntity camion)
    {
        return remorqueService.updateRemorque(matricule,camion);
    }
    public  Boolean updateRemorque(String matricule,Vehicule camion)
    {
        return remorqueService.updateRemorque(matricule,camion);
    }
    @Transactional
    public Boolean deleteRemorque(String matricule)
    {
        return remorqueService.deleteRemorque(matricule);
    }




    public List<TransportEnCommunEntity> getAllTransits()
    {
        return transitService.getAll();
    }
    public TransportEnCommunEntity getTransit(String matricule)
    {
        return transitService.findByMatricule(matricule);
    }
    public TransportEnCommunEntity addTransit(TransportEnCommunEntity camion)
    {
        return transitService.addTransit(camion);
    }
    public  Boolean updateTransit(String matricule,TransportEnCommunEntity camion)
    {
        return transitService.updateTransit(matricule,camion);
    }
    public  Boolean updateTransit(String matricule,Vehicule camion)
    {
        return transitService.updateTransit(matricule,camion);
    }
    @Transactional
    public Boolean deleteTransit(String matricule)
    {
        return transitService.deleteTransit(matricule);
    }




    public List<PickUpVoitureEntity> getAllpickUps()
    {
        return pickUpVoitureService.getAll();
    }
    public PickUpVoitureEntity getPickUp(String matricule)
    {
        return pickUpVoitureService.findByMatricule(matricule);
    }
    public PickUpVoitureEntity addPickUp(PickUpVoitureEntity camion)
    {
        return pickUpVoitureService.addPickUpVoiture(camion);
    }
    public  Boolean updatePickUp(String matricule,PickUpVoitureEntity camion)
    {
        return pickUpVoitureService.updatePickUpVoiture(matricule,camion);
    }
    public  Boolean updatePickUp(String matricule,Vehicule camion)
    {
        return pickUpVoitureService.updatePickUpVoiture(matricule,camion);
    }
    @Transactional
    public Boolean deletePickUp(String matricule)
    {
        return pickUpVoitureService.deletePickUp(matricule);
    }


    public List<ServiceEntity> getAllServices()
    {
        return serviceService.getAll();
    }

    public ServiceEntity getServiceById(Integer id)
    {
        return serviceService.findById(id);
    }
    public Boolean serviceExistById(Integer id)
    {
        return serviceService.existById(id);
    }
    public List<ServiceHistorique> getHistoriqueServiceById(Integer id)
    {
        return historiqueService.findAllById(id);
    }
    public List<ServiceEntity> getServiceByTypeVehicule(String type)
    {
        return serviceService.getServiceByTypeVehicule(type);
    }

    public Integer countVehicules()
    {
        return camionService.countCamions()+remorqueService.countRemorques()
                +pickUpVoitureService.countPickUps()+transitService.countTransit();
    }

    public Integer countCamions()
    {
        return camionService.countCamions();
    }
    public Integer countRemorques()
    {
        return remorqueService.countRemorques();
    }
    public Integer countTraffics()
    {
        return transitService.countTransit();
    }
    public Integer countPickUp()
    {
        return pickUpVoitureService.countPickUps();
    }
    public List<Vehicule> getAllVehicules()
    {
        List<Vehicule> vehicules = new LinkedList<Vehicule>();
        vehicules.addAll(this.getAllRemorque());
        vehicules.addAll(this.getAllCamions());
        vehicules.addAll(this.getAllTransits());
        vehicules.addAll(this.getAllpickUps());
        return vehicules;
    }

    public Integer countClients()
    {
        return (int)clientRepository.count();
    }

    public Integer countConducteurs()
    {
        return conducteurService.countConducteurs();
    }

    public Integer countService()
    {
        return serviceService.countServices();
    }
    public Integer countServiceActive(){return serviceService.countByActive();}
    public Integer countTodayClient()
    {
        return compteService.countCompteByDAte(LocalDate.now(),ClientEntity.class.getSimpleName());
    }

    public Integer countThisWeekClient()
    {
        return compteService.countCompteByTimeAfter(LocalDate.now().minusWeeks(1) , ClientEntity.class.getSimpleName());
    }

    public Integer countThisMounthClients()
    {
        return compteService.countCompteByTimeAfter(LocalDate.now().minusMonths(1) , ClientEntity.class.getSimpleName());
    }

    public Integer countClientActive()
    {
        Integer count=0;
        List<ServiceEntity> serviceEntities=serviceService.allServiceActive();
        List<ClientEntity> clients=new ArrayList<ClientEntity>();
        for(ServiceEntity service : serviceEntities)
        {
            if (clients.contains(service.getClientEntity())){}
            else {
                count++;
                clients.add(service.getClientEntity());
            }
        }
        return count;
    }


    public Integer countTodayConds()
    {
        return compteService.countCompteByDAte(LocalDate.now(),ConducteurEntity.class.getSimpleName());
    }

    public Integer countThisWeekConds()
    {
        return compteService.countCompteByTimeAfter(LocalDate.now().minusWeeks(1) , ConducteurEntity.class.getSimpleName());
    }

    public Integer countThisMounthConds()
    {
        return compteService.countCompteByTimeAfter(LocalDate.now().minusMonths(1) , ConducteurEntity.class.getSimpleName());
    }

    public Integer countCondActive()
    {
        Integer count=0;
        List<ServiceEntity> serviceEntities=serviceService.allServiceActive();
        List<ConducteurEntity> conds=new ArrayList<ConducteurEntity>();
        for(ServiceEntity service : serviceEntities)
        {
            for(ConducteurEntity conducteur : service.getConducteur() ) {
                if (conds.contains(conducteur)) {
                }
                else {
                    count++;
                    conds.add(conducteur);
                }
            }
        }
        return count;
    }
    public Vehicule getVehiculeByMatricule(String mat){
        if(camionService.findByMatricule(mat)!=null){
            return camionService.findByMatricule(mat);
        }
        if(pickUpVoitureService.findByMatricule(mat)!=null){
            return pickUpVoitureService.findByMatricule(mat);
        }
        if(transitService.findByMatricule(mat)!=null){
            return transitService.findByMatricule(mat);
        }
        if(remorqueService.findByMatricule(mat)!=null){
            return remorqueService.findByMatricule(mat);
        }
        return null;
    }
    public Vehicule getVehiculeByMatrAndType(String mat , String type){
        if(Objects.equals(type, CamionEntity.class.getSimpleName())){
            return camionService.findByMatricule(mat);
        }
        if(Objects.equals(type, PickUpVoitureEntity.class.getSimpleName())){
            return pickUpVoitureService.findByMatricule(mat);
        }
        if(Objects.equals(type, TransportEnCommunEntity.class.getSimpleName())){
            return transitService.findByMatricule(mat);
        }
        if(Objects.equals(type, RemorqueEntity.class.getSimpleName())){
            return remorqueService.findByMatricule(mat);
        }
        return null;
    }


    public ManagerEntity addManager(ManagerEntity manager1) {
        ManagerEntity manager=managerRepository.save(manager1) ;
        if (manager1.getCompte(manager1.getType()) != null)
            compteService.createCompte(manager1.getCompte(manager1.getType()));
        return manager;
    }

    public List<ManagerEntity> getAllManagers()
    {
        return managerRepository.findAll();
    }

    public void updateLastMailSent(String email, LocalDate lastMailSent) {
        if (managerRepository.existsById(email))
        {
            ManagerEntity manager=managerRepository.findById(email).orElseThrow(
                    () -> new ClientNotFound(email)
            );
            manager.setLastMailSent(lastMailSent);
            managerRepository.save(manager);
        }
    }
//    public void checkAssurance(){
//        mailSenderService.notifyManagerAboutAssurance();
//    }
//    public void checkVisite(){
//        mailSenderService.notifyManagerAboutVisite();
//    }
//    public void checkCarteGrise(){
//        mailSenderService.notifyManagerAboutCarteGrise();
//    }
}
