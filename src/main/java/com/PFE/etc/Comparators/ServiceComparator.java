package com.PFE.etc.Comparators;

import com.PFE.Entity.ServiceEntity;

import java.util.Comparator;

public class ServiceComparator implements Comparator<ServiceEntity> {
    @Override
    public int compare(ServiceEntity o1, ServiceEntity o2) {
        int activeComparison = Boolean.compare(o2.getActive(),o1.getActive());
        if (activeComparison != 0)
            return activeComparison;
        else
            return o2.getDateService().compareTo(o1.getDateService());
    }
}