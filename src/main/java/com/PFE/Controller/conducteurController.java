package com.PFE.Controller;

import com.PFE.Entity.*;
import com.PFE.Service.*;
import com.PFE.etc.PasswordChanging.ChangePassword;
import com.PFE.etc.Comparators.HistoriqueComparator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("conducteur")
@RequiredArgsConstructor
public class conducteurController {
    private final ConducteurService conducteurService;
    private final ServiceService serviceService;
    private final ServiceHistoriqueService historiqueService;
    private final EmailSender mailSender;

    @GetMapping
    public String homePage(HttpSession session, Model model)
    {
        mailSender.notifyManagerAboutAssurance();
        mailSender.notifyManagerAboutVisite();
        mailSender.notifyManagerAboutCarteGrise();
        if(session.getAttribute("conducteurLoggedIn") != null) {
            ConducteurEntity conducteur = (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
            model.addAttribute("nbrServiceTotales", conducteurService.countAllServices(conducteur));
            model.addAttribute("nbrServInVilleParMois", conducteurService.countServiceInVilleParMois(conducteur));
            model.addAttribute("nbrServOutVilleParMois", conducteurService.countServiceOutVilleParMois(conducteur));
            model.addAttribute("salaire", conducteur.getSalary().toString());
            if (conducteurService.getAllServiceInVille(conducteur).size() > 4) {
                List<ServiceHistorique> serviceHistoriqueIn= conducteurService.getAllServiceInVille(conducteur);
                Collections.sort(serviceHistoriqueIn, new HistoriqueComparator());
                model.addAttribute("servicesInVille", serviceHistoriqueIn.subList(0, 4));
            } else
                model.addAttribute("servicesInVille", conducteurService.getAllServiceInVille(conducteur));
            if (conducteurService.getAllServiceOutVille(conducteur).size() > 4) {
                List<ServiceHistorique> serviceHistoriqueOut= conducteurService.getAllServiceOutVille(conducteur);
                Collections.sort(serviceHistoriqueOut, new HistoriqueComparator());
                model.addAttribute("servicesOutVille", serviceHistoriqueOut.subList(0, 4));
            } else
                model.addAttribute("servicesOutVille", conducteurService.getAllServiceOutVille(conducteur));
            if (conducteurService.getAllService(conducteur).size() > 6) {
                List<ServiceHistorique> serviceHistorique= conducteurService.getAllService(conducteur);
                Collections.sort(serviceHistorique, new HistoriqueComparator());
                model.addAttribute("services",serviceHistorique.subList(0, 6));
            } else
                model.addAttribute("services", conducteurService.getAllService(conducteur));
            return "conducteur/Conducteur";
        }
        else
            return "redirect:/login";
    }

    @GetMapping("service")
    public String servicePage(HttpSession session, Model model)
    {
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
        List<ServiceHistorique> conducteurEntities=conducteurService.getAllService(conducteur);
        Collections.sort(conducteurEntities, new HistoriqueComparator());
        model.addAttribute("services",conducteurEntities);
        return "conducteur/ConducteurService";
    }
    @GetMapping("service_details/{num_service}")
    public String moreServiceInformationById(@PathVariable("num_service") Integer id , Model model)
    {
        List<ServiceHistorique> serviceHistoriques=historiqueService.findAllById(id);
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
    public String endServiceById(@PathVariable("num_service") Integer num_service )
    {
        ServiceEntity service=serviceService.findById(num_service);
        serviceService.finService(service);
        return "redirect:/conducteur/service";
    }


    @GetMapping("serviceIn/{id}")
    public String serviceInCity(@PathVariable("id") Integer id, HttpSession session, Model model)
    {
        ServiceEntity service=serviceService.findById(id);
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
        model.addAttribute("services",service);
        return "conducteur/ChoixDansVille";
    }
    @GetMapping("serviceOut/{id}")
    public String serviceOutCity(@PathVariable("id") Integer id,HttpSession session,Model model)
    {
        ServiceEntity service=serviceService.findById(id);
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
        model.addAttribute("services",service);
        return "conducteur/ChoixDansVille";
    }

    @GetMapping("setting")
    public String settingPage(HttpSession session, Model model)
    {
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
        model.addAttribute("cond",conducteur);
        model.addAttribute("changePass",new ChangePassword());
        return "conducteur/ConducteurSetting";
    }
    @PostMapping("setting")
    public String updateSetting(@ModelAttribute ChangePassword changePassword , HttpSession session)
    {
        ConducteurEntity conducteur= (ConducteurEntity) session.getAttribute("conducteurLoggedIn");
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        if(passwordEncoder.matches(changePassword.getPassword(), conducteur.getPassword()))
        {
            if(Objects.equals(changePassword.getNew_pswd(), changePassword.getNew_pswd2()))
            {
                conducteur.setPassword(passwordEncoder.encode(changePassword.getNew_pswd()));
                conducteurService.updateClientPassword(conducteur.getEmail(), conducteur);
            }
        }
        return "redirect:/conducteur/setting";
    }
}
