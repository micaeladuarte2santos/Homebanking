package com.homebanking.homebanking.exceptions;

public class DniInvalidoException extends Exception{

    public DniInvalidoException(String dni) {
		super("El DNI:"+dni+" es invalido");
	}

}
