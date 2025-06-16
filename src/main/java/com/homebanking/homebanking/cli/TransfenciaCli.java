package com.homebanking.homebanking.cli;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.models.CuentaBancaria;

public class TransfenciaCli {

    private final Scanner scanner = new Scanner(System.in);


    public void transferir(CuentaBancaria cuentaOrigen) {
        while (true) {
            try {
                System.out.print("Nro de cuenta destino (o '0' para cancelar): ");
                Long nroCuentaDestino = Long.parseLong(scanner.nextLine());

                if (nroCuentaDestino == 0) {
                    System.out.println("Transferencia cancelada.");
                    return;
                }
                
                System.out.print("Monto a transferir: ");
                double monto = Double.parseDouble(scanner.nextLine());
                
                InterfazServiceCli.getInstancia().transferir(cuentaOrigen.getNroCuenta(), nroCuentaDestino, monto);


                System.out.println("Transferencia realizada con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", cuentaOrigen.getNroCuenta(), cuentaOrigen.getSaldo());
                return; // salir del bucle
                } catch (MontoInvalidoException e) {
                    System.out.println(e.getMessage());
                }catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un monto valido");
                }
            }
    }
}
