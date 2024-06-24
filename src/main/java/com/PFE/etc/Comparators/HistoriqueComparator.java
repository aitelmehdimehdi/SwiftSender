package com.PFE.etc.Comparators;

import com.PFE.Entity.ServiceHistorique;

import java.util.Comparator;

public class HistoriqueComparator implements Comparator<ServiceHistorique> {
    @Override
    public int compare(ServiceHistorique o1, ServiceHistorique o2) {
        int activeComparison = Boolean.compare(o2.getServiceEntity().getActive(),o1.getServiceEntity().getActive());
        if (activeComparison != 0)
            return activeComparison;
        else
            return o2.getServiceEntity().getDateService().compareTo(o1.getServiceEntity().getDateService());
    }
}