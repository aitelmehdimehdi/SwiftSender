package com.PFE.Exeption;

public class ClientNotFound extends RuntimeException{
    public ClientNotFound(String id)
    {

        super("Client "+id+" not found ");
    }
}
