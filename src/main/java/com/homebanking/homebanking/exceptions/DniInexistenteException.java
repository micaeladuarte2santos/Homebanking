package com.homebanking.homebanking.exceptions;

public class DniInexistenteException extends Exception{

    public DniInexistenteException(String dni) {
        super("El DNI:"+dni+" no existe en la base de datos");
    }

}
