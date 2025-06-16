package com.homebanking.homebanking.InterfazOperacionesUsuarios;


import java.util.List;

import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;


public interface InterfazOperaciones {
    
    public List<CuentaBancaria> obtenerCuentasPorDni(String dni);
    public CuentaBancaria getCuentaBancaria(Long nroCuenta);
    public CuentaBancaria depositar(Long nroCuenta, double monto);
    public CuentaBancaria retirar(Long nroCuenta, double monto);
    public CuentaBancaria transferir(Long origen, Long destino, double monto);
    public List<Movimiento> obtenerMovimientos(Long nroCuenta);
}
