package com.PFE.Service;

import com.PFE.Entity.*;
import com.PFE.Exeption.ServiceNotFoundException;
import com.PFE.Repositry.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ServiceService {

    private final CamionRepository camionRepository;
    private final CamionService camionService;
    private final RemorqueService remorqueService;
    private final PickUpVoitureService pickUpVoitureService;
    private final PickUpVoitureRepository pickUpVoitureRepository;
    private final TransitRepository transitRepository;
    private final RemorqueRepository remorqueRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;
    private final ConducteurRepository conducteurRepository;
    @Autowired
    private final ServiceHistoriqueRepository serviceHistoriqueRepository;
//    @Autowired
//    private final EmailSender mailService;


    public List<ServiceEntity> getAll()
    {
        return serviceRepository.findAll();
    }

    public List<ServiceEntity> getAllServiceByClient(ClientEntity client){
        return serviceRepository.findAllByClientEntity(client);
    };

    public ServiceEntity findById(Integer id)
    {
        return serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException(id));
    }

    public Integer countServices()
    {
        return (int) serviceRepository.count();
    }

    public ServiceEntity addService(ServiceEntity service)
    {
        ServiceEntity service1= serviceRepository.save(service);
        //service1.getFacture();
        return service1;
    }

    public ServiceEntity addService1(ClientEntity client, String vSource, String vDest
                                  , String typeVehicule , Integer nbrV , LocalDate dateServ)
    {
        List<Vehicule> vehiculeEntities=new LinkedList<>();
        List<ConducteurEntity> lesCondsDispo=conducteurRepository.findAllByActive(false);
        if (Objects.equals(typeVehicule, "camion"))
        {
            vehiculeEntities =camionRepository.findAllByActive(false);
        }
        if (Objects.equals(typeVehicule, "remorque")){
            vehiculeEntities=remorqueRepository.findAllByActive(false);
        }
        if (Objects.equals(typeVehicule, "transportencommun")){
            vehiculeEntities=transitRepository.findAllByActive(false);
        }
        if (Objects.equals(typeVehicule, "pickupvoiture")) {
            vehiculeEntities =pickUpVoitureRepository.findAllByActive(false);
        }
        if(lesCondsDispo.size() >= nbrV && vehiculeEntities.size() >=nbrV)
        {
            client.setActive(true);
            clientRepository.save(client);
            ConducteurEntity[] tabCond = lesCondsDispo.subList(0, nbrV).toArray(new ConducteurEntity[nbrV]);
            List<Vehicule> tabVehicule = vehiculeEntities.subList(0, nbrV);
            ServiceEntity service = new ServiceEntity(dateServ, nbrV, client, tabVehicule, vSource, vDest, tabCond);
            ServiceEntity service1 = addService(service);
            conducteurRepository.saveAll(service1.getConducteur());
//                for (ConducteurEntity conducteurEntity : service.getConducteur()) {
//                    System.out.println(conducteurEntity);
//                    conducteurRepository.save(conducteurEntity);
//                }
            if (Objects.equals(service.getTypeVehicule(), CamionEntity.class.getSimpleName())) {
                for (Vehicule vehicule : service.getVehicule()) {
                    camionService.addService(vehicule.getMatricule(), service1);
                }
            }
            if (Objects.equals(service.getTypeVehicule(), RemorqueEntity.class.getSimpleName())) {
                for (Vehicule vehicule : service.getVehicule()) {
                    remorqueService.addService(vehicule.getMatricule(), service1);
                }
            }
            if (Objects.equals(service.getTypeVehicule(), PickUpVoitureEntity.class.getSimpleName())) {
                for (Vehicule vehicule : service.getVehicule()) {
                    System.out.println(vehicule.getMatricule());
                    pickUpVoitureService.addService(vehicule.getMatricule(), service1);
                }
            }
            if (Objects.equals(service.getTypeVehicule(), TransportEnCommunEntity.class.getSimpleName())) {
                for (Vehicule vehicule : service.getVehicule()) {
                    camionService.addService(vehicule.getMatricule(), service1);
                }
            }
            List<ServiceHistorique> historique=new LinkedList<>();
            List<ConducteurEntity> conducteurEntities=conducteurRepository.findAllByService(service1);
            for(int i=0 ; i<conducteurEntities.size();i++){
                ConducteurEntity conducteur=conducteurEntities.get(i);
                Vehicule vehicule=service1.getVehicule().get(i);
                historique.add(new ServiceHistorique(service1,conducteur
                        , service1.getClientEntity() , vehicule));
            }
            serviceHistoriqueRepository.saveAll(historique);
            //send mail
            return service1;
        }
        return null;
    }

    public ServiceEntity addService2(ClientEntity client , String vsource , String vDest
            , Integer marchandise , LocalDate dateService)
    {
        //List<Vehicule> vehiculeList=new ArrayList<>();
        String typeVehicule;
        Integer nbV;
        if (marchandise<=1000)
        {
            if (marchandise<400){
                float nb= (float) marchandise/400;
                nbV=(int) Math.ceil(nb);
                typeVehicule="pickupvoiture";
            }
            else {
                float nb= (float) marchandise/1000;
                nbV=(int) Math.ceil(nb);
                typeVehicule="transportencommun";
            }
        }
        else {
            if (marchandise<=1700) {
                float nb= (float) marchandise/1700;
                nbV=(int) Math.ceil(nb);
                typeVehicule="camion";
            }
            else {
                float nb= (float) marchandise/4000;
                nbV=(int) Math.ceil(nb);
                typeVehicule="remorque";
            }
        }
        System.out.println("type de vehicule a choisi : "+typeVehicule);
        System.out.println("nombre de vehicule a choisi : "+nbV);
        return addService1(client,vsource,vDest,typeVehicule,nbV,dateService);
    }

    public void finService(ServiceEntity service) {
        //List<ConducteurEntity> lesConducteurs = service.getConducteur();
        //List<Vehicule> lesVehicules = service.getVehicule();
        //ClientEntity client = service.getClientEntity();
        for (ConducteurEntity conducteur : service.getConducteur()) {
            conducteur.setActive(false);
            conducteur.setService(null);
            conducteur.setVehicule(null);
            conducteurRepository.save(conducteur);
        }
        try{
            String type = service.getVehicule().getFirst().getClass().getSimpleName();
            for (Vehicule vehicule : service.getVehicule()) {
                vehicule.setActive(false);
                vehicule.setService(null);
                vehicule.setConducteur(null);
                if (type.equals(CamionEntity.class.getSimpleName()))
                    camionRepository.save((CamionEntity) vehicule);
                if (type.equals(RemorqueEntity.class.getSimpleName()))
                    remorqueRepository.save((RemorqueEntity) vehicule);
                if (type.equals(PickUpVoitureEntity.class.getSimpleName()))
                    pickUpVoitureRepository.save((PickUpVoitureEntity) vehicule);
                if (type.equals(TransportEnCommunEntity.class.getSimpleName()))
                    transitRepository.save((TransportEnCommunEntity) vehicule);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        service.getClientEntity().getListServices().remove(service);
        service.getClientEntity().setActive(false);
        clientRepository.save(service.getClientEntity());
        service.setActive(false);
        serviceRepository.save(service);
    }

    public void checkAllServices(){
        List<ServiceEntity> entityList=serviceRepository.findAll();
        for(ServiceEntity service:entityList){
            if(service.getDateService().compareTo(LocalDate.now())<0)
                finService(service);
        }
    }

    public Boolean updateService(Integer id , ServiceEntity service)
    {
        if(!serviceRepository.existsById(id))
        {
            return false;
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("pas de service d'id "+id);
        }

        ServiceEntity serviceEntity=serviceRepository.findById(id).orElseThrow();
        serviceEntity.setClientEntity(service.getClientEntity());
        serviceEntity.setPrice(service.getPrice());
        serviceEntity.setVehicule(service.getVehicule());
        serviceEntity.setNombreVehicule(service.getNombreVehicule());
        serviceEntity.setDansLaVille(service.getDansLaVille());
         serviceRepository.save(serviceEntity);
        return true;
    }


    public Boolean deleteService( Integer id)
    {
        if(!serviceRepository.existsById(id))
        {
            return false;
        }
        serviceRepository.deleteById(id);
        return true;
    }
    public Boolean deleteServiceByClient(ClientEntity client)
    {
        try {
            List<ServiceEntity> serviceEntities = serviceRepository.findAllByClientEntity(client);
            for (ServiceEntity service : serviceEntities) {
                serviceRepository.deleteById(service.getNum_service());
            }
            return true;
        }catch (SqlScriptException sqlScriptException)
        {
            System.err.println(sqlScriptException.getMessage());
            System.exit(0);
            return false;
        }
    }

    public Boolean existById(Integer id)
    {
        return serviceRepository.existsById(id);
    }


    public List<ServiceEntity> allServiceActive()
    {
        return serviceRepository.getAllByActive(true);
    }

    public List<ServiceEntity> getServiceByTypeVehicule(String type)
    {
        return serviceRepository.findAllByTypeVehicule(type);
    }

    public Integer countByActive()
    {
        return serviceRepository.countByActive(true);
    }

    public Integer countServiceByConducteur(ConducteurEntity conducteur)
    {
        return serviceRepository.countByConducteur(conducteur);
    }
    public Integer countServiceByConducteurAndActive(ConducteurEntity conducteur)
    {
        return serviceRepository.countByConducteurAndActive(conducteur,true);
    }

    public Integer countServInOrOutVilleParDate(ConducteurEntity conducteur,Boolean var , LocalDate date)
    {
        return serviceRepository.countByConducteurEqualsAndDansLaVilleEqualsAndDateServiceAfter(conducteur,var,date);
    }

    public List<ServiceEntity> getAllInVilleByConducteur(ConducteurEntity conducteur)
    {
        return serviceRepository.findAllByConducteurAndDansLaVille(conducteur,true);
    }
    public List<ServiceEntity> getAllOutVilleByConducteur(ConducteurEntity conducteur)
    {
        return serviceRepository.findAllByConducteurAndDansLaVille(conducteur,false);
    }

    public List<ServiceEntity> getAllByConducteur(ConducteurEntity conducteur)
    {
        return serviceRepository.findAllByConducteur(conducteur);
    }

    public Boolean disalowClient(ClientEntity client)
    {
        List<ServiceEntity> entities=serviceRepository.findAllByClientEntity(client);
        System.out.println("Service service : "+client);
        Integer i=0;
        for (ServiceEntity service : entities) {
            i=0;
            service.setClientEntity(null);
            serviceRepository.save(service);
            i=1;
        }
        if (i==1) return true;
        else return false;
    }
}
