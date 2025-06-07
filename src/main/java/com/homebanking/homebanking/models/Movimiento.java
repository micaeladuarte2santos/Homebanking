package com.homebanking.homebanking.models;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="movimiento")
@Data
public class Movimiento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//crea el id autonumerico
    private Long id;
    private Long nroCuenta;
    private TipoMovimiento descripcion;
    private double monto;
    private LocalDateTime fechaMovimiento;

    public Movimiento() {}

    public Movimiento(Long nroCuenta, TipoMovimiento descripcion, double monto) {
        this.nroCuenta=nroCuenta;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechaMovimiento = LocalDateTime.now();
    }

}
