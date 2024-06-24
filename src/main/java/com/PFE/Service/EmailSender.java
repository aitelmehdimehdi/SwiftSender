package com.PFE.Service;

import com.PFE.Entity.ManagerEntity;
import com.PFE.Entity.ServiceEntity;
import com.PFE.Entity.Vehicule;
import com.PFE.Repositry.ServiceHistoriqueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmailSender {
    @Autowired
    private final ManagerService managerService;

    private final JavaMailSender emailSender;
    @Autowired
    private ServiceHistoriqueRepository serviceHistoriqueRepository;

    public EmailSender(ManagerService managerService, JavaMailSender emailSender) {
        this.managerService = managerService;
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void notifyManagerAboutAssurance() {
        List<Vehicule> vehicules=managerService.getAllVehicules();
        String message = null;
        for(Vehicule vehicule:vehicules){
            if(LocalDate.now().plusDays(5).compareTo(vehicule.getDateDeFinDassurance())>0){
                message="vous etes besoin de regler votre assurance de la vehicule de type : "+vehicule.getTypeVehicule()+
                        " et de matricule : "+vehicule.getMatricule()+" avant le "+vehicule.getDateDeFinDassurance();
            }
            if(message!=null){
                for (ManagerEntity manager : managerService.getAllManagers()) {
                    if (manager.getLastMailSent() == null) {
                        this.sendEmail(manager.getEmail(), "SwiftSender : Alert Pour regler " +
                                "les papiers d'une vehicule ", message);
                        manager.setLastMailSent(LocalDate.now());
                        managerService.updateLastMailSent(manager.getEmail(), manager.getLastMailSent());
                    } else if (LocalDate.now().compareTo(manager.getLastMailSent()) > 0) {
                        this.sendEmail(manager.getEmail(), "SwiftSender : Alert Pour regler les" +
                                "papiers d'une vehicule ", message);
                        manager.setLastMailSent(LocalDate.now());
                        managerService.updateLastMailSent(manager.getEmail(), manager.getLastMailSent());
                    }
                }
                message=null;
            }
        }
    }

    public void notifyManagerAboutCarteGrise()
    {
        List<Vehicule> vehicules=managerService.getAllVehicules();
        String message = new String();
        for(Vehicule vehicule:vehicules){
            if (LocalDate.now().plusDays(5).compareTo(vehicule.getCarteGrise())>0)
                message="vous etes besoin de regler votre carte grise de la vehicule de type : "+vehicule.getTypeVehicule()+
                        " et de matricule : "+vehicule.getMatricule()+" avant le "+vehicule.getCarteGrise();
            for (ManagerEntity manager:managerService.getAllManagers())
            {
                if(manager.getLastMailSent()==null)
                {
                    this.sendEmail(manager.getEmail(), "SwiftSender : Alert Pour regler " +
                            "les papiers d'une vehicule ", message);
                    manager.setLastMailSent(LocalDate.now());
                    managerService.updateLastMailSent(manager.getEmail() , manager.getLastMailSent());
                }
                else if (LocalDate.now().compareTo(manager.getLastMailSent())>0) {
                    this.sendEmail(manager.getEmail(), " SwiftSender : Alert Pour regler " +
                            "les papiers d'une vehicule ", message);
                    manager.setLastMailSent(LocalDate.now());
                    managerService.updateLastMailSent(manager.getEmail() , manager.getLastMailSent());
                }
            }
        }
    }



    public void notifyManagerAboutVisite()
    {
        List<Vehicule> vehicules=managerService.getAllVehicules();
        String message = new String();
        for(Vehicule vehicule:vehicules){
            if (LocalDate.now().plusDays(5).compareTo(vehicule.getNextVisite())>0)
            {
                message="vous etes besoin de regler votre visite de la vehicule de type : "+vehicule.getTypeVehicule()+
                        " et de matricule : "+vehicule.getMatricule()+" avant le "+vehicule.getNextVisite();
            }
            for (ManagerEntity manager:managerService.getAllManagers())
            {
                if(manager.getLastMailSent()==null)
                {
                    this.sendEmail(manager.getEmail(), "SwiftSender : Alert Pour regler" +
                            " les papiers d'une vehicule ", message);
                    manager.setLastMailSent(LocalDate.now());
                    managerService.updateLastMailSent(manager.getEmail() , manager.getLastMailSent());
                }
                else if (LocalDate.now().compareTo(manager.getLastMailSent())>0) {
                    this.sendEmail(manager.getEmail(), "SwiftSender : Alert Pour regler" +
                            " les papiers d'une vehicule ", message);
                    manager.setLastMailSent(LocalDate.now());
                    managerService.updateLastMailSent(manager.getEmail() , manager.getLastMailSent());
                }
            }
        }
    }

    public void notifyClient(String email, ServiceEntity service) {
        this.sendEmail(email,"SwiftSender : Details De votre derniers Service ",service.getFacture());
    }
}