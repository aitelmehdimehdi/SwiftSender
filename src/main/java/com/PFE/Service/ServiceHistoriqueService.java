package com.PFE.Service;

import com.PFE.Entity.ClientEntity;
import com.PFE.Entity.ConducteurEntity;
import com.PFE.Entity.ServiceHistorique;
import com.PFE.Entity.Vehicule;
import com.PFE.Exeption.ServiceNotFoundException;
import com.PFE.Repositry.ServiceHistoriqueRepository;
import com.PFE.Repositry.ServiceRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@NoArgsConstructor(force = true)

public class ServiceHistoriqueService {
    @Autowired
    private final ServiceHistoriqueRepository historiqueRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private final ServiceRepository serviceService;

    public List<ServiceHistorique> findAllById(Integer id)
    {
        return historiqueRepository.findAllByServiceEntity(serviceService.findById(id).orElseThrow());
    }
    public List<ServiceHistorique> findAllByConducteur(ConducteurEntity conducteur)
    {
        return historiqueRepository.findAllByConducteurEntity(conducteur);
    }
    public List<ServiceHistorique> findAllByConducteurAndInOrOutVille(ConducteurEntity conducteur , Boolean exist)
    {
        return historiqueRepository.findAllByConducteurEntityAndAndServiceEntity_DansLaVille(conducteur,exist);
    }
    public Integer countServiceByConducteur(ConducteurEntity conducteur)
    {
        return historiqueRepository.countByConducteurEntity(conducteur);
    }

    public Integer countServiceByConducteurInOrOutVilleParPeriode(ConducteurEntity conducteur ,Boolean exist, LocalDate date)
    {
        return historiqueRepository.countByConducteurEntityEqualsAndServiceEntity_DansLaVilleEqualsAndServiceEntity_DateServiceAfter(
                conducteur , exist ,date );
    }

    public void disalowClient(ClientEntity client)
    {
       List<ServiceHistorique> serviceHistoriques=historiqueRepository.findAllByClientEntity(client);
       for(ServiceHistorique serviceHistorique:serviceHistoriques)
       {
           serviceHistorique.setClientEntity(null);
           historiqueRepository.save(serviceHistorique);
       }
    }

    public void deleteByClient(ClientEntity client)
    {
        List<ServiceHistorique> serviceHistoriques=historiqueRepository.findAllByClientEntity(client);
        for(ServiceHistorique serviceHistorique:serviceHistoriques)
        {
            historiqueRepository.delete(serviceHistorique);
        }
    }

    public void disalowConducteur(ConducteurEntity conducteur)
    {
        List<ServiceHistorique> serviceHistoriques=historiqueRepository.findAllByConducteurEntity(conducteur);
        for(ServiceHistorique serviceHistorique:serviceHistoriques)
        {
            serviceHistorique.setConducteurEntity(null);
            historiqueRepository.save(serviceHistorique);
        }
    }

    public void disalowVehicule(Vehicule vehicule)
    {
        List<ServiceHistorique> serviceHistoriques=historiqueRepository.findAllByVehicule(vehicule);
        for(ServiceHistorique serviceHistorique:serviceHistoriques)
        {
            serviceHistorique.setVehicule(null);
            historiqueRepository.save(serviceHistorique);
        }
    }
}
