package com.homebanking.homebanking.models;

import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CajaDeAhorro implements Cuenta{

    private double saldo;

    @Override
    public void depositar(Double monto) {
        if (monto <= 0) {
            throw new MontoInvalidoException();
        }
        this.setSaldo(this.getSaldo() + monto);
    }

    @Override
    public void retirar(Double monto) {
        if (monto <= 0) {
            throw new MontoInvalidoException();
        }

        if (monto <= this.getSaldo()) {
            this.setSaldo(this.getSaldo() - monto);
        } else {
            throw new SaldoInsuficienteException();
        }
    }

    @Override
    public Double consultarSaldo() {
        return this.saldo;
    } 
}
