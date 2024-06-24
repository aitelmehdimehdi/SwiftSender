package com.PFE.Controller;

import com.PFE.Entity.*;
import com.PFE.Service.EmailSender;
import com.PFE.Service.ManagerService;
import com.PFE.etc.Comparators.ClientComparator;
import com.PFE.etc.Comparators.ConducteurComparator;
import com.PFE.etc.Comparators.ServiceComparator;
import com.PFE.etc.Comparators.VehiculeComparator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequestMapping("manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final EmailSender mailSender;

    @GetMapping("managerCreation/addmanager/add")
    public String addManager(Model model) {
        model.addAttribute("newManager" , new ManagerEntity());
        return "manager/addManagerForm";
    }
    @PostMapping("managerCreation/addmanager/add")
    public String addManager(@ModelAttribute ManagerEntity manager) {
        ManagerEntity manager1=new ManagerEntity(manager.getEmail(),manager.getNom(),
                manager.getPrenom(),manager.getGenre(),manager.getTelephone(),manager.getDateNaissance()
        ,manager.getPassword());
        if(managerService.addManager(manager1) != null)
            return "redirect:/manager";
        else return "redirect:/manager/managerCreation/addmanager/add";
    }

    @GetMapping("")
    public String home(HttpSession session , Model model)
    {
        mailSender.notifyManagerAboutAssurance();
        mailSender.notifyManagerAboutVisite();
        mailSender.notifyManagerAboutCarteGrise();
        if(session.getAttribute("managerLoggedIn") != null) {
            model.addAttribute("nbrServices", managerService.countService());
            model.addAttribute("nbrClients", managerService.countClients());
            model.addAttribute("nbrServiceActive", managerService.countServiceActive());
            model.addAttribute("nbrVehicules", managerService.countVehicules());
            //add sorted method
            List<ServiceEntity> services= managerService.getAllServices();
            Collections.sort(services,new ServiceComparator());
            if (services.size() > 7) {
                List<ServiceEntity> serviceEntities = services.subList(0, 7);
                model.addAttribute("services", serviceEntities);
            } else
                model.addAttribute("services", services);
            return "/manager/managerHomePage";
        }
        else
            return  "redirect:/managerLogIn";
    }

    @GetMapping("service_details/{num_service}")
    public String moreServiceInfo(@PathVariable("num_service") Integer id , Model model)
    {
        List<ServiceHistorique> serviceHistoriques=managerService.getHistoriqueServiceById(id);;
        ClientEntity client=serviceHistoriques.getFirst().getClientEntity();
        ServiceEntity service=serviceHistoriques.getFirst().getServiceEntity();
        List<ConducteurEntity> conducteurs=new ArrayList<ConducteurEntity>();
        List<Vehicule> vehicules=new ArrayList<Vehicule>();
        for(ServiceHistorique historique : serviceHistoriques) {
            conducteurs.add(historique.getConducteurEntity());
            vehicules.add(historique.getVehicule());
        }
        model.addAttribute("service",service);
        model.addAttribute("client",client);
        model.addAttribute("conducteurs",conducteurs);
        model.addAttribute("vehicules",vehicules);
        return "manager/InfoDeService";
    }
    @GetMapping("finService/{num_service}")
    public String finService(@PathVariable("num_service") Integer num_service )
    {
        ServiceEntity service=managerService.getServiceById(num_service);
        managerService.finService(service);
        return "redirect:/manager";
    }
    @GetMapping("client_details/{email}")
    public String moreClientInfo(@PathVariable("email") String email , Model model)
    {
        ClientEntity client=managerService.getClientByEmail(email);
        model.addAttribute("client",client);
        return "manager/PlusD'infoClient";
    }

    @GetMapping("conducteur_details/{email}")
    public String moreCondInfo(@PathVariable("email") String email , Model model)
    {
        ConducteurEntity conducteur=managerService.getConducteurByEmail(email);
        model.addAttribute("conducteur",conducteur);
        return "manager/PlusD'infoConducteur";
    }
    @GetMapping("vehicule_details/{matricule}")
    public String moreVehiculeInfo(@PathVariable("matricule") String matricule , Model model)
    {
        Vehicule vehicule=managerService.getVehiculeByMatricule(matricule);
        model.addAttribute("vehicule",vehicule);
        if(Objects.equals(vehicule.getTypeVehicule(), CamionEntity.class.getSimpleName()))
            return "manager/PlusD'infoCamion";
        if(Objects.equals(vehicule.getTypeVehicule(), RemorqueEntity.class.getSimpleName()))
            return "manager/PlusD'infoRemorque";
        if(Objects.equals(vehicule.getTypeVehicule(), TransportEnCommunEntity.class.getSimpleName()))
            return "manager/PlusD'infoTransit";
        if(Objects.equals(vehicule.getTypeVehicule(), PickUpVoitureEntity.class.getSimpleName()))
            return "manager/PlusD'infoPickUp";
        return "manager/PlusD'infoConducteur";
    }
    @GetMapping("searchService")
    public String searchService(@ModelAttribute ServiceEntity service,Model model , HttpSession session)
    {
        List<ServiceEntity> serviceEntityList=new ArrayList<>();
        System.out.println(service.getTypeVehicule());
        if(service.getNum_service() != null) {
                if(managerService.serviceExistById(service.getNum_service()))
                    serviceEntityList.add(managerService.getServiceById(service.getNum_service()));
        }
        else
        {
            if (Objects.equals(service.getTypeVehicule(), "camion")) {
                serviceEntityList.addAll(managerService.getServiceByTypeVehicule(CamionEntity.class.getSimpleName()));
            } else if (Objects.equals(service.getTypeVehicule(), "remorque")) {
                serviceEntityList.addAll(managerService.getServiceByTypeVehicule(RemorqueEntity.class.getSimpleName()));
            } else if (Objects.equals(service.getTypeVehicule(), "transit")) {
                serviceEntityList.addAll(managerService.getServiceByTypeVehicule(TransportEnCommunEntity.class.getSimpleName()));
            } else if (Objects.equals(service.getTypeVehicule(), "pickup")) {
                serviceEntityList.addAll(managerService.getServiceByTypeVehicule(PickUpVoitureEntity.class.getSimpleName()));
            }
        }
        model.addAttribute("serviceSearched",serviceEntityList);
        return home(session, model);
    }
    @GetMapping("client")
    public String homeClient(Model model)
    {
        List<ClientEntity> clients=managerService.getAllClients();
        Collections.sort(clients,new ClientComparator());
        model.addAttribute("clients",clients);
        model.addAttribute("todayNbrClients",managerService.countTodayClient());
        model.addAttribute("NbrClientsActive",managerService.countClientActive());
        model.addAttribute("thisWeekNbrClients",managerService.countThisWeekClient());
        model.addAttribute("thisMounthNbrClients",managerService.countThisMounthClients());
        return "manager/managerClient";
    }
    @GetMapping("deleteClient/{email}")
    public String deleteClient(@PathVariable("email") String email)
    {
        System.out.println(email);
        managerService.deleteClient(email);
        return "redirect:/manager/client";
        //return true;//managerService.deleteClient(email);
    }

    @GetMapping("conducteur")
    public String homeConducteur(Model model)
    {
        List<ConducteurEntity> conducteurs=managerService.getAllConducteurs();
        Collections.sort(conducteurs,new ConducteurComparator());
        model.addAttribute("conducteurs",conducteurs);
        model.addAttribute("nbrCondDeActive",managerService.countCondActive());
        model.addAttribute("nbrCondDeJour",managerService.countTodayConds());
        model.addAttribute("nbrCondtDeSemaine",managerService.countThisWeekConds());
        model.addAttribute("nbrCondDeMois",managerService.countThisMounthConds());
        ConducteurEntity conducteur=(ConducteurEntity) model.getAttribute("conducteur.email");
        return "manager/managerConducteur";
    }
    @GetMapping("addConducteur")
    public String formConducteur(Model model)
    {
        model.addAttribute("newConducteur",new ConducteurEntity());
        return "manager/managerConducteurForm";
    }
    @PostMapping("addConducteur")
    public String addConducteur(@ModelAttribute ConducteurEntity conducteur)
    {
        ConducteurEntity conducteur1=new ConducteurEntity(conducteur.getEmail(),conducteur.getNom(),conducteur.getPrenom(),
                conducteur.getGenre(),conducteur.getTelephone(),conducteur.getDateNaissance(),conducteur.getPassword(), 5000.0);
        if(managerService.addConducteur(conducteur1) != null)
            return "redirect:/manager/conducteur";
        return "redirect:/manager/addConcudteur";
    }
    @GetMapping("updateConducteur/{email}")
    public String versUpdateConducteur(@PathVariable("email") String email , HttpSession session)
    {
        session.setAttribute("pointedCond",managerService.getConducteurByEmail(email));
        return "redirect:/manager/updateConducteur";
    }
    @GetMapping("deleteConducteur/{email}")
    public String deleteCond(@PathVariable("email") String email)
    {
        System.out.println(email);
        managerService.deleteConducteur(email);
        return "redirect:/manager/conducteur";
        //return true;//managerService.deleteClient(email);
    }
    @GetMapping("updateConducteur")
    public String updateConducteur(HttpSession session,Model model)
    {
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("pointedCond");
        model.addAttribute("conducteur",conducteur);
        model.addAttribute("newConducteur",new ConducteurEntity());
        return "manager/managerConducteurUpdateForm";
    }
    @PostMapping("updateConducteur")
    public String updateConducteur(HttpSession session,Model model,@ModelAttribute ConducteurEntity conducteur)
    {
        ConducteurEntity conducteur1= (ConducteurEntity) session.getAttribute("pointedCond");
        if(managerService.updateConducteur(conducteur1.getEmail(),conducteur)){
            session.setAttribute("pointedCond",managerService.getConducteurByEmail(conducteur1.getEmail()));
        };
        return "redirect:/manager/updateConducteur";
    }

    @GetMapping("vehicule")
    public String homeVehicule(Model model)
    {
        model.addAttribute("nbrCamions",managerService.countCamions());
        model.addAttribute("nbrRemorques",managerService.countRemorques());
        model.addAttribute("nbrTraffics",managerService.countTraffics());
        model.addAttribute("nbrPickUps",managerService.countPickUp());
        List<Vehicule> vehicules=managerService.getAllVehicules();
        Collections.sort(vehicules,new VehiculeComparator());
        model.addAttribute("vehicules",vehicules);
        model.addAttribute("service" , new ServiceEntity());
        return "manager/managerVehicule";
    }
    @GetMapping("addVehicule")
    public String addCar(Model model)
    {
        model.addAttribute("newcar", new CamionEntity() );
        return "manager/managerVehiculeForm";
    }
    @PostMapping("addVehicule")
    public String addVehicule(@ModelAttribute CamionEntity vehicule,Model model)
    {
        Vehicule vehicule1;

        if(Objects.equals(vehicule.getTypeVehicule(), "camion")){
            vehicule1=new CamionEntity(vehicule.getMatricule(),
                    vehicule.getDateDeFinDassurance(),vehicule.getNextVisite(),vehicule.getCarteGrise());
            managerService.addCamion((CamionEntity) vehicule1);
            System.out.println(1);
        }
        else {
            if(Objects.equals(vehicule.getTypeVehicule(), "remorque")){
                vehicule1=new RemorqueEntity(vehicule.getMatricule(),
                        vehicule.getDateDeFinDassurance(),vehicule.getNextVisite(),vehicule.getCarteGrise());
                managerService.addRemorque((RemorqueEntity) vehicule1);
                System.out.println(2);
            }
            else {
                if(Objects.equals(vehicule.getTypeVehicule(), "traffic")){
                    vehicule1=new TransportEnCommunEntity(vehicule.getMatricule(),
                            vehicule.getDateDeFinDassurance(),vehicule.getNextVisite(),vehicule.getCarteGrise());
                    managerService.addTransit((TransportEnCommunEntity) vehicule1);
                    System.out.println(3);
                }
                else {
                    if(Objects.equals(vehicule.getTypeVehicule(), "pickUp")){
                        vehicule1=new PickUpVoitureEntity(vehicule.getMatricule(),
                                vehicule.getDateDeFinDassurance(),vehicule.getNextVisite(),vehicule.getCarteGrise());
                        managerService.addPickUp((PickUpVoitureEntity) vehicule1);
                        System.out.println(4);
                    }
                    else vehicule1=null;
                }
            }
        }
        if(vehicule1!=null)
            model.addAttribute("message","valid");
        else
            model.addAttribute("message","invalid");
            //return "redirect:/manager/addVehicule";
        return "redirect:/manager/vehicule";
    }
    @GetMapping("updateVehicule/{matricule}/{type}")
    public String versUpdateVehicule(@PathVariable("matricule") String matricule,
                                     @PathVariable("type") String type, HttpSession session)
    {
        session.setAttribute("pointedVehicule",managerService.getVehiculeByMatrAndType(matricule,type));
        return "redirect:/manager/updateVehicule";
    }
    @GetMapping("deleteVehicule/{matricule}/{type}")
    public String deleteVehicule(@PathVariable("matricule") String matricule,
                                 @PathVariable("type") String type)
    {
        if(Objects.equals(type, CamionEntity.class.getSimpleName())){
             managerService.deleteCamion(matricule);
        }
        if(Objects.equals(type, PickUpVoitureEntity.class.getSimpleName())){
            managerService.deletePickUp(matricule);
        }
        if(Objects.equals(type, TransportEnCommunEntity.class.getSimpleName())){
            managerService.deleteTransit(matricule);
        }
        if(Objects.equals(type, RemorqueEntity.class.getSimpleName())){
            managerService.deleteRemorque(matricule);
        }
        return "redirect:/manager/vehicule";
    }
    @GetMapping("updateVehicule")
    public String updateVehicule(HttpSession session,Model model)
    {
        Vehicule vehicule= (Vehicule) session.getAttribute("pointedVehicule");
        model.addAttribute("vehicule",vehicule);
        model.addAttribute("newVehicule", new Vehicule());
        return "manager/managerVehiculeUpdateForm";
    }
    @PostMapping("updateVehicule")
    public String updateVehicule(HttpSession session,Model model,@ModelAttribute Vehicule vehicule)
    {
        Vehicule vehicule1= (Vehicule) session.getAttribute("pointedVehicule");

        if(Objects.equals(vehicule1.getTypeVehicule(), CamionEntity.class.getSimpleName())){
            if(managerService.updateCamion(vehicule1.getMatricule(), vehicule)){
                session.setAttribute("pointedVehicule",managerService.getVehiculeByMatrAndType(vehicule.getMatricule(),CamionEntity.class.getSimpleName()));
            };
        }
        if(Objects.equals(vehicule1.getTypeVehicule(), PickUpVoitureEntity.class.getSimpleName())){
            if(managerService.updatePickUp(vehicule1.getMatricule(), vehicule)){
                session.setAttribute("pointedVehicule",managerService.getVehiculeByMatrAndType(vehicule.getMatricule(),PickUpVoitureEntity.class.getSimpleName()));
            };
        }
        if(Objects.equals(vehicule1.getTypeVehicule(), TransportEnCommunEntity.class.getSimpleName())){
            if(managerService.updateTransit(vehicule1.getMatricule(), vehicule)){
                session.setAttribute("pointedVehicule",managerService.getVehiculeByMatrAndType(vehicule.getMatricule(),TransportEnCommunEntity.class.getSimpleName()));
            };
        }
        if(Objects.equals(vehicule1.getTypeVehicule(), RemorqueEntity.class.getSimpleName())){
            if(managerService.updateRemorque(vehicule1.getMatricule(), vehicule)){
                session.setAttribute("pointedVehicule",managerService.getVehiculeByMatrAndType(vehicule.getMatricule(),RemorqueEntity.class.getSimpleName()));
            };
        }

        return "redirect:/manager/updateVehicule";
    }

//    @GetMapping("clientform")
//    public String formClient()
//    {
//        return "manager/managerClientForm";
//    }
//
//    @GetMapping("vehiculeform")
//    public String formVehicule()
//    {
//        return "manager/managerVehiculeForm";
//    }

}
