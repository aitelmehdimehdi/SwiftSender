package com.PFE.etc.Comparators;

import com.PFE.Entity.Vehicule;

import java.util.Comparator;

public class VehiculeComparator implements Comparator<Vehicule> {
    @Override
    public int compare(Vehicule o1, Vehicule o2) {
        int activeComparison = Boolean.compare(o2.getActive(), o1.getActive());
        return activeComparison;
    }
}
