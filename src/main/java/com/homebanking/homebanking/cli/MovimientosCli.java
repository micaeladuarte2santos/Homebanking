package com.homebanking.homebanking.cli;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import com.homebanking.homebanking.models.Movimiento;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

@Component
@Profile("cli")
public class MovimientosCli {

    @Autowired
    private InterfazServiceCli interfazServiceCli;
    
    public void mostrarMovimientos(Long nroCuenta){

        System.out.println("\nMovimientos de la cuenta Nº " + nroCuenta);
        var movimientos = interfazServiceCli.obtenerMovimientos(nroCuenta);
        
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
