package com.homebanking.homebanking.validador;

import org.springframework.stereotype.Component;

import com.homebanking.homebanking.exceptions.DniInvalidoException;

@Component
public class ValidarCliente {

    public void validar(String dni) {
		
		if(!dni.matches("\\d{8}")) {
			throw new DniInvalidoException(dni);
		}
		
	}

}
