package com.PFE.Exeption;

public class ConducteurNotFound extends RuntimeException {
    public ConducteurNotFound(String email)
    {
        super("pas de conducteur avec email "+email);
    }
}
