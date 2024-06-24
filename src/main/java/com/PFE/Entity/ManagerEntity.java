package com.PFE.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "manager")
@Data
public class ManagerEntity extends Person{
    private LocalDate lastMailSent;

    public ManagerEntity(String email, String nom , String prenom , String genre
            , String telephone, LocalDate dateNaissance, String password){
        super(nom,
                prenom,
                genre,
                dateNaissance,
                telephone,
                password,
                email);
        this.compte=new LinkedList<CompteEntity>();
        this.compte.add(new CompteEntity(this,LocalDate.now()));
        this.lastMailSent=null;
    }
    public  ManagerEntity()
    {
        super();
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
