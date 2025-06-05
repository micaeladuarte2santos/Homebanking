package com.homebanking.homebanking.exceptions;

public class CuentaInexistenteException extends Exception{

    public CuentaInexistenteException(Long nroCruenta) {
        super("El número de cuenta " +nroCruenta+ " es inexistente.");
    }

}
