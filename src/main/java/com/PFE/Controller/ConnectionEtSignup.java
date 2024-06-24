package com.PFE.Controller;

import com.PFE.Entity.ClientEntity;
import com.PFE.Entity.CompteEntity;
import com.PFE.Entity.ConducteurEntity;
import com.PFE.Entity.ManagerEntity;
import com.PFE.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ConnectionEtSignup{
    private final CompteService compteService;
    private final ClientService clientService;
    private final ConducteurService conducteurService;
    private final ManagerService managerService;
    private final EmailSender mailSender;
    private final ServiceService serviceService;

    @GetMapping("")
    public String homepage(HttpSession session)
    {
        mailSender.notifyManagerAboutAssurance();
        mailSender.notifyManagerAboutVisite();
        mailSender.notifyManagerAboutCarteGrise();
        serviceService.checkAllServices();
        session.removeAttribute("managerLoggedIn");
        session.removeAttribute("clientLoggedIn");
        session.removeAttribute("conducteurLoggedIn");
        return "index";
    }

    @GetMapping("/signup")
    public String toSignUpPage(Model model) {
        model.addAttribute("addclient",new ClientEntity());
        return "signup2";
    }

    @PostMapping("/signup")
    public String addClient(@ModelAttribute ClientEntity client,Model model) {
        if(clientService.existsByEmail(client.getEmail()))
        {
            model.addAttribute("message" , "email deja exists");
            return "signup2";
        }
        else {
            //System.out.println(client.getDateNaissance());
            ClientEntity clientEntity = new ClientEntity(client.getEmail(), client.getNom()
                    , client.getPrenom(), client.getGenre(), client.getTelephone(), client.getDateNaissance(), client.getPassword());
            //System.out.println(clientEntity.getDateNaissance());
            clientService.saveClient(clientEntity);
            System.out.println("bien ajouté");
            return "redirect:/login";
        }
    }


    @GetMapping("/login")
    public String toLogInPage(Model model) {
        model.addAttribute("connect" , new CompteEntity());
        return "login2";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute CompteEntity compte,Model model,HttpSession session)
    {
        ClientEntity client=new ClientEntity();
        ConducteurEntity conducteur=new ConducteurEntity();
        if(compteService.existsByEmail(compte.getEmail()))
        {
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            List<CompteEntity> compteEntities = compteService.getAllByEmail(compte.getEmail());
            for(CompteEntity cmp : compteEntities)
            {
                if (passwordEncoder.matches(compte.getPassword(), cmp.getPassword()))
                {
                   if(Objects.equals(cmp.getTypeCompte(), client.getClass().getSimpleName()))
                   {
                        return  clientService.clientlogin(cmp , session);
                   }
                   if(Objects.equals(cmp.getTypeCompte(), conducteur.getClass().getSimpleName()))
                   {
                       return conducteurService.conducteurlogin(cmp ,  session);
                   }
                }
            }
            model.addAttribute("message", "password incorrect");
        }
        else {
            model.addAttribute("message", "compte introuvable , sign up");
        }
        return "login2";
    }

    @GetMapping("/managerLogIn")
    public String toManagerLogInPage(Model model) {
        model.addAttribute("connect" , new CompteEntity());
        return "managerlogIn";
    }
    @PostMapping("/managerLogIn")
    public String Managerlogin(@ModelAttribute CompteEntity compte,Model model,HttpSession session)
    {
        //ClientEntity client=new ClientEntity();
        //ConducteurEntity conducteur=new ConducteurEntity();
        if(compteService.existsByEmail(compte.getEmail()))
        {
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            List<CompteEntity> compteEntities = compteService.getAllByEmail(compte.getEmail());
            for(CompteEntity cmp : compteEntities)
            {
                if (passwordEncoder.matches(compte.getPassword(), cmp.getPassword()))
                {
                    if(Objects.equals(cmp.getTypeCompte(), ManagerEntity.class.getSimpleName()))
                    {
                        return  managerService.managerLogin(cmp , session);
                    }
                }
            }
            model.addAttribute("message", "password incorrect");
        }
        else {
            model.addAttribute("message", "compte introuvable , contactez nous");
        }
        return "managerlogIn";
    }


    @GetMapping("/logOut")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
}
