package com.homebanking.homebanking.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="cuenta_bancaria")
public class CuentaBancaria {

    @Id//clave primaria en al bd
    private Long nroCuenta;

    @Embedded
    private Cuenta cuenta;

    public CuentaBancaria(Cuenta c){
        this.cuenta=c;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//Una cuenta tiene muchos movimientos
    //si borro una cuenta, se borran los movimientos de esa cuenta de la bd
    private List<Movimiento> historial = new ArrayList<>();

    public void depositar(double monto) {
        cuenta.depositar(monto);
        historial.add(new Movimiento(this.nroCuenta,TipoMovimiento.Deposito, monto));
    }

    public void retirar(double monto) {
        cuenta.retirar(monto);
        historial.add(new Movimiento(this.nroCuenta,TipoMovimiento.Retiro, -monto));
    }

    public double getSaldo() {
        return cuenta.consultarSaldo();
    }

}
