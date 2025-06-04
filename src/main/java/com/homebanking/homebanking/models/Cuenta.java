package com.homebanking.homebanking.models;

public interface Cuenta {
    public void depositar(Double monto);
    public void retirar(Double monto);
    public Double consultarSaldo();
}
