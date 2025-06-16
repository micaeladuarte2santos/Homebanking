package com.homebanking.homebanking.InterfazOperacionesUsuarios;


import java.util.List;

import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;


public interface InterfazOperaciones {
    
    List<CuentaBancaria> obtenerCuentasPorDni(String dni);
    CuentaBancaria getCuentaBancaria(Long nroCuenta);
    void depositar(Long nroCuenta, double monto);
    void retirar(Long nroCuenta, double monto);
    void transferir(Long origen, Long destino, double monto);
    List<Movimiento> obtenerMovimientos(Long nroCuenta);
}
