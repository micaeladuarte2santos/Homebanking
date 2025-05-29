package com.homebanking.homebanking.entity;

public interface Cuenta {
    public void depositar(Double monto);
    public void retirar(Double monto);
    public Double consultarSaldo();
}
