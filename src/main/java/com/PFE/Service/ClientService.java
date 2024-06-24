package com.PFE.Service;

import com.PFE.Entity.ClientEntity;
import com.PFE.Entity.CompteEntity;
import com.PFE.Entity.ServiceEntity;
import com.PFE.Exeption.ClientNotFound;
import com.PFE.Repositry.ClientRepository;
import com.PFE.Repositry.CompteRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ServiceHistoriqueService historiqueService;
    private final CompteRepository compteRepository;
    private final ServiceService serviceService;

    public Integer countClients()
    {
        return (int)clientRepository.count();
    }

    public ClientEntity saveClient(ClientEntity clientEntity)
    {
        ClientEntity client = clientRepository.save(clientEntity);
        if(clientEntity.getCompte(clientEntity.getClass().getSimpleName()) != null)
        {
            compteRepository.save(clientEntity.getCompte(clientEntity.getClass().getSimpleName()));
        }
        return client;
    }



    public ClientEntity findById(String id)
    {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFound(id));
    }


    public List<ClientEntity> getAll()
    {
        return clientRepository.findAll();
    }


    public Boolean updateClient(String email ,ClientEntity client , HttpSession session)
    {
        if(! clientRepository.existsById(email) )
        {
            return false;
        }
        ClientEntity client1 = clientRepository.findById(email).orElseThrow(
                ()-> new ClientNotFound(email));
        client1.setNom(client.getNom());
        client1.setPrenom(client.getPrenom());
        client1.setTelephone(client.getTelephone());
        client1.setDateNaissance(client.getDateNaissance());
        client1.setFullname(client.getPrenom()+" "+client.getNom());
        clientRepository.save(client1);
        session.setAttribute("clientLoggedIn",findById(client1.getEmail()));
        return true;
    }

    public Boolean setActive(String email)
    {
        if(!clientRepository.existsById(email))
        {
            return false;
        }
        ClientEntity client1 = clientRepository.findById(email).orElseThrow(
                ()-> new ClientNotFound(email));
        client1.setActive(true);
        clientRepository.save(client1);
        return true;
    }


    public Boolean updateClientPassword(String email ,ClientEntity client)
    {
        if(!clientRepository.existsById(email))
        {
            return false;
        }
        else {
            ClientEntity client1 = clientRepository.findById(email).orElseThrow(
                    () -> new ClientNotFound(email));
            client1.setPassword(client.getPassword());
            for(CompteEntity compte : compteRepository.findAllByEmail(email))
            {
                if(Objects.equals(compte.getTypeCompte(), ClientEntity.class.getSimpleName())) {
                    compte.setPassword(client.getPassword());
                    compteRepository.save(compte);
                    System.out.println(compte.getPassword());
                }
            }
            clientRepository.save(client1);
            System.out.println(client1.getPassword());
            System.out.println(clientRepository.findByEmail(client1.getEmail()).getPassword());
            return true;
        }
    }
    public Boolean deleteClient(String id)
    {
        try {
            if (clientRepository.existsById(id)) {
                ClientEntity client = clientRepository.findById(id).orElseThrow();
                historiqueService.deleteByClient(client);
                serviceService.deleteServiceByClient(client);
                //clientRepository.save(client1);
                compteRepository.deleteByEmailAndTypeCompte(client.getEmail(), client.getType());
                clientRepository.deleteById(id);
                return true;
            }
        }catch (SqlScriptException e)
        {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return false;
    }


    public Boolean existsByEmail(String email)
    {

        return  clientRepository.existsByEmail(email);
    }


    public List<ServiceEntity> getAllServiceByCleint(ClientEntity client)
    {
        return serviceService.getAllServiceByClient(client);
    }


    public String clientlogin(CompteEntity compte , HttpSession session)
    {
        session.setAttribute("clientLoggedIn" , clientRepository.findById(compte.getEmail()).
                orElseThrow(()->new ClientNotFound(compte.getEmail())));
        return "redirect:client";
    }




}
