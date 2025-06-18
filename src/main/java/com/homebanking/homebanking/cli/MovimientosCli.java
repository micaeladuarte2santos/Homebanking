package com.homebanking.homebanking.cli;

import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("cli")
public class MovimientosCli {

    @Autowired
    private ManejadorOperacionesCli manejadorOperacionesCli;
    
    public void mostrarMovimientos(Long nroCuenta){

        System.out.println("\nMovimientos de la cuenta Nº " + nroCuenta);
        var movimientos = manejadorOperacionesCli.obtenerMovimientos(nroCuenta);
        
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (var movimiento : movimientos) {
            String fechaFormateada = movimiento.getFechaMovimiento().format(formatter);
            String descripcionLegible = movimiento.getDescripcion().name();
            double monto = movimiento.getMonto();

            System.out.printf("%s - %s de $%.2f\n", fechaFormateada, descripcionLegible, monto);
        }
        }
    }

}
