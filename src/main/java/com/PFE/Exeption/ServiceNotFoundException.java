package com.PFE.Exeption;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(Integer id)
    {
        super("pas de service avec un id "+id);
    }
}
