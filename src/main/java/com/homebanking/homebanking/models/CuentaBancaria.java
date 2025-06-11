package com.homebanking.homebanking.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="cuenta_bancaria")
@Data
public class CuentaBancaria {

    @Id
    private Long nroCuenta;

    @ManyToOne
    private Cliente cliente;

    @Embedded
    private CajaDeAhorro cuenta;
    
    public CuentaBancaria() {
        
    }
    
    public CuentaBancaria(CajaDeAhorro c){
        this.cuenta=c;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> historial = new ArrayList<>();


    public double getSaldo() {
        return cuenta.consultarSaldo();
    }

}
