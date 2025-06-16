package com.homebanking.homebanking.exceptions;

public class CuentaInvalidaException extends Exception {

    public CuentaInvalidaException () {
        super("Nro de cuenta invalido");
    }
}
