package com.PFE.etc.Comparators;

import com.PFE.Entity.ClientEntity;

import java.util.Comparator;

public class ClientComparator implements Comparator<ClientEntity> {
    @Override
    public int compare(ClientEntity o1, ClientEntity o2) {
        return  Boolean.compare(o2.getActive(), o1.getActive());
    }
}