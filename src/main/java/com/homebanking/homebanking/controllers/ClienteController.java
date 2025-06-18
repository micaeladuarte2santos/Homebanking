package com.homebanking.homebanking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.homebanking.homebanking.models.Cliente;
import com.homebanking.homebanking.services.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> obtenerClientePorDni(@PathVariable String dni) {
        Cliente cliente = clienteService.buscarClientePorDni(dni); 
        return ResponseEntity.ok(cliente);
    }
}
