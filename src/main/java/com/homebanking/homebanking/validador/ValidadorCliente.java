package com.homebanking.homebanking.validador;

import org.springframework.stereotype.Component;

import com.homebanking.homebanking.exceptions.DniInvalidoException;

@Component
public class ValidadorCliente {

    public void validarDniCliente(String dni) {
		
		if(!dni.matches("\\d{8}")) {
			throw new DniInvalidoException(dni);
		}
		
	}

}
