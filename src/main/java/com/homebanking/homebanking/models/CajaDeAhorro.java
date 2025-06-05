package com.homebanking.homebanking.models;

import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable//para no crear la tabla CajaAhorro en la bd
@Data//crear los sets y gets 
@AllArgsConstructor//crea los constructores
@NoArgsConstructor
public class CajaDeAhorro implements Cuenta{

    private double saldo;

    @Override
    public void depositar(Double monto) {
        this.saldo+=monto;
    }

    @Override
    public void retirar(Double monto) {
         if (monto <= saldo) {
            saldo -= monto;
        } else {
            throw new SaldoInsuficienteException();
        }
    }

    @Override
    public Double consultarSaldo() {
        return this.saldo;
    } 
}
