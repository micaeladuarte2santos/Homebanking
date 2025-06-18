package com.homebanking.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.models.Cliente;
import com.homebanking.homebanking.repositories.ClienteRepository;
import com.homebanking.homebanking.validador.ValidarCliente;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ValidarCliente validador;

    @Transactional(readOnly = true)
    public Cliente buscarClientePorDni(String dni) {
        validador.validar(dni);
        return clienteRepository.findByDni(dni).orElseThrow(() -> new DniInexistenteException(dni));
    }

}
