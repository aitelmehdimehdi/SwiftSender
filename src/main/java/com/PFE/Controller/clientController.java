package com.PFE.Controller;

import com.PFE.Entity.*;
import com.PFE.Service.*;
import com.PFE.etc.PasswordChanging.ChangePassword;
import com.PFE.etc.Comparators.ServiceComparator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("client")
@RequiredArgsConstructor
public class clientController {
    private final ClientService clientService;
    private final ServiceService serviceService;
    //private final ManagerService managerService;
    private final EmailSender mailSender;
    private final ServiceHistoriqueService historiqueService;




    @GetMapping
    public String clientHomePage(Model model,HttpSession session)
    {
        mailSender.notifyManagerAboutAssurance();
        mailSender.notifyManagerAboutVisite();
        mailSender.notifyManagerAboutCarteGrise();
        if(session.getAttribute("clientLoggedIn") != null)
        {
            ClientEntity client =(ClientEntity)session.getAttribute("clientLoggedIn");
            model.addAttribute("clientInfos" , client);
            return "client/clientHomePage";
        }
        else
            return "redirect:/login";

    }
    @GetMapping("service")
    public String servicePage()
    {
        return "client/clientsService";
    }

    @GetMapping("service1")
    public String pageService1(Model model )
    {
        //ClientEntity client = (ClientEntity) session.getAttribute("clientLoggedIn");
        model.addAttribute("service_request",new ServiceEntity());
        return "client/clientServiceParChoisis";
    }

    @PostMapping("service1")
    public String sendService1(@ModelAttribute ServiceEntity service ,Model model, HttpSession session)
    {
        ClientEntity client = (ClientEntity) session.getAttribute("clientLoggedIn");
        if(serviceService.addService1(client,service.getVilleSource(),
                service.getVilleDest(),service.getTypeVehicule(),
                service.getNombreVehicule(),service.getDateService())!=null)
        {
            mailSender.notifyClient(client.getEmail(),service);
            model.addAttribute("svalid","bien fait");
            return "client/clientServiceParChoisis";
        }
        model.addAttribute("sinvalid","mauvais fait");
        return "client/clientServiceParChoisis";
    }

    @GetMapping("service2")
    public String pageService2(Model model )
    {
        model.addAttribute("service_request",new ServiceEntity());
        return "client/clientServiceParDesc";
    }
    @PostMapping("service2")
    public String sendService2(@ModelAttribute ServiceEntity service ,Model model, HttpSession session)
    {
        ClientEntity client = (ClientEntity) session.getAttribute("clientLoggedIn");
        if(serviceService.addService2(client,service.getVilleSource(),
                service.getVilleDest(),service.getMarchandise(),service.getDateService() )!=null)
        {
            model.addAttribute("svalid","bien fait");
            return "client/clientServiceParDesc";
        }
        model.addAttribute("sinvalid","mauvais fait");
        return "client/clientServiceParDesc";
    }

    @GetMapping("profil")
    public String profilePage(Model model , HttpSession session)
    {
        ClientEntity client = (ClientEntity) session.getAttribute("clientLoggedIn");
        model.addAttribute("clientInfos" , client);
        return "client/clientSetting";
    }

    @GetMapping("setting")
    public String settingPage(HttpSession session,Model model)
    {
        ClientEntity client=(ClientEntity) session.getAttribute("clientLoggedIn");
        model.addAttribute("clientInfos",client);
        model.addAttribute("newClient",new ClientEntity());
        return "client/ClientsSettingUpdate";
    }

    @PostMapping("setting")
    public String updateSetting(@ModelAttribute ClientEntity client1,Model model,HttpSession session)
    {
        ClientEntity client=(ClientEntity) session.getAttribute("clientLoggedIn");
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        if(passwordEncoder.matches(client1.getPassword(), client.getPassword()))
        {
            if(clientService.updateClient(client.getEmail() , client1 , session))
            {
                model.addAttribute("mvalid","Modifications bien fait");
                ClientEntity clientEntity= (ClientEntity) session.getAttribute("clientLoggedIn");
                model.addAttribute("clientInfos",clientService.findById(clientEntity.getEmail()));
                System.out.println("bient modifié");
                return "client/ClientsSettingUpdate";
            }
        }
        model.addAttribute("clientInfos",client);
        model.addAttribute("minvalid","Erreur lors de la modification , ressayer");
        return "client/ClientsSettingUpdate";

    }

    @GetMapping("mot_de_passe")
    public String pagePassword(Model model)
    {
        model.addAttribute("changePass",new ChangePassword());
        return "client/clientChangePassword";
    }
    @PostMapping("mot_de_passe")
    public String updatePassword(@ModelAttribute ChangePassword changePassword,HttpSession session, Model model)
    {
        ClientEntity client= (ClientEntity) session.getAttribute("clientLoggedIn");
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        if(passwordEncoder.matches(changePassword.getPassword(), client.getPassword()))
        {
            if(Objects.equals(changePassword.getNew_pswd(), changePassword.getNew_pswd2()))
            {
                client.setPassword(passwordEncoder.encode(changePassword.getNew_pswd()));
                if(clientService.updateClientPassword(client.getEmail(),client)) {
                    model.addAttribute("mvalid","mot de passe bien modifié");
                }
                else
                    model.addAttribute("minvalid","erreur inconnu please ressayer");
            }
            else {
                model.addAttribute("minvalid","if faut bien re ecrire le nouveau mot de passe ");
            }
        }
        else{
            model.addAttribute("minvalid","password incorrect ");
        }
        return "client/clientChangePassword";
    }
    @GetMapping("allService")
    public String getTousServices(HttpSession session,Model model)
    {
        ClientEntity client= (ClientEntity) session.getAttribute("clientLoggedIn");
        List<ServiceEntity> services= serviceService.getAllServiceByClient(client);
        services.sort(new ServiceComparator());
        model.addAttribute("services",services);
        return "client/clientAllServices";
    }
    @GetMapping("service_details/{num_service}")
    public String getServiceSpecifique(@PathVariable("num_service") Integer id , Model model)
    {
        List<ServiceHistorique> serviceHistoriques=historiqueService.findAllById(id);
        ClientEntity client=serviceHistoriques.getFirst().getClientEntity();
        ServiceEntity service=serviceHistoriques.getFirst().getServiceEntity();
        List<ConducteurEntity> conducteurs=new ArrayList<>();
        List<Vehicule> vehicules=new ArrayList<>();
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
}
