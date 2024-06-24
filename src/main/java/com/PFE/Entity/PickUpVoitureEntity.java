package com.PFE.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pick-up")
@NoArgsConstructor
public class PickUpVoitureEntity extends Vehicule {


    public PickUpVoitureEntity(String matricule , LocalDate finAssurance , LocalDate visite,LocalDate grise){
        super(matricule,400,50,Double.valueOf(5),finAssurance,visite,grise);
        this.typeVehicule=this.getClass().getSimpleName();
    }

    @Override
    public Integer getTarifInside() {
        return Integer.valueOf(99);
    }

    @Override
    public Integer getTarifOutside() {
        return Integer.valueOf(399);
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
