package com.PFE.etc.Comparators;

import com.PFE.Entity.ConducteurEntity;

import java.util.Comparator;

public class ConducteurComparator implements Comparator<ConducteurEntity> {
    @Override
    public int compare(ConducteurEntity o1, ConducteurEntity o2) {
        return  Boolean.compare(o2.getActive(), o1.getActive());
    }
}
