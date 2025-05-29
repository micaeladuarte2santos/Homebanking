package com.homebanking.homebanking.entity;

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
            origen.retirar(monto);
            destino.depositar(monto);
        }else{
            throw new IllegalArgumentException("Saldo insuficiente en transferencia");
        }
    }

}
