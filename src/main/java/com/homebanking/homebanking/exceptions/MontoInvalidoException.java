package com.homebanking.homebanking.exceptions;

public class MontoInvalidoException extends Exception{

    public MontoInvalidoException () {
        super("El monto a depositar debe ser positivo");
    }

}
