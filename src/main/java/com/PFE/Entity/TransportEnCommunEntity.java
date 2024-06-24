package com.PFE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transit")
@NoArgsConstructor
public class TransportEnCommunEntity extends Vehicule {

    public TransportEnCommunEntity(String matricule , LocalDate finAssurance , LocalDate visite,LocalDate grise){
        super(matricule,1000,100, 7.0,finAssurance,visite,grise);
        this.typeVehicule=this.getClass().getSimpleName();
    }

    @Override
    public Integer getTarifInside() {
        return 249;
    }

    @Override
    public Integer getTarifOutside() {
        return 499;
    }

    @Override
    public String getSimpleType() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
