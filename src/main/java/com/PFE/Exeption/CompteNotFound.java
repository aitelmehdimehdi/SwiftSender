package com.PFE.Exeption;

public class CompteNotFound extends RuntimeException{
    public CompteNotFound(Integer id)
    {
        super("Compte "+ id+" introuvable");
    }
}
