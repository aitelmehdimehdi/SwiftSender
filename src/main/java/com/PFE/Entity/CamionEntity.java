package com.PFE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "camion")
@NoArgsConstructor
public class CamionEntity extends Vehicule {
    public CamionEntity(String matricule , LocalDate finAssurance , LocalDate visite,LocalDate grise){
        super(matricule,1700,200, 10.0,finAssurance,visite,grise);
        this.typeVehicule=this.getClass().getSimpleName();
    }

    @Override
    public Integer getTarifInside() {
        return Integer.valueOf(599);
    }


    public Integer getTarifOutside() {
        return Integer.valueOf(999);
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
