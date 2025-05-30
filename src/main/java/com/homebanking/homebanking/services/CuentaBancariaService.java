package com.homebanking.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homebanking.homebanking.repositories.ClienteRepository;
import com.homebanking.homebanking.repositories.CuentaBancariaRepository;
import com.homebanking.homebanking.repositories.MovimientoRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CuentaBancariaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    
}