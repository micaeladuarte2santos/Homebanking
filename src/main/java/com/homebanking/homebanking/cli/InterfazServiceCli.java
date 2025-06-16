package com.homebanking.homebanking.cli;

import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.exceptions.DniInvalidoException;
import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.TipoMovimiento;
import com.homebanking.homebanking.services.ClienteService;
import com.homebanking.homebanking.services.CuentaBancariaService;
import com.homebanking.homebanking.models.Movimiento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Scanner;

import com.homebanking.homebanking.InterfazOperacionesUsuarios.InterfazOperaciones;


public class InterfazServiceCli implements InterfazOperaciones{


    @Autowired
    private CuentaBancariaService cuentaBancariaService;

    private final Scanner scanner = new Scanner(System.in);
    private static InterfazServiceCli instancia;


    public static InterfazServiceCli getInstancia() {
        if (instancia == null) {
            instancia = new InterfazServiceCli();
        }
        return instancia;
    }


    public List<CuentaBancaria> obtenerCuentasPorDni(String dni){
        return cuentaBancariaService.encontrarCuentasPorDNI(dni);
    }

    public CuentaBancaria getCuentaBancaria(Long nroCuenta){

        return cuentaBancariaService.buscarCuenta(nroCuenta);
    }

    public CuentaBancaria depositar(Long nroCuenta, double monto) {

        return cuentaBancariaService.depositar(nroCuenta, monto);    
        
    }

    public CuentaBancaria retirar(Long nroCuenta, double monto) {

        return cuentaBancariaService.retirar(nroCuenta, monto);
    }


    public void transferir(Long nroCuentaOrigen, Long nroCuentaDestino, double monto) {

        CuentaBancaria cuentaOrigen = getCuentaBancaria(nroCuentaOrigen);
        CuentaBancaria cuentaDestino = getCuentaBancaria(nroCuentaDestino);

        cuentaBancariaService.transferir(cuentaOrigen, cuentaDestino, monto);

    }

    public List<Movimiento> obtenerMovimientos(Long nroCuenta) {

        return cuentaBancariaService.obtenerMovimientosDeCuenta(nroCuenta);
    }

}