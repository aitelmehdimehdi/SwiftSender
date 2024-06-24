package com.PFE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "remorque")
@NoArgsConstructor
public class RemorqueEntity extends Vehicule {

    public RemorqueEntity(String matricule , LocalDate finAssurance , LocalDate visite,LocalDate grise){
        super(matricule,4000,400,Double.valueOf(14),finAssurance,visite,grise);
        this.typeVehicule=this.getClass().getSimpleName();
    }

    @Override
    public Integer getTarifInside() {
        return Integer.valueOf(799);
    }

    @Override
    public Integer getTarifOutside() {
        return Integer.valueOf(1499);
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
