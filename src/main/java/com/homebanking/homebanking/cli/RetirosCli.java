package com.homebanking.homebanking.cli;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;
import com.homebanking.homebanking.models.CuentaBancaria;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

@Component
@Profile("cli")
public class RetirosCli {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ManejadorOperacionesCli manejadorOperacionesCli;
    
    public void retirar(Long nroCuenta) {

        while (true) {
            try {
                System.out.print("Monto a retirar (o '0' para cancelar): ");
                double montoARetirar = Double.parseDouble(scanner.nextLine());

                if (montoARetirar == 0) {
                    System.out.println("Operación cancelada.");
                    return;
                }

                CuentaBancaria cuentaOrigen = manejadorOperacionesCli.retirar(nroCuenta, montoARetirar);

                System.out.println("Retiro realizado con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", nroCuenta, cuentaOrigen.getCuenta().getSaldo());

                return; // salir del bucle
            } catch (MontoInvalidoException e) {
                System.out.println(e.getMessage());
            }catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un monto valido");
            }catch(SaldoInsuficienteException e){
                    System.out.println(e.getMessage());
            }
        }
    }

}
