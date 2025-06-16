package com.homebanking.homebanking.cli;

import java.util.Scanner;

import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.models.CuentaBancaria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
public class DepositosCli {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private InterfazServiceCli interfazServiceCli;

    public DepositosCli() {
        this.interfazServiceCli = null;
    }
    
    public void depositar(Long nroCuenta) {
        while (true) {
            try {
                System.out.print("Monto a depositar (o '0' para cancelar): ");
                double montoADepositar = Double.parseDouble(scanner.nextLine());

                if (montoADepositar == 0) {
                    System.out.println("Operación cancelada.");
                    return;
                }
                
                

                CuentaBancaria cuentaOrigen = interfazServiceCli.depositar(nroCuenta, montoADepositar);

                System.out.println("Depósito realizado con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n",
                        cuentaOrigen.getNroCuenta(), cuentaOrigen.getCuenta().getSaldo());

                return;

            } catch (MontoInvalidoException e) {
                System.out.println(e.getMessage());
            }catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un monto valido");
            }
        }
    }
}
