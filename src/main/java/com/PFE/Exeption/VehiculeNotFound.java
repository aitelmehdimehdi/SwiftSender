package com.PFE.Exeption;

public class VehiculeNotFound extends RuntimeException{
    public VehiculeNotFound(String matricule) {
        super("Vehicule de matricule "+matricule+" n'existais plus");
    }
}
