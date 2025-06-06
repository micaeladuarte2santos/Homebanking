package com.homebanking.homebanking.models;

import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;

public class Transferencia implements Operacion{

    private CuentaBancaria origen;
    private CuentaBancaria destino; 
    private double monto;

    public Transferencia(CuentaBancaria origen ,CuentaBancaria destino, double monto){
        this.origen=origen;
        this.destino=destino;
        this.monto=monto;
    }

    @Override
    public void ejecutar() {
        if(origen.getSaldo()>= monto){
            origen.getCuenta().retirar(monto);
            destino.getCuenta().depositar(monto);
        }else{
            throw new SaldoInsuficienteException();
        }
    }

}
